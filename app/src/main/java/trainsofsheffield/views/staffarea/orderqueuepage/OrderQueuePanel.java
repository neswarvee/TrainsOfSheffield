package trainsofsheffield.views.staffarea.orderqueuepage;
import trainsofsheffield.models.OrderDatabaseOperations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static trainsofsheffield.models.OrderDatabaseOperations.getProductStock;

public class OrderQueuePanel extends JPanel {
    public static JTable orderQueueTable;
    private JButton processOrderButton;
    private JButton viewOrderButton;
    private JButton viewUserButton;

    public OrderQueuePanel() throws Exception {
        // Set layout for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Order Queue"));

        // Fetch and display the table of confirmed orders ranked by date
        orderQueueTable = OrderDatabaseOperations.getConfirmedOrdersTable();
        if (orderQueueTable != null) {
            add(new JScrollPane(orderQueueTable), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
            add(buttonPanel, BorderLayout.SOUTH);

            // Create a button to process the top order
            processOrderButton = new JButton("Process Order");
            processOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        processTopOrder();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            buttonPanel.add(processOrderButton);

            //Create a button to view order details(lines)
            viewOrderButton = new JButton("View Order Details");
            viewOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        viewOrderDetails();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            buttonPanel.add(viewOrderButton);


            //Create a button to view user details
            viewUserButton = new JButton("View User Details");
            //TODO add viewUserButton Listener
            buttonPanel.add(viewUserButton);
            viewUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        viewUserDetails();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } else {
            // Display a message if no confirmed orders are found
            add(new JLabel("No confirmed orders found."), BorderLayout.CENTER);
        }
    }


    // Method to process the top order (either delete or change status to 'Fulfilled')
    private void processTopOrder() throws Exception {
        int selectedRow = orderQueueTable.getSelectedRow();

        if (selectedRow != -1) {
            int orderID = (int) orderQueueTable.getValueAt(selectedRow, 0);
            String status = (String) orderQueueTable.getValueAt(selectedRow, 2);

            if (status.equals("Confirmed")) {
                // Check if any stock level will be reduced to less than 0
                if (!checkStockLevelsBeforeFulfill(orderID)) {
                    return; // Cancel the process if stock levels are insufficient
                }
                //get the popup window to say fulfill or delete instead of yes or no
                Object[] options = {"Fulfill", "Reject"};
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Do you want to fulfill or delete this order?",
                        "Process Order",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == 0) {
                    OrderDatabaseOperations.fulfillOrder(orderID);
                } else if (choice == 1) {
                    OrderDatabaseOperations.deleteOrder(orderID);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selected order is not in 'Confirmed' status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to process the order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Helper method to check if fulfilling the order will reduce any stock level to less than 0
    private boolean checkStockLevelsBeforeFulfill(int orderID) throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String orderLinesQuery = "SELECT * FROM OrderLines WHERE orderID = ?";
            try (PreparedStatement orderLinesStatement = connection.prepareStatement(orderLinesQuery)) {
                orderLinesStatement.setInt(1, orderID);
                ResultSet orderLinesResultSet = orderLinesStatement.executeQuery();

                while (orderLinesResultSet.next()) {
                    int productID = orderLinesResultSet.getInt("productID");
                    int quantityOrdered = orderLinesResultSet.getInt("quantity");

                    // Retrieve the current product stock level
                    int currentStock = getProductStock(connection, productID);

                    // Check if fulfilling the order will reduce the stock level to less than 0
                    if (currentStock - quantityOrdered < 0) {
                        // Ask the staff member if they want to continue
                        int confirmChoice = JOptionPane.showConfirmDialog(
                                this,
                                "Fulfilling this order will reduce the stock level of a product to less than 0. Do you want to continue?",
                                "Insufficient Stock",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        return confirmChoice == JOptionPane.YES_OPTION;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return true; // Continue the process if stock levels are sufficient
    }
    //method to view the user details when the button is pressed
    private void viewUserDetails() throws SQLException {
        int selectedRow = orderQueueTable.getSelectedRow();

        if (selectedRow != -1) {
            int orderID = (int) orderQueueTable.getValueAt(selectedRow, 0);
            String userID = OrderDatabaseOperations.getUserIDForOrder(orderID);

            if (userID != null) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
                    // Query to get user details including address information using LEFT JOIN to get the addresses from their own tables
                    String userQuery = "SELECT Users.*, Streets.street, Cities.city, Postcodes.postcode " +
                            "FROM Users " +
                            "LEFT JOIN Addresses ON Users.addressID = Addresses.addressID " +
                            "LEFT JOIN Streets ON Addresses.streetID = Streets.streetID " +
                            "LEFT JOIN Cities ON Addresses.cityID = Cities.cityID " +
                            "LEFT JOIN Postcodes ON Addresses.postcodeID = Postcodes.postcodeID " +
                            "WHERE Users.userID = ?";

                    try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                        userStatement.setString(1, userID);
                        ResultSet userResultSet = userStatement.executeQuery();

                        // Check if the result set has any rows
                        if (userResultSet.next()) {
                            // Retrieve user details from the result set
                            String email = userResultSet.getString("email");
                            String firstName = userResultSet.getString("firstName");
                            String surname = userResultSet.getString("surname");
                            String role = userResultSet.getString("role");

                            // Retrieve address information
                            String street = userResultSet.getString("street");
                            String city = userResultSet.getString("city");
                            String postcode = userResultSet.getString("postcode");

                            // Display user details and address
                            String userDetails =
                                    "Email: " + email + "\n" +
                                    "First Name: " + firstName + "\n" +
                                    "Surname: " + surname + "\n" +
                                    "Role: " + role + "\n" +
                                            "Address:\n" +
                                    "Street: " + street + "\n" +
                                    "City: " + city + "\n" +
                                    "Postcode: " + postcode;

                            JOptionPane.showMessageDialog(this, userDetails, "User Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            } else {
                JOptionPane.showMessageDialog(this, "User ID not available for the selected order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to view user details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Method to show order details in a dialog (basically the same as the one in StaffOrderHistoryPanel)
    private void showOrderDetailsDialog(int orderID) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "SELECT * FROM OrderLines WHERE orderID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, orderID);
                ResultSet resultSet = statement.executeQuery();

                // Create a DefaultTableModel to store order lines
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("OrderLineID");
                model.addColumn("Product Name");
                model.addColumn("Quantity");
                model.addColumn("ProductID");
                model.addColumn("OrderID");

                // Populate the table with order lines
                while (resultSet.next()) {
                    int orderLineID = resultSet.getInt("orderLineID");
                    int quantity = resultSet.getInt("quantity");
                    int productID = resultSet.getInt("productID");
                    int orderIDValue = resultSet.getInt("orderID");
                    // Use the getProductName method to fetch the product name
                    String productName = OrderDatabaseOperations.getProductName(connection, productID);

                    model.addRow(new Object[]{orderLineID, productName, quantity, productID, orderIDValue});
                }

                // Create and show a dialog to display order lines
                JTable orderLinesTable = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(orderLinesTable);
                JOptionPane.showMessageDialog(this, scrollPane, "Order Lines for Order ID: " + orderID, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Action listener for the viewOrderButton
    private void viewOrderDetails() throws SQLException {
        int selectedRow = orderQueueTable.getSelectedRow();

        if (selectedRow != -1) {
            int orderID = (int) orderQueueTable.getValueAt(selectedRow, 0);
            showOrderDetailsDialog(orderID);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to view order details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
