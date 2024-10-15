package trainsofsheffield.views.customerarea.productpage;

import trainsofsheffield.models.*;
import trainsofsheffield.models.Controller.ControllerType;
import trainsofsheffield.models.Locomotive.DccCode;
import trainsofsheffield.models.Product.*;
import trainsofsheffield.models.RollingStock.BRStandardMark;
import trainsofsheffield.models.RollingStock.CompanyMark;
import trainsofsheffield.models.RollingStock.RollingStockType;
import trainsofsheffield.views.staffarea.AddBoxedSetPanel;
import trainsofsheffield.views.staffarea.AddProductPanel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ProductTablePanel extends JPanel{
    private JTable table;
    private ProductCategory pCategory;
    private Session session;
    private User user;
    private Boolean isStuffArea;
    private OrderDatabaseOperations orderDatabaseOperations = new OrderDatabaseOperations();
    private ProductDatabaseOperations productDatabaseOperations = new ProductDatabaseOperations();
    public ProductTablePanel (ProductCategory productCategory, Session session, Boolean isStuffArea) {
        this.pCategory = productCategory;
        this.session = session;
        this.user = session.getUser();
        this.isStuffArea = isStuffArea;

        setLayout(new BorderLayout());
        add(new JLabel(productCategory.toString()), BorderLayout.NORTH);

        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();

            displayTable(databaseConnectionHandler.getConnection());
            addButton(user.getUserID(), databaseConnectionHandler.getConnection());

        } catch (Throwable t) {
            add(new JLabel("Error: can't load database table"), BorderLayout.CENTER);
            throw new RuntimeException(t);
        } finally {
            databaseConnectionHandler.closeConnection();
        }
    }

    private void displayTable(Connection connection) throws Exception {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //create a DefaultTableModel for the JTable
        DefaultTableModel tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        //Add the shared columns for all the products to the tableModel
        ArrayList<String> columnNames = new ArrayList<>(Arrays.asList(
                "Product ID", "Product Name", "Price (per product)",
                "Number in Stock", "Gauge", "Manufacturer"
        ));

        //Add all the product-category-dependant columns to the tableModel
        if (pCategory == ProductCategory.LOCOMOTIVES ||
                pCategory == ProductCategory.ROLLING_STOCKS ||
                pCategory == ProductCategory.TRAIN_SETS) {

            columnNames.add("Era");
            if (pCategory == ProductCategory.LOCOMOTIVES) {
                columnNames.add("DCC Code");
            }
            else if (pCategory == ProductCategory.ROLLING_STOCKS) {
                columnNames.add("BR Standard");
                columnNames.add("Company Mark");
            }
        }
        else if (pCategory == ProductCategory.CONTROLLERS) {
            columnNames.add("Controller Type");
            columnNames.add("Digital");
        }
        if (!isStuffArea) {
            columnNames.add("Quantity in Current Order");
        }

        for (String name : columnNames) {
            tableModel.addColumn(name);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        ResultSet resultSet = productDatabaseOperations.getAllProductsOfCategory(pCategory, connection);
        //Populate the JTable with the query results
        while (resultSet.next()) {
            ArrayList<Object> columnValueList = new ArrayList<> ( Arrays.asList(
                    resultSet.getString("productID"),
                    resultSet.getString("productName"),
                    resultSet.getBigDecimal("retailPrice"),
                    resultSet.getInt("quantityInStock"),
                    resultSet.getString("gauge"),
                    resultSet.getString("manufacturerName")
            ));

            if (pCategory == ProductCategory.LOCOMOTIVES ||
                pCategory == ProductCategory.ROLLING_STOCKS ||
                pCategory == ProductCategory.TRAIN_SETS) {

                columnValueList.add(resultSet.getString("eraCode"));

                if (pCategory == ProductCategory.LOCOMOTIVES) {
                    columnValueList.add(resultSet.getString("DCCode"));
                }
                else if (pCategory == ProductCategory.ROLLING_STOCKS) {
                    columnValueList.add(resultSet.getString("BRStandardMark"));
                    columnValueList.add(resultSet.getString("companyMark"));
                }
            }
            else if (pCategory == ProductCategory.CONTROLLERS) {
                columnValueList.add(resultSet.getString("controllerType"));
                columnValueList.add(resultSet.getBoolean("isDigital"));
            }


            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            /*
            Setting the column value for the last column which is for the quantity in current order, to match
            what is in the database for that users current order if they have one
             */
            if (orderDatabaseOperations.userHasCurrentOrder(session.getUser().getUserID(), connection)) {

                int productID = resultSet.getInt("productID");
                if (orderDatabaseOperations.isProductIDInCurrentOrderLinesForUser(connection, session.getUser().getUserID(), productID)) {

                    int alreadySelectedQuantityForProductID = orderDatabaseOperations.getAlreadySelectedQuantityForProductID(connection, session.getUser().getUserID(), productID);
                    columnValueList.add(alreadySelectedQuantityForProductID);

                } else {
                    columnValueList.add(0); //for the quantity in Current Order, because that's what it is by default
                }

            } else {
                columnValueList.add(0); //for the quantity in Current Order, because that's what it is by default
            }

            tableModel.addRow(columnValueList.toArray());
        }

        resultSet.close();

        //
        if (!isStuffArea) {
            makeColumnErrorHandle(table.getColumn("Quantity in Current Order"), getStockValuesMap(connection));
        }

        //

        //Create a JScrollPane to display the table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void makeColumnErrorHandle(TableColumn column, HashMap<Integer, Integer> maxValuesHashMap) {
        column.setCellEditor(new DefaultCellEditor(new JTextField()) {
            private boolean editingStopped = false; // Flag to track if editing is stopped

            @Override
            public Object getCellEditorValue() {
                // Only perform validation if editing hasn't been stopped due to an error
                if (!editingStopped) {
                    // Get the row and original value
                    int row = table.getEditingRow();
                    String originalValue = table.getValueAt(row, table.getColumnCount() - 1).toString();

                    // Intercept the new value before it's set in the model
                    String newValue = (String) super.getCellEditorValue();

                    try {
                        int intValue = Integer.parseInt(newValue);
                        int maxValue = maxValuesHashMap.get(row);
                        if (intValue > maxValue) {
                            // Show an error message
                            String errorMessage = "Selected Value exceeds stock level. Stock level: " + maxValue;
                            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);

                            // Mark editing as stopped to prevent further validation attempts
                            editingStopped = true;

                            // Return the original value to prevent changes
                            return originalValue;
                        } else {
                            return newValue;
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        // If the value is not an integer or if max value is not found, show an error
                        JOptionPane.showMessageDialog(null,
                                "Invalid input or max value not found for this row! Enter a number.",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        // Mark editing as stopped to prevent further validation attempts
                        editingStopped = true;

                        // Return the original value to prevent changes
                        return originalValue;
                    }
                } else {
                    // If editing is already stopped due to an error, return null to cancel cell editing
                    return null;
                }
            }

            @Override
            public boolean stopCellEditing() {
                // Reset the flag when cell editing is stopped
                editingStopped = false;
                return super.stopCellEditing();
            }
        });
    }


    public void addButton(String userID, Connection connection) throws SQLException{
        JButton button;
        if (!isStuffArea) {
            if (orderDatabaseOperations.userHasCurrentOrder(userID, connection)) {
                button = new JButton("Update Current Order");;
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DatabaseConnectionHandler dch = new DatabaseConnectionHandler();
                        try {
                            dch.openConnection();

                            updateCurrentOrder(dch.getConnection(), session.getUser().getUserID());

                        } catch (SQLException x) {
                            x.printStackTrace();
                        } catch (Throwable t) {
                            add(new JLabel("Error: can't load database table"), BorderLayout.CENTER);
                            throw new RuntimeException(t);
                        } finally {
                            dch.closeConnection();
                        }
                    }
                });
                add(button, BorderLayout.SOUTH);
            }
            else {
                button = new JButton("Create Order");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DatabaseConnectionHandler dch = new DatabaseConnectionHandler();
                        try {
                            dch.openConnection();

                            createOrder(dch.getConnection());
                        } catch (SQLException x) {
                            x.printStackTrace();
                        } catch (Throwable t) {
                            add(new JLabel("Error: can't load database table"), BorderLayout.CENTER);
                            throw new RuntimeException(t);
                        } finally {
                            dch.closeConnection();
                        }
                    }
                });
                add(button, BorderLayout.SOUTH);
            }
        }
        else {
            button = new JButton("Edit Stock");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int rowCount = table.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        int productIDForThisRow = Integer.parseInt(table.getValueAt(i, 0).toString());
                        int quantitySelectedInCurrentOrderForThisRow = Integer.parseInt(table.getValueAt(i, 3).toString());
                        ProductDatabaseOperations.updateStock(productIDForThisRow, quantitySelectedInCurrentOrderForThisRow);
                    }
                }
            });

            JButton deleteButton = new JButton("Delete Product");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedProductID;
                    try {
                        selectedProductID = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Please choose the product to delete", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        ProductDatabaseOperations.deleteProduct(selectedProductID);
                    }
                }
            });

            JButton addButton = new JButton("Add Product");
            String categoryString = pCategory.toString();
            String message = "Add " + categoryString.substring(0, categoryString.length() - 1);
            AddBoxedSetPanel addBoxedSetPanel = new AddBoxedSetPanel(pCategory, connection);
            AddProductPanel addProductPanel = new AddProductPanel(pCategory);
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (pCategory.equals(ProductCategory.TRACK_PACKS) || pCategory.equals(ProductCategory.TRAIN_SETS)) {
                        Object[] options = {"Add"};
                        int n = JOptionPane.showOptionDialog(null,
                            addBoxedSetPanel,
                            message,
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                        if (n == 0) {
                            String productName = addBoxedSetPanel.getProductName();
                            String manufacturer = addBoxedSetPanel.getManufacturer();
                            Gauge gauge = addBoxedSetPanel.getGauge();
                            BigDecimal retailPrice = BigDecimal.ZERO;
                            int quantityInStock = 0;

                            try {
                                retailPrice = new BigDecimal(addBoxedSetPanel.getRetailPrice());
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input for Retail Price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            try {
                                quantityInStock = Integer.parseInt(addBoxedSetPanel.getQuantityInStock());
                            } catch (Exception exe) {
                                JOptionPane.showMessageDialog(null, "Invalid input for Quantity In Stock. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            
                            TrackPack trackPack = new TrackPack(productName, retailPrice, quantityInStock, manufacturer, gauge, addBoxedSetPanel.getTrackPackComponents());
                            trackPack.setProductCategory(ProductCategory.TRACK_PACKS);
                            try {
                                productDatabaseOperations.addNewProduct(trackPack);
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                            System.out.println(trackPack);
                        }
                    }
                    else {
                        Object[] options = {"Add"};
                        int n = JOptionPane.showOptionDialog(null,
                            addProductPanel,
                            message,
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                        if (n == 0) {
                            String productName = addProductPanel.getProductName();
                            String manufacturer = addProductPanel.getManufacturer();
                            Gauge gauge = addProductPanel.getGauge();
                            BigDecimal retailPrice = BigDecimal.ZERO;
                            int quantityInStock = 0;
                            String eraCode;

                            try {
                                retailPrice = new BigDecimal(addProductPanel.getRetailPrice());
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input for Retail Price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            try {
                                quantityInStock = Integer.parseInt(addProductPanel.getQuantityInStock());
                            } catch (Exception exe) {
                                JOptionPane.showMessageDialog(null, "Invalid input for Quantity In Stock. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            try {
                                switch (pCategory) {
                                    case TRACKS:
                                        Track track = new Track(productName, retailPrice, quantityInStock, manufacturer, gauge);
                                        track.setProductCategory(ProductCategory.TRACKS);
                                        productDatabaseOperations.addNewProduct(track);
                                        break;
                                    case LOCOMOTIVES:
                                        eraCode = addProductPanel.getEraCode().toString();
                                        DccCode dccCode = addProductPanel.getDccCode();

                                        Locomotive locomotive = new Locomotive(productName, retailPrice, quantityInStock, manufacturer, gauge, dccCode, eraCode);
                                        locomotive.setProductCategory(ProductCategory.LOCOMOTIVES);
                                        productDatabaseOperations.addNewProduct(locomotive);
                                        break;
                                    case ROLLING_STOCKS:
                                        RollingStockType rollingStockType = addProductPanel.getRollingStockType();
                                        CompanyMark companyMark = addProductPanel.getCompanyMark();
                                        BRStandardMark bRStandardMark = addProductPanel.getBRStandardMark();
                                        eraCode = addProductPanel.getEraCode().toString();

                                        RollingStock rollingStock = new RollingStock(productName, retailPrice, quantityInStock, manufacturer, gauge, bRStandardMark, companyMark, rollingStockType, eraCode);
                                        rollingStock.setProductCategory(ProductCategory.ROLLING_STOCKS);
                                        productDatabaseOperations.addNewProduct(rollingStock);
                                        break;
                                    case CONTROLLERS:
                                        Boolean isDigital = addProductPanel.getIsDigital();
                                        ControllerType controllerType = addProductPanel.getControllerType();

                                        Controller controller = new Controller(productName, retailPrice, quantityInStock, manufacturer, gauge, isDigital, controllerType);
                                        controller.setProductCategory(ProductCategory.CONTROLLERS);
                                        productDatabaseOperations.addNewProduct(controller);
                                        break;
                                    default:
                                        // TODO: Add your action here
                                        break;
                                }
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                        }
                    }
                }
            });

            JPanel buttoPanel = new JPanel(new GridLayout(1, 3));
            buttoPanel.add(button);
            buttoPanel.add(deleteButton);
            buttoPanel.add(addButton);
            
            add(buttoPanel, BorderLayout.SOUTH);
        }
    }

    private void updateCurrentOrder(Connection connection, String userID) throws Exception {

        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {

            int quantitySelectedInCurrentOrderForThisRow = Integer.parseInt(table.getValueAt(i, table.getColumnCount() - 1).toString());
            int productIDForThisRow = Integer.parseInt(table.getValueAt(i, 0).toString());

            int orderIDOfCurrentOrder = orderDatabaseOperations.getOrderIDOfCurrentOrder(connection, userID);

            //if the productID of the row already has an orderline in the current order
                //if the quantity selected now equals zero for that row
                    //delete this orderline from currentorder
                //if the quantity selected now is > zero
                    //then edit the quantity of this orderline in current order
            //if the productID of the row doesn't already have an orderline in the current order
                //add order line

            if (orderDatabaseOperations.isProductIDInCurrentOrderLinesForUser(connection, userID, productIDForThisRow)) {

                int orderLineID = orderDatabaseOperations.getOrderLineIDFromCurrentOrderForProductID(connection, productIDForThisRow, userID);

                if (quantitySelectedInCurrentOrderForThisRow == 0) {
                    orderDatabaseOperations.deleteOrderLineFromDatabase(orderLineID);
                    System.out.println("deleting orderline");
                } else {
                    System.out.println("updating orderline");
                    OrderDatabaseOperations.updateQuantityInDatabase(quantitySelectedInCurrentOrderForThisRow, orderLineID);
                }

            } else {
                System.out.println("adding new orderline");
                orderDatabaseOperations.addOrderLine(productIDForThisRow, quantitySelectedInCurrentOrderForThisRow, orderIDOfCurrentOrder, connection);
            }


        }

    }

    private void createOrder(Connection connection) throws SQLException{
        orderDatabaseOperations.addCurrentOrder(connection, session.getUser().getUserID(), getProductIDAndQuantityMap());
    }

    private HashMap<Integer, Integer> getStockValuesMap (Connection connection) throws SQLException {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int productID = 0;
            try {
                Object productIDObject = table.getValueAt(i, 0);
                productID = Integer.parseInt(productIDObject.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Not a valid integer");
            }

            int stockLevel = productDatabaseOperations.getStockFromProductID(connection, productID);

            hashMap.put(i, stockLevel);
        }

        return hashMap;
    }

    public HashMap<Integer, Integer> getProductIDAndQuantityMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int columnIndexOfQuantitySelected = table.getColumnCount() - 1;
            int productID = Integer.parseInt(table.getValueAt(i, 0).toString());
            int quantitySelected = Integer.parseInt(table.getValueAt(i, columnIndexOfQuantitySelected).toString());

            if (quantitySelected != 0) {
                hashMap.put(productID, quantitySelected);
            }
        }

        return hashMap;
    }

}
