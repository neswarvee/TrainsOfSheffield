package trainsofsheffield.views.staffarea.orderhistorypage;

import trainsofsheffield.models.OrderDatabaseOperations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffOrderHistoryPanel extends JPanel {
    private JTable orderHistoryTable;
    private JButton viewOrderButton;
    public StaffOrderHistoryPanel() throws SQLException{
        // Set layout for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("All Order History"));

        // Fetch and display the table of fulfilled orders
        orderHistoryTable = OrderDatabaseOperations.getFulfilledOrdersTable();
        if (orderHistoryTable != null) {
            add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);

            // Create a button to view the order details
            viewOrderButton = new JButton("View Order");
            viewOrderButton.addActionListener(e -> {
                try {
                    viewOrderDetails();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(viewOrderButton, BorderLayout.SOUTH);

        } else {
            // Display a message if no fulfilled orders are found
            add(new JLabel("No fulfilled orders found."), BorderLayout.CENTER);
        }
    }
    // Method to handle viewing order details
    private void viewOrderDetails() throws SQLException{
        int selectedRow = orderHistoryTable.getSelectedRow();

        if (selectedRow != -1) {
            int orderID = (int) orderHistoryTable.getValueAt(selectedRow, 0);
            showOrderDetailsDialog(orderID);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to view order details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to show order details in a dialog
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

                    model.addRow(new Object[]{orderLineID,productName, quantity, productID, orderIDValue});
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
}
