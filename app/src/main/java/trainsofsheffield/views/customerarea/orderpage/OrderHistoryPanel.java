package trainsofsheffield.views.customerarea.orderpage;
import trainsofsheffield.models.OrderDatabaseOperations;
import trainsofsheffield.models.Session;
import trainsofsheffield.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class OrderHistoryPanel extends JPanel {
    private String userID;
    private JTable orderHistoryTable;

    public OrderHistoryPanel(Session session) throws SQLException {
        //gets the user from the session
        User user = session.getUser();
        this.userID = user.getUserID();

        // Set layout for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Order History"));

        // Fetch and display the table of order history for the given user ID
        orderHistoryTable = OrderDatabaseOperations.getOrderHistoryTable(userID);
        if (orderHistoryTable != null) {
            add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);
        } else {
            // Display a message if no order history is found
            add(new JLabel("No order history found."), BorderLayout.CENTER);
        }
    }


}