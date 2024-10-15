package trainsofsheffield.models;

import trainsofsheffield.models.Product.ProductCategory;

import java.sql.*;
import java.util.*;

public class ProductDatabaseOperations {
    final static DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
    public ResultSet getAllProductsOfCategory(ProductCategory pCategory, Connection connection) throws SQLException {
        try {
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            String selectSQL = (
                    "SELECT p.productID, p.productName, p.retailPrice, p.quantityInStock, p.gauge, m.manufacturerName"
            );
            /*
            The below nested if statement alters the SELECT part of the SQL statement to get the correct
            columns depending on the product category
             */
            if (pCategory == ProductCategory.LOCOMOTIVES ||
                    pCategory == ProductCategory.ROLLING_STOCKS ||
                    pCategory == ProductCategory.TRAIN_SETS) {

                selectSQL += ", eraCode";
                if (pCategory == ProductCategory.LOCOMOTIVES) {
                    selectSQL += ", x.DCCode";
                } else if (pCategory == ProductCategory.ROLLING_STOCKS) {
                    selectSQL += ", x.BRStandardMark, x.companyMark";
                }
            } else if (pCategory == ProductCategory.CONTROLLERS) {
                selectSQL += ", x.controllerType, x.isDigital";
            }
            /*
            Below I haven't done a prepared statement with the product category, because I found out
            online that you can't set the table name using a prepared statement, and it can only be used
            for column values. So concatenating the product category string is the only way that I could
            think of doing it.
             */

            selectSQL += (" FROM " + pCategory.toString() + " x, Products p, Manufacturers m");

            /*
            Also queries the HistoricalEras table, if it needs to be accessed for the specific
            product type
             */
            if (pCategory == ProductCategory.LOCOMOTIVES ||
                    pCategory == ProductCategory.ROLLING_STOCKS ||
                    pCategory == ProductCategory.TRAIN_SETS) {

                selectSQL += ", HistoricalEras e";
            }

            //The "WHERE" part of the statement below, which matches tables together
            selectSQL += (
                    " WHERE p.productID = x.productID " +
                            "AND p.manufacturerID = m.manufacturerID"
            );
            //Below it matches the product eraID to the HistoricalEras table eraID
            if (pCategory == ProductCategory.LOCOMOTIVES ||
                    pCategory == ProductCategory.ROLLING_STOCKS ||
                    pCategory == ProductCategory.TRAIN_SETS) {

                selectSQL += " AND x.eraID = e.eraID";
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //Returns the result set that is returned by querying the database with the created SQL statement

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectSQL);
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int getStockFromProductID(Connection connection, int productID) throws SQLException {
        try {
            String selectSQL = "SELECT quantityInStock FROM Products WHERE productID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("quantityInStock");
            } else {
                throw new IllegalArgumentException("Invalid productID provided in method arguments");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getProductCodeFromProductID(Connection connection, int productID) throws Exception {
        try {
            String selectSQL = (
                    "SELECT c.codeType, c.codeValue " +
                            "FROM ProductCodes c, Products p " +
                            "WHERE p.productID = ? " +
                            "AND p.codeID = c.codeID"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                return (
                        resultSet.getString("codeType") +
                                resultSet.getString("codeValue")
                );

            } else {
                throw new IllegalArgumentException("Invalid productID provided as argument");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateStock(int productID, int newStock) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String updateStock = "UPDATE Products SET quantityInStock = ? WHERE productID = ?;";
            PreparedStatement pstmtAddress = connection.prepareStatement(updateStock);
            pstmtAddress.setInt(1, newStock);
            pstmtAddress.setInt(2, productID);
            int updated = pstmtAddress.executeUpdate();
            if (updated > 0) {
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("Failed to update stock.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productID) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String updateStock = "DELETE FROM Products WHERE productID = ?;";
            PreparedStatement pstmtAddress = connection.prepareStatement(updateStock);
            pstmtAddress.setInt(1, productID);
            int updated = pstmtAddress.executeUpdate();
            if (updated > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewProduct (Product product) throws Exception {

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /*
        Add the product into the product categories list,
        and also get the productID of the inserted product to be used for inserting the product into a
        specific category product table
         */

        String productName = product.getProductName();
        String retailPrice = product.getRetailPrice().toString();
        String quantityInStock = Integer.toString(product.getQuantityInStock());
        String manufacturer = product.getManufacturer();
        String gauge = product.getGauge().toString();

        int productID = insertIntoProductsTable(
                productName, retailPrice, quantityInStock,
                manufacturer, gauge, product.getProductCategory()
        );

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        ArrayList<Object> listOfColumnValues = new ArrayList<>();

        if (product instanceof Controller) {
            String controllerType = ((Controller) product).getControllerType().toString();
            listOfColumnValues.add(controllerType);

            Boolean isDigital = ((Controller) product).getDigital();
            listOfColumnValues.add(isDigital);

        }
        else if (product instanceof Locomotive) {
            String dccCode = ((Locomotive) product).getDccCode().toString();
            listOfColumnValues.add(dccCode);
        }
        else if (product instanceof RollingStock) {
            String brStandardMark = ((RollingStock) product).getBrStandardMark().toString();
            listOfColumnValues.add(brStandardMark);

            String companyMark = ((RollingStock) product).getCompanyMark().toString();
            listOfColumnValues.add(companyMark);

            String rollingStockType = ((RollingStock) product).getRollingStockType().toString();
            listOfColumnValues.add(rollingStockType);
        }

        insertIntoSpecificProductTable(product, listOfColumnValues, productID);


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        if (product instanceof TrackPack) {
            HashMap<Track, Integer> trackAndQuantityMap = ((TrackPack) product).getMapOfTracks();
            int trackPackID = getTrackPackIDOfLastInsert();

            insertTrackPackComponents(trackAndQuantityMap, trackPackID);
        }
        else if (product instanceof TrainSet) {
            HashMap<Locomotive, Integer> mapOfLocomotivesAndQuantities = ((TrainSet) product).getMapOfLocomotives();
            HashMap<RollingStock, Integer> mapOfRollingStocksAndQuantities = ((TrainSet) product).getMapOfRollingStock();
            HashMap<TrackPack, Integer> mapOfTrackPacksAndQuantities = ((TrainSet) product).getMapOfTrackPacks();
            Controller trainSetController = ((TrainSet) product).getController();
            int trainSetID = getTrainSetIDOfLastInsert();

            insertTrainSetComponents(mapOfLocomotivesAndQuantities, mapOfRollingStocksAndQuantities,
                                    mapOfTrackPacksAndQuantities, trainSetController, trainSetID);
        }

    }

    public int getTrainSetIDOfLastInsert() throws SQLException {

        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT MAX(trainSetID) FROM TrainSets";

            Statement statement = databaseConnectionHandler.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Can't fetch max trainSetID from TrainSets table");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public int getTrackPackIDOfLastInsert() throws SQLException{

        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT MAX(trackPackID) FROM TrackPacks";

            Statement statement = databaseConnectionHandler.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Can't fetch max trackPackID from TrackPacks table");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void insertTrainSetComponents(HashMap<Locomotive, Integer> locomotiveAndQuantityMap,
                                         HashMap<RollingStock, Integer> rollingStockAndQuantityMap,
                                         HashMap<TrackPack, Integer> trackPackAndQuantityMap,
                                         Controller controller, int trainSetID) throws Exception{

        try {
            databaseConnectionHandler.openConnection();

            if (locomotiveAndQuantityMap == null || locomotiveAndQuantityMap.isEmpty() ||
                rollingStockAndQuantityMap == null || rollingStockAndQuantityMap.isEmpty() ||
                trackPackAndQuantityMap == null || trackPackAndQuantityMap.isEmpty() ||
                controller == null) {
                throw new IllegalArgumentException("One or more hashmaps, or the controller is null or empty.");
            }

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //inserting the locomotives

            Iterator<Map.Entry<Locomotive, Integer>> locomotiveHashMapIterator = locomotiveAndQuantityMap.entrySet().iterator();

            while (locomotiveHashMapIterator.hasNext()) {
                Map.Entry<Locomotive, Integer> entry = locomotiveHashMapIterator.next();
                Locomotive locomotive = entry.getKey();
                Integer productID = locomotive.getProductID();
                Integer quantity = entry.getValue();

                String insertSQL = "INSERT INTO TrainSetComponents (trainSetID, productID, quantity) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
                preparedStatement.setString(1, Integer.toString(trainSetID));
                preparedStatement.setString(2, productID.toString());
                preparedStatement.setString(3, quantity.toString());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + "row added");
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //inserting the rolling stocks

            Iterator<Map.Entry<RollingStock, Integer>> rollingStockHashMapIterator = rollingStockAndQuantityMap.entrySet().iterator();

            while (rollingStockHashMapIterator.hasNext()) {
                Map.Entry<RollingStock, Integer> entry = rollingStockHashMapIterator.next();
                RollingStock rollingStock = entry.getKey();
                Integer productID = rollingStock.getProductID();
                Integer quantity = entry.getValue();

                String insertSQL = "INSERT INTO TrainSetComponents (trainSetID, productID, quantity) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
                preparedStatement.setString(1, Integer.toString(trainSetID));
                preparedStatement.setString(2, productID.toString());
                preparedStatement.setString(3, quantity.toString());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row added into TrainSetComponents");
            }

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //inserting the track packs

            Iterator<Map.Entry<TrackPack, Integer>> trackPackHashMapIterator = trackPackAndQuantityMap.entrySet().iterator();

            while (trackPackHashMapIterator.hasNext()) {
                Map.Entry<TrackPack, Integer> entry = trackPackHashMapIterator.next();
                TrackPack trackPack = entry.getKey();
                Integer productID = trackPack.getProductID();
                Integer quantity = entry.getValue();

                String insertSQL = "INSERT INTO TrainSetComponents (trainSetID, productID, quantity) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
                preparedStatement.setString(1, Integer.toString(trainSetID));
                preparedStatement.setString(2, productID.toString());
                preparedStatement.setString(3, quantity.toString());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row added into TrainSetComponents");
            }

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //inserting the controller

            Integer productID = controller.getProductID();

            String insertSQL = "INSERT INTO TrainSetComponents (trainSetID, productID, quantity) VALUES (?, ?, 1)";

            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
            preparedStatement.setString(1, Integer.toString(trainSetID));
            preparedStatement.setString(2, productID.toString());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row added into TrainSetComponents");



        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void insertTrackPackComponents(HashMap<Track, Integer> trackAndQuantityMap, int trackPackID) throws Exception {

        try {
            databaseConnectionHandler.openConnection();

            if (trackAndQuantityMap.isEmpty() || trackAndQuantityMap == null) {
                throw new IllegalArgumentException("Track Pack Component hashmap cannot be empty or null");
            }

            Iterator<Map.Entry<Track, Integer>> hashMapIterator = trackAndQuantityMap.entrySet().iterator();

            while (hashMapIterator.hasNext()) {
                Map.Entry<Track, Integer> entry = hashMapIterator.next();
                Track track = entry.getKey();
                int productID = track.getProductID();
                System.out.println("ProductID: " + productID);
                Integer trackID = getTrackIDFromProductID(productID);
                System.out.println("TrackID: "+ trackID);
                Integer quantity = entry.getValue();
                System.out.println("Quantity: " + quantity);

                String insertSQL = "INSERT INTO TrackPackComponents (trackPackID, trackID, quantity) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
                preparedStatement.setString(1, Integer.toString(trackPackID));
                preparedStatement.setString(2, trackID.toString());
                preparedStatement.setString(3, quantity.toString());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + "row added into TrackPackComponents");

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public int getTrackIDFromProductID(int productID) throws Exception {

        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT trackID FROM Tracks WHERE productID = ?";
            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(selectSQL);
            preparedStatement.setString(1, Integer.toString(productID));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                System.out.println("ProductID you attempted to query: " + productID);
                throw new IllegalArgumentException("This productID does not exist in the table");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void insertIntoSpecificProductTable(Product product, ArrayList<Object> listOfColumnValues, int productID) throws Exception {
        ProductCategory productCategory = product.getProductCategory();

        //Inserts the product into the correct table
        String insertSQL = "INSERT INTO ";
        if (product instanceof Controller) {
            insertSQL += "Controllers (controllerType, isDigital, productID) VALUES (?, ?, ?)";
        } else if (product instanceof Locomotive) {
            insertSQL += "Locomotives (DCCode, eraID, productID) VALUES (?, ?, ?)";
        } else if (product instanceof RollingStock) {
            insertSQL += "RollingStocks (BRStandardMark, companyMark, rollingStockType, eraID, productID) VALUES (?, ?, ?, ?, ?)";
        } else if (product instanceof Track) {
            insertSQL += "Tracks (productID) VALUES (?)";
        } else if (product instanceof TrackPack) {
            insertSQL += "TrackPacks (productID) VALUES (?)";
        }
        else if (product instanceof TrainSet) {
            insertSQL += "TrainSets (eraID, productID) VALUES (?, ?)";
        }

        PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);

        int counter = 1;
        if (!listOfColumnValues.isEmpty()) {
            for (Object columnValue : listOfColumnValues) {
                if (columnValue instanceof String) {
                    preparedStatement.setString(counter, columnValue.toString());
                } else if (columnValue instanceof Boolean) {
                    preparedStatement.setBoolean(counter, (Boolean)columnValue);
                }
                counter++;
            }
        }

        if (product instanceof Locomotive ||
                product instanceof RollingStock ||
                product instanceof TrainSet) {
            String eraID = Integer.toString(getEraIDFromEraCode(product));
            preparedStatement.setString(counter, eraID);
            counter++;
        }

        preparedStatement.setString(counter, Integer.toString(productID));

        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println(rowsAffected + "row updated");




    }

    public int getEraIDFromEraCode(Product product) throws Exception {
        String eraCode = null;

        if (product instanceof Locomotive) {
            eraCode = ((Locomotive) product).getEra();
        }
        else if (product instanceof RollingStock) {
            eraCode = ((RollingStock) product).getEra();
        }
        else if (product instanceof TrainSet) {
            eraCode = ((TrainSet) product).getEra();
        }

        if (eraCode == null) {
            throw new IllegalArgumentException("This product type does not have an Era");
        }

        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT eraID FROM HistoricalEras WHERE eraCode = ?";
            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(selectSQL);
            preparedStatement.setString(1, eraCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);

            } else {
                throw new SQLException("Invalid eraCode, not in table of HistoricalEras");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


    public int insertIntoProductsTable (String productName, String retailPrice, String quantityInStock,
                                         String manufacturer, String gauge, ProductCategory productCategory) throws Exception {
        try {
            databaseConnectionHandler.openConnection();

            int manufacturerID = insertManufacturer(manufacturer);
            int codeID = produceProductCodeAndReturnCodeID(productCategory);

            String insertSQL = (
                "INSERT INTO Products " +
                "(productName, retailPrice, quantityInStock, gauge, manufacturerID, codeID) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
            );
            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, retailPrice);
            preparedStatement.setString(3, quantityInStock);
            preparedStatement.setString(4, gauge);
            preparedStatement.setString(5, Integer.toString(manufacturerID));
            preparedStatement.setString(6, Integer.toString(codeID));

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + "row inserted into products table");

            return getProductIDFromLastInsertionInProductsTable();


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    public int insertManufacturer (String manufacturer) throws Exception {

        HashMap<Integer, String> manufacturerIDAndManufacturerNameMap = new HashMap<>();

        try {
            databaseConnectionHandler.openConnection();

            boolean isAlreadyInManufacturerTable = false;

            ResultSet resultSetOfManufacturers = getResultSetOfManufacturers(databaseConnectionHandler.getConnection());
            while (resultSetOfManufacturers.next()) {
                if (manufacturer == resultSetOfManufacturers.getString("manufacturerName")) {
                    isAlreadyInManufacturerTable = true;
                }

                manufacturerIDAndManufacturerNameMap.put(
                        resultSetOfManufacturers.getInt("manufacturerID"),
                        resultSetOfManufacturers.getString("manufacturerName")
                );
            }

            if (!isAlreadyInManufacturerTable) {
                //manufacturer provided isn't in manufacturers table, so insert it as a new row
                //and return the manufacturerID it now has
                String insertSQL = "INSERT INTO Manufacturers (manufacturerName) VALUES (?)";
                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
                preparedStatement.setString(1, manufacturer);

                preparedStatement.executeUpdate();

                /*
                gets the set of manufacturerIDs from the hash map that was made from the result set that
                was previously created. Then gets the maximum of this set and adds 1 to it. This is the
                manufacturerID of the newly inserted manufacturer, because the table auto increments the
                manufacturerID
                 */
                return Collections.max(manufacturerIDAndManufacturerNameMap.keySet()) + 1;

            } else {
                //manufacturer provided is already in the table, so simply return its manufacturerID
                String selectSQL = "SELECT manufacturerID FROM Manufacturers WHERE manufacturerName = ?";
                PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(selectSQL);
                preparedStatement.setString(1, manufacturer);

                ResultSet resultSet = preparedStatement.executeQuery();

                //gets the manufacturerID of the manufacturer provided, and returns it
                return resultSet.getInt(1);

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            databaseConnectionHandler.closeConnection();
        }

    }

    public ResultSet getResultSetOfManufacturers (Connection connection) throws SQLException {

        try {
            String selectSQL = "SELECT * FROM Manufacturers";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectSQL);
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int produceProductCodeAndReturnCodeID(ProductCategory productCategory) throws SQLException{
        String codeType = null;
        String codeValue = null;

        switch (productCategory) {
            case TRACKS -> codeType = "R";
            case TRAIN_SETS -> codeType = "M";
            case CONTROLLERS -> codeType = "C";
            case LOCOMOTIVES -> codeType = "L";
            case TRACK_PACKS -> codeType = "P";
            case ROLLING_STOCKS -> codeType = "S";
        }

        boolean isValidCodeNumber = false;

        Random random = new Random();
        while (!isValidCodeNumber) {
            String randomNumberAsString = Integer.toString(random.nextInt(1000, 999999));

            if (!codeWillProduceDuplicateProductCode(codeType, codeValue)) {
                codeValue = randomNumberAsString;
                isValidCodeNumber = true;
            }
        }

        insertNewProductCode(codeType, codeValue);
        return getCodeIDOfLastInsertion();
    }

    public boolean codeWillProduceDuplicateProductCode (String codeType, String codeValue) throws SQLException{

        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT * FROM ProductCodes WHERE codeType = ? AND codeValue = ?";

            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(selectSQL);
            preparedStatement.setString(1, codeType);
            preparedStatement.setString(2, codeValue);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void insertNewProductCode(String codeType, String codeValue) throws SQLException {

        try {
            databaseConnectionHandler.openConnection();
            String insertSQL = "INSERT INTO ProductCodes (codeType, codeValue) VALUES (?, ?)";

            PreparedStatement preparedStatement = databaseConnectionHandler.getConnection().prepareStatement(insertSQL);
            preparedStatement.setString(1, codeType);
            preparedStatement.setString(2, codeValue);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + "row inserted");

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public int getCodeIDOfLastInsertion() throws SQLException {
        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT MAX(codeID) FROM ProductCodes";

            Statement statement = databaseConnectionHandler.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("ProductCodes table is empty");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int getProductIDFromLastInsertionInProductsTable() throws SQLException {
        try {
            databaseConnectionHandler.openConnection();

            String selectSQL = "SELECT MAX(productID) FROM Products";

            Statement statement = databaseConnectionHandler.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Products table is empty, or the query is wrong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


            /*
    - Order queue page
        - order lines pop up from button
        - user details pop up from button
        ✓ dont show userid
    - Product management page
        ✓ edit stock level
        - add (!)
        ✓ delete
    - check the stock levels thing that Nirmal had to do
     */

    public static void main(String[] args) {
        ProductDatabaseOperations pdo = new ProductDatabaseOperations();

        try {
            System.out.println(pdo.getTrackIDFromProductID(9));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
