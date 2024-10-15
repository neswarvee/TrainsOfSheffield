package trainsofsheffield.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.HashMap;

import static trainsofsheffield.views.staffarea.orderqueuepage.OrderQueuePanel.orderQueueTable;

public class OrderDatabaseOperations {
    // Method to update the quantity in the database
    public static void updateQuantityInDatabase(int newQuantity, int orderLineID) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String updateQuery = "UPDATE OrderLines SET quantity = ? WHERE orderLineID = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, newQuantity);
                updateStatement.setInt(2, orderLineID);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //returns table of order by filtering by USERID
    public JTable filterOrdersByUserID(String userID) throws SQLException{
        JTable table = null;
        //establishes connection to the database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010",
                "team010", "AhKeh5IeV")) {
            // Creates a PreparedStatement for the first query
            String orderQuery = "SELECT * FROM Orders WHERE userID = ? AND status = 'Pending'";
            PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
            orderStatement.setString(1, userID);

            // Executes the first query
            ResultSet orderResultSet = orderStatement.executeQuery();

            // Checks if the result set has any rows
            if (orderResultSet.next()) {
                // Retrieves the orderID from the result set
                int orderID = orderResultSet.getInt("orderID");

                // Creates a PreparedStatement for the second query
                String lineQuery = "SELECT * FROM OrderLines WHERE orderID = ?";
                PreparedStatement lineStatement = connection.prepareStatement(lineQuery);
                lineStatement.setInt(1, orderID);

                // Executes the second query
                ResultSet lineResultSet = lineStatement.executeQuery();

                // Creates a DefaultTableModel to store order lines
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("OrderLine ID");
                model.addColumn("Product Name");
                model.addColumn("Quantity");
                model.addColumn("ProductID");
                model.addColumn("Retail Price");
                model.addColumn("Total Price"); // New column for total price

                // Populates the table with all the user's order lines for the current order
                while (lineResultSet.next()) {
                    int orderLineID = lineResultSet.getInt("orderLineID");
                    int quantity = lineResultSet.getInt("quantity");
                    int productID = lineResultSet.getInt("productID");


                    // Fetch the product name and retailPrice using the productID
                    String productName = getProductName(connection, productID);
                    double retailPrice = getRetailPrice(connection, productID);

                    // Calculate the total price by multiplying the retail price by the quantity
                    double totalPrice = retailPrice * quantity;
                    /** Use BigDecimal for precise decimal arithmetic and rounding to two decimal places because we're
                     dealing with money*/
                    BigDecimal totalPriceDecimal = new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP);

                    model.addRow(new Object[]{orderLineID, productName, quantity, productID, retailPrice,
                            totalPrice});
                }

                // Closes the second result set and statement
                lineResultSet.close();
                lineStatement.close();

                // Creates the JTable with the model
                table = new JTable(model);
            } else {
                System.out.println("No pending orders found for userID: " + userID);
            }

            // Closes the first result set and statement
            orderResultSet.close();
            orderStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return table;
    }

    // Helper method to get the product name using the productID
    public static String getProductName(Connection connection, int productID) throws SQLException {
        String productName = "";

        // Create a PreparedStatement for the query
        String productQuery = "SELECT productName FROM Products WHERE productID = ?";
        try (PreparedStatement productStatement = connection.prepareStatement(productQuery)) {
            productStatement.setInt(1, productID);

            // Execute the query
            ResultSet productResultSet = productStatement.executeQuery();

            // Check if the result set has any rows
            if (productResultSet.next()) {
                // Retrieve the product name from the result set
                productName = productResultSet.getString("productName");
            }
        }

        return productName;
    }
    // Helper method to get the retail price using the productID
    private double getRetailPrice(Connection connection, int productID) throws SQLException {
        double retailPrice = 0.0;

        // Create a PreparedStatement for the query
        String retailPriceQuery = "SELECT retailPrice FROM Products WHERE productID = ?";
        try (PreparedStatement priceStatement = connection.prepareStatement(retailPriceQuery)) {
            priceStatement.setInt(1, productID);

            // Execute the query
            ResultSet priceResultSet = priceStatement.executeQuery();

            // Check if the result set has any rows
            if (priceResultSet.next()) {
                // Retrieve the retail price from the result set
                retailPrice = priceResultSet.getDouble("retailPrice");
            }
        }

        return retailPrice;
    }
    // Method to delete order line from the database
    public void deleteOrderLineFromDatabase(int orderLineID) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String deleteQuery = "DELETE FROM OrderLines WHERE orderLineID = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, orderLineID);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Method to check if the new quantity exceeds available stock
    public static boolean checkQuantityInStock(int productID, int newQuantity) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String stockQuery = "SELECT quantityInStock FROM Products WHERE productID = ?";
            try (PreparedStatement stockStatement = connection.prepareStatement(stockQuery)) {
                stockStatement.setInt(1, productID);
                ResultSet stockResultSet = stockStatement.executeQuery();

                if (stockResultSet.next()) {
                    int quantityInStock = stockResultSet.getInt("quantityInStock");
                    return newQuantity <= quantityInStock;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return false; // Return false by default if an error occurs
    }
    // Method to fetch the table of order history for the given user ID
    public static JTable getOrderHistoryTable(String userID) throws SQLException {
        JTable table = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "SELECT * FROM Orders WHERE userID = ? AND status <> 'Pending'";//returns orders that aren't pending
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userID);
                ResultSet resultSet = statement.executeQuery();

                // Create a DefaultTableModel to store order history
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("OrderID");
                model.addColumn("Order Date");
                model.addColumn("Status");
                model.addColumn("Total Cost");

                // Populate the table with order history
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderID");
                    Date orderDate = resultSet.getDate("order_date");
                    String status = resultSet.getString("status");
                    BigDecimal totalCost = resultSet.getBigDecimal("totalCost");
                    model.addRow(new Object[]{orderID,orderDate,status,totalCost});
                }

                // Create the JTable with the model
                table = new JTable(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return table;
    }
    // Method to check if the user has bank details
    public boolean userHasBankDetails(String userID) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "SELECT * FROM BankDetails WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userID);
                ResultSet resultSet = statement.executeQuery();

                return resultSet.next(); // Returns true if there are bank details for the user
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Method to update the order status to 'Confirmed' in the database
    public void updateOrderStatus(String userID) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String updateQuery = "UPDATE Orders SET status = 'Confirmed' WHERE userID = ? AND status = 'Pending'";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, userID);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to fetch the table of fulfilled orders
    public static JTable getFulfilledOrdersTable() throws SQLException {
        JTable table = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "SELECT * FROM Orders WHERE status = 'Fulfilled'";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                // Create a DefaultTableModel to store fulfilled orders
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("OrderID");
                model.addColumn("Order Date");
                model.addColumn("Status");
                model.addColumn("Total Cost");
                model.addColumn("UserID");

                // Populate the table with fulfilled orders
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderID");
                    Date orderDate = resultSet.getDate("order_date");
                    String status = resultSet.getString("status");
                    BigDecimal totalCost = resultSet.getBigDecimal("totalCost");
                    String userID = resultSet.getString("userID");

                    model.addRow(new Object[]{orderID, orderDate, status, totalCost, userID});
                }

                // Create the JTable with the model
                table = new JTable(model);
            }
        }

        return table;
    }

    public boolean userHasCurrentOrder(String userID, Connection connection) throws SQLException {
        try {
            String selectSQL = ("SELECT orderID FROM Orders WHERE userID = ? AND status = 'Pending'");

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {return true;}
            else {return false;}

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void addOrderLine(int productID, int quantity, int orderID, Connection connection) throws SQLException{
        if (quantity > 0) {
            try {
                String insertSQL = (
                        "INSERT INTO OrderLines (quantity, productID, orderID) VALUES (?, ?, ?)"
                );

                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, Integer.toString(quantity));
                preparedStatement.setString(2, Integer.toString(productID));
                preparedStatement.setString(3, Integer.toString(orderID));

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void addCurrentOrder(Connection connection, String userID, HashMap<Integer, Integer> productIDAndQuantityMap) throws SQLException {

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Adding the new order now in Orders table
        String currentDate = java.time.LocalDate.now().toString();

        int count = 0;
        for (Integer i : productIDAndQuantityMap.values()) {
            count += i;
        }
        BigDecimal totalCostOfOrder = BigDecimal.valueOf(count);

        try {
            String insertSQL = (
                    "INSERT INTO Orders (order_date, status, totalCost, userID) VALUES " +
                    "(DATE ?, 'Pending', ?, ?)"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, currentDate);
            preparedStatement.setString(2, totalCostOfOrder.toString());
            preparedStatement.setString(3, userID);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /*
        get orderID max value, which is the most recently added orders orderID, this is because
        this column is a primary key and auto increments in the database when adding new rows to it.
        All of this means that this orderID, is the orderID of the order that was just added in the code above
         */
        int orderID;
        String selectQueryMaxOrderID = "SELECT MAX(orderID) FROM Orders";
        Statement statementMaxOrderID = connection.createStatement();
        ResultSet resultSetMaxOrderID = statementMaxOrderID.executeQuery(selectQueryMaxOrderID);

        if (resultSetMaxOrderID.next()) {
            orderID = resultSetMaxOrderID.getInt(1);
        } else {
            throw new SQLException("Can't get max value of OrderID column in Orders table");
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Adding each order line and connecting it to the order that was just added
        for (int pID : productIDAndQuantityMap.keySet()) {
            int quantity = productIDAndQuantityMap.get(pID);

            addOrderLine(pID, quantity, orderID, connection);

        }

    }

    public ResultSet getOrderLinesOfCurrentOrder(Connection connection, String userID) throws SQLException {
        try {
            String selectSQL = (
                "SELECT l.orderLineID, l.quantity, l.productID, l.orderID " +
                "FROM Orders o, OrderLines l " +
                "WHERE o.userID = ? " +
                "AND o.status = 'Pending' " +
                "AND l.orderID = o.orderID"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


    public boolean isProductIDInCurrentOrderLinesForUser(Connection connection, String userID, int productID) throws SQLException {
        try {
            String selectSQL = (
              "SELECT l.quantity " +
              "FROM Orders o, OrderLines l " +
              "WHERE o.userID = ? " +
              "AND o.status = 'Pending' " +
              "AND o.orderID = l.orderID " +
              "AND l.productID = ?"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {return true;}
            else {return false;}

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int getAlreadySelectedQuantityForProductID(Connection connection, String userID, int productID) throws Exception {

        try {
            String selectSQL = (
                    "SELECT l.quantity " +
                    "FROM Orders o, OrderLines l " +
                    "WHERE o.userID = ? " +
                    "AND o.status = 'Pending' " +
                    "AND o.orderID = l.orderID " +
                    "AND l.productID = ?"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new IllegalArgumentException("There is no order line which is in this users current order with the provided productID");
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Method to fetch the table of confirmed orders ranked by date
    public static JTable getConfirmedOrdersTable() throws Exception {
        JTable table = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "SELECT * FROM Orders WHERE status = 'Confirmed' ORDER BY order_date";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                // Create a DefaultTableModel to store confirmed orders
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("OrderID");
                model.addColumn("Order Date");
                model.addColumn("Status");
                model.addColumn("Total Cost");

                // Populate the table with confirmed orders
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderID");
                    Date orderDate = resultSet.getDate("order_date");
                    String status = resultSet.getString("status");
                    BigDecimal totalCost = resultSet.getBigDecimal("totalCost");
                    //String userID = resultSet.getString("userID");

                    model.addRow(new Object[]{orderID, orderDate, status, totalCost});
                }

                // Create the JTable with the model
                table = new JTable(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return table;
    }
    // Method to change the order status to 'Fulfilled'
    public static void fulfillOrder(int orderID) throws Exception{
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Retrieve order lines for the given orderID
            String orderLinesQuery = "SELECT * FROM OrderLines WHERE orderID = ?";
            try (PreparedStatement orderLinesStatement = connection.prepareStatement(orderLinesQuery)) {
                orderLinesStatement.setInt(1, orderID);
                ResultSet orderLinesResultSet = orderLinesStatement.executeQuery();

                // Update product stock levels based on order lines
                while (orderLinesResultSet.next()) {
                    int productID = orderLinesResultSet.getInt("productID");
                    int quantityOrdered = orderLinesResultSet.getInt("quantity");

                    // Update the product stock level
                    updateProductStock(connection, productID, quantityOrdered);
                }
            }

            // Update the order status to 'Fulfilled'
            String updateOrderQuery = "UPDATE Orders SET status = 'Fulfilled' WHERE orderID = ?";
            try (PreparedStatement updateOrderStatement = connection.prepareStatement(updateOrderQuery)) {
                updateOrderStatement.setInt(1, orderID);
                updateOrderStatement.executeUpdate();
            }

            // Refresh the table after processing the order
            orderQueueTable.setModel(getConfirmedOrdersTable().getModel());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void updateProductStock(Connection connection, int productID, int quantityOrdered) throws SQLException {
        // Retrieve the current product stock level
        String getProductQuery = "SELECT quantityInStock FROM Products WHERE productID = ?";
        try (PreparedStatement getProductStatement = connection.prepareStatement(getProductQuery)) {
            getProductStatement.setInt(1, productID);
            ResultSet productResultSet = getProductStatement.executeQuery();

            if (productResultSet.next()) {
                int currentStock = productResultSet.getInt("quantityInStock");

                // Calculate the new stock level after fulfilling the order
                int newStock = currentStock - quantityOrdered;

                // Update the product stock level
                String updateProductQuery = "UPDATE Products SET quantityInStock = ? WHERE productID = ?";
                try (PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuery)) {
                    updateProductStatement.setInt(1, newStock);
                    updateProductStatement.setInt(2, productID);
                    updateProductStatement.executeUpdate();
                }
            }
        }
    }
    // Method to delete the order
    public static void deleteOrder(int orderID) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String deleteQuery = "DELETE FROM Orders WHERE orderID = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, orderID);
                deleteStatement.executeUpdate();
            }

            // Refresh the table after deleting the order
            orderQueueTable.setModel(getConfirmedOrdersTable().getModel());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public int getOrderIDOfCurrentOrder (Connection connection, String userID) throws Exception {

        try {
            String selectSQL = (
              "SELECT orderID " +
              "FROM Orders " +
              "WHERE userID = ? " +
              "AND status = 'Pending'"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new IllegalArgumentException("User has no current order");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException x) {
            x.printStackTrace();
            throw x;
        }
    }

    public int getOrderLineIDFromCurrentOrderForProductID (Connection connection, int productID, String userID) throws Exception {

        //get the orderLineID for the order line which is in current order for the user and matches the productID argument
        try {
            String selectSQL = (
              "SELECT l.orderLineID " +
              "FROM OrderLines l, Orders o " +
              "WHERE o.userID = ? " +
              "AND o.status = 'Pending' " +
              "AND o.orderID = l.orderID " +
              "AND l.productID = ?"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new IllegalArgumentException("The productID doesn't match an order line in the current order of the user");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Helper method to retrieve the current product stock level
    public static int getProductStock(Connection connection, int productID) {
        int currentStock = 0;

        String getProductQuery = "SELECT quantityInStock FROM Products WHERE productID = ?";
        try (PreparedStatement getProductStatement = connection.prepareStatement(getProductQuery)) {
            getProductStatement.setInt(1, productID);
            ResultSet productResultSet = getProductStatement.executeQuery();

            if (productResultSet.next()) {
                currentStock = productResultSet.getInt("quantityInStock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return currentStock;
    }
    public static String getUserIDForOrder(int orderID) {
        String userID = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String orderQuery = "SELECT userID FROM Orders WHERE orderID = ?";
            try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery)) {
                orderStatement.setInt(1, orderID);
                ResultSet orderResultSet = orderStatement.executeQuery();

                if (orderResultSet.next()) {
                    userID = orderResultSet.getString("userID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return userID;
    }
}

