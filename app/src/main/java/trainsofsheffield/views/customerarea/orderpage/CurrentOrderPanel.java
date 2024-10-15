package trainsofsheffield.views.customerarea.orderpage;

import trainsofsheffield.models.OrderDatabaseOperations;
import trainsofsheffield.models.Session;
import trainsofsheffield.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class CurrentOrderPanel extends JPanel {
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;

    private String userID;
    private JTable orderLinesTable;

    //using methods from the controller
    OrderDatabaseOperations orderDatabaseOperations = new OrderDatabaseOperations();

    public CurrentOrderPanel(Session session) throws SQLException {
        //gets the user from the session
        User user = session.getUser();
        this.userID = user.getUserID();
        //sets the border for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Current Order"));

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        add(new JScrollPane(orderList), BorderLayout.CENTER);

        // Create a button for deleting order line
        JButton deleteOrderLineButton = new JButton("Delete Order Line");
        deleteOrderLineButton.addActionListener(e -> {
            try {
                deleteOrderLine();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(deleteOrderLineButton, BorderLayout.WEST);

        JButton editQuantityButton = new JButton("Edit Quantity");
        editQuantityButton.addActionListener(e -> {
            try {
                editQuantity();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(editQuantityButton, BorderLayout.EAST);

        // Fetches and displays the table of order lines for the given user ID
        orderLinesTable = orderDatabaseOperations.filterOrdersByUserID(userID);
        if (orderLinesTable != null) {
            add(new JScrollPane(orderLinesTable), BorderLayout.CENTER);


            // Create a button to confirm the order
            JButton confirmOrderButton = new JButton("Confirm Order");
            confirmOrderButton.addActionListener(e -> {
                try {
                    confirmOrder();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(confirmOrderButton, BorderLayout.SOUTH);
        } else {
            // Display a message if no current order is found
            add(new JLabel("No current order found."), BorderLayout.CENTER);
        }
    }


    // Method to handle order line deletion
    private void deleteOrderLine() throws SQLException {
        int selectedRow = orderLinesTable.getSelectedRow();

        if (selectedRow != -1) {
            int orderLineID = (int) orderLinesTable.getValueAt(selectedRow, 0);

            // Remove the selected row from the JTable
            DefaultTableModel model = (DefaultTableModel) orderLinesTable.getModel();
            model.removeRow(selectedRow);

            // Remove the corresponding order line from the database
            orderDatabaseOperations.deleteOrderLineFromDatabase(orderLineID);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        // Method to handle quantity editing
        private void editQuantity() throws SQLException {
            int selectedRow = orderLinesTable.getSelectedRow();

            if (selectedRow != -1) {
                int orderLineID = (int) orderLinesTable.getValueAt(selectedRow, 0);
                int productID = (int) orderLinesTable.getValueAt(selectedRow, 3); // Assuming productID is at index 3

                // Prompt user for the new quantity
                String quantityString = JOptionPane.showInputDialog(this, "Enter new quantity:");

                // Check if the user canceled the input or entered an invalid quantity
                if (quantityString != null && !quantityString.isEmpty()) {
                    try {
                        int newQuantity = Integer.parseInt(quantityString);

                        // Check if the new quantity doesn't exceed the available stock
                        if (OrderDatabaseOperations.checkQuantityInStock(productID, newQuantity)) {
                            // Update the JTable with the new quantity
                            orderLinesTable.getModel().setValueAt(newQuantity, selectedRow, 2); // Assuming quantity is at index 2

                            // Update the corresponding field in the database
                            OrderDatabaseOperations.updateQuantityInDatabase(newQuantity, orderLineID);
                        } else {
                            JOptionPane.showMessageDialog(this, "Quantity exceeds available stock.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    // Method to handle confirming the order
    private void confirmOrder() throws SQLException {
        // Check if the user has bank details
        if (orderDatabaseOperations.userHasBankDetails(userID)) {
            // Update the order status to 'Confirmed' in the database
            orderDatabaseOperations.updateOrderStatus(userID);
            // Display a confirmation message
            JOptionPane.showMessageDialog(this, "Order confirmed and removed from current order, please refresh the page by exiting" +
                    " and reentering.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optionally: Update the UI or perform additional actions
        } else {
            // Display a message indicating that bank details are required
            JOptionPane.showMessageDialog(this, "Bank details are required to confirm the order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

