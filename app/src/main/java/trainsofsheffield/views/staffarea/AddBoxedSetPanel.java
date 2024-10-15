package trainsofsheffield.views.staffarea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import trainsofsheffield.models.Product.Gauge;
import trainsofsheffield.models.Product.ProductCategory;
import trainsofsheffield.models.ProductDatabaseOperations;
import trainsofsheffield.models.Track;

public class AddBoxedSetPanel extends JPanel {
    private JTextField productNameField;
    private JTextField retailPriceField;
    private JTextField quantityInStockField;
    private JTextField manufacturerField;
    private JComboBox<Gauge> gaugeComboBox;
    private JTable trackTable, tackPackComponentsTable;
    private ProductDatabaseOperations productDatabaseOperations = new ProductDatabaseOperations();

    public AddBoxedSetPanel(ProductCategory category, Connection connection) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        // Producut input fields
        JPanel fieldsPanel =  new JPanel(new GridLayout(5, 2));
        JPanel tables = new JPanel(new GridLayout(2, 1));

        // productName
        fieldsPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        fieldsPanel.add(productNameField);

        // retailPrice
        fieldsPanel.add(new JLabel("Retail Price:"));
        retailPriceField = new JTextField();
        fieldsPanel.add(retailPriceField);

        // quantityInStock
        fieldsPanel.add(new JLabel("Quantity in Stock:"));
        quantityInStockField = new JTextField();
        fieldsPanel.add(quantityInStockField);

        // manufacturer
        fieldsPanel.add(new JLabel("Manufacturer:"));
        manufacturerField = new JTextField();
        fieldsPanel.add(manufacturerField);

        // gauge
        fieldsPanel.add(new JLabel("Gauge:"));
        gaugeComboBox = new JComboBox<>(Gauge.values());
        fieldsPanel.add(gaugeComboBox);
        
        // Add the shared columns for all the products to the tableModel
        ArrayList<String> columnNames = new ArrayList<>(Arrays.asList(
                "Product ID", "Product Name", "Price (per product)",
                "Number in Stock", "Gauge", "Manufacturer"
        ));

        if (category.equals(ProductCategory.TRACK_PACKS)) {
            // Tracks Tabel
            DefaultTableModel trackTableModel = new DefaultTableModel();
            JPanel tracksPanel = new JPanel(new BorderLayout());
            JLabel tracksLabel = new JLabel("Tracks Available:");
            trackTable = new JTable(trackTableModel);


            for (String name : columnNames) {
                trackTableModel.addColumn(name);
            }

            try {
                ResultSet resultSet = productDatabaseOperations.getAllProductsOfCategory(ProductCategory.TRACKS, connection);
                while (resultSet.next()) {
                    ArrayList<Object> columnValueList = new ArrayList<> ( Arrays.asList(
                            resultSet.getString("productID"),
                            resultSet.getString("productName"),
                            resultSet.getBigDecimal("retailPrice"),
                            resultSet.getInt("quantityInStock"),
                            resultSet.getString("gauge"),
                            resultSet.getString("manufacturerName")
                    ));
                    trackTableModel.addRow(columnValueList.toArray());
                }
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            JScrollPane scrollPane1 = new JScrollPane(trackTable);
            tracksPanel.add(tracksLabel, BorderLayout.NORTH);
            tracksPanel.add(scrollPane1, BorderLayout.CENTER);

            // Track Pack's Components table
            DefaultTableModel trackPackComponentsTableModel = new DefaultTableModel();
            JPanel trackPackComponentsPanel = new JPanel(new BorderLayout());
            JLabel trackPackComponentsLabel = new JLabel("Components of the Track Pack:");
            tackPackComponentsTable = new JTable(trackPackComponentsTableModel);

            for (String name : columnNames) {
                trackPackComponentsTableModel.addColumn(name);
            }
            trackPackComponentsTableModel.addColumn("Quantity");

            JButton addSelectedProduct = new JButton("Add Component");
            addSelectedProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedRows = trackTable.getSelectedRows();
                    if (selectedRows.length == 0) {
                        JOptionPane.showMessageDialog(null, "No tracks selected. Please select a track to add.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
            
                    DefaultTableModel trackTableModel = (DefaultTableModel) trackTable.getModel();
                    DefaultTableModel trackPackComponentsTableModel = (DefaultTableModel) tackPackComponentsTable.getModel();
            
                    for (int i = 0; i < selectedRows.length; i++) {
                        Object[] rowData = new Object[trackTableModel.getColumnCount()];
                        for (int j = 0; j < trackTableModel.getColumnCount(); j++) {
                            rowData[j] = trackTableModel.getValueAt(selectedRows[i], j);
                        }
            
                        // Check if the product already exists in the trackPackComponentsTable
                        int existingRowIndex = -1;
                        for (int j = 0; j < trackPackComponentsTableModel.getRowCount(); j++) {
                            if (trackPackComponentsTableModel.getValueAt(j, 0).equals(rowData[0])) { // Assuming productID is at column 0
                                existingRowIndex = j;
                                break;
                            }
                        }
            
                        if (existingRowIndex != -1) {
                            // If the product already exists, increment the quantity
                            int currentQuantity = (int) trackPackComponentsTableModel.getValueAt(existingRowIndex, trackPackComponentsTableModel.getColumnCount() - 1); // Assuming "Quantity" is the last column
                            trackPackComponentsTableModel.setValueAt(currentQuantity + 1, existingRowIndex, trackPackComponentsTableModel.getColumnCount() - 1);
                        } else {
                            // If the product doesn't exist, add a new row with quantity 1
                            Object[] newRowData = Arrays.copyOf(rowData, rowData.length + 1);
                            newRowData[newRowData.length - 1] = 1;
                            trackPackComponentsTableModel.addRow(newRowData);
                        }
                    }
                }
            });
            

            JScrollPane scrollPane2 = new JScrollPane(tackPackComponentsTable);
            trackPackComponentsPanel.add(trackPackComponentsLabel, BorderLayout.NORTH);
            trackPackComponentsPanel.add(scrollPane2, BorderLayout.CENTER);

            tables.add(tracksPanel);
            tables.add(trackPackComponentsPanel);

            add(fieldsPanel, BorderLayout.NORTH);
            add(tables, BorderLayout.CENTER);
            add(addSelectedProduct, BorderLayout.SOUTH);
        }
    }

    public String getProductName() {
        return productNameField.getText();
    }
    
    public String getRetailPrice() {
        return retailPriceField.getText();
    }
    
    public String getQuantityInStock() {
        return quantityInStockField.getText();
    }
    
    public String getManufacturer() {
        return manufacturerField.getText();
    }
    
    public Gauge getGauge() {
        return (Gauge) gaugeComboBox.getSelectedItem();
    }

    public HashMap<Track, Integer> getTrackPackComponents() {
        HashMap<Track, Integer> components = new HashMap<>();
        DefaultTableModel model = (DefaultTableModel) tackPackComponentsTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            int productID = Integer.parseInt((String) model.getValueAt(i, model.findColumn("Product ID")));
            System.out.println("Product ID: " + productID);

            String productName = (String) model.getValueAt(i, model.findColumn("Product Name"));
            System.out.println("Product Name: " + productName);

            BigDecimal retailPrice = (BigDecimal) model.getValueAt(i, model.findColumn("Price (per product)"));
            System.out.println("Retail Price: " + retailPrice);

            int quantityInStock = (Integer) model.getValueAt(i, model.findColumn("Number in Stock"));
            System.out.println("Quantity in Stock: " + quantityInStock);

            String manufacturer = (String) model.getValueAt(i, model.findColumn("Manufacturer"));
            System.out.println("Manufacturer: " + manufacturer);

            Gauge gauge = Gauge.valueOf(((String) model.getValueAt(i, model.findColumn("Gauge"))).replace(" ", "_").toUpperCase());
            System.out.println("Gauge: " + gauge);

            Track track = new Track(productID, productName, retailPrice, quantityInStock, manufacturer, gauge);
            System.out.println("Track: " + track);

            int quantity = (Integer) model.getValueAt(i, model.findColumn("Quantity"));
            System.out.println("Quantity: " + quantity);

            components.put(track, quantity);
        }

        return components;
    }

}
