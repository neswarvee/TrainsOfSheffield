package trainsofsheffield.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {
    private int addressID;
    private String street, city, postCode;

    public Address(int addressID) {
        this.addressID = addressID;

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Query the database to fetch address information
            String sql = "SELECT A.addressID, S.street, C.city, P.postcode " +
                        "FROM Addresses A " +
                        "INNER JOIN Streets S ON A.streetID = S.streetID " +
                        "INNER JOIN Cities C ON A.cityID = C.cityID " +
                        "INNER JOIN Postcodes P ON A.postcodeID = P.postcodeID " +
                        "WHERE A.addressID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, addressID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                this.setStreet(resultSet.getString("street"));
                this.setCity(resultSet.getString("city"));
                this.setPostCode(resultSet.getString("postcode"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAddressID() { return this.addressID; }

    public String getStreet() { return street; }
    public void setStreet(String newSteet) { this.street = newSteet; }

    public String getCity() { return city; }
    public void setCity(String newCity) { this.city = newCity; }

    public String getPostCode() { return postCode; }
    public void setPostCode(String newPostCode) { this.postCode = newPostCode; }

    public void updateAddress(String newStreet, String newCity, String newPostCode) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Check if the new street exists
            String checkStreet = "SELECT streetID FROM Streets WHERE street = ?;";
            PreparedStatement pstmtStreet = connection.prepareStatement(checkStreet);
            pstmtStreet.setString(1, newStreet);
            ResultSet rsStreet = pstmtStreet.executeQuery();
            int streetID;
            if (rsStreet.next()) {
                // If the street exists, get its ID
                streetID = rsStreet.getInt("streetID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertStreet = "INSERT INTO Streets (street) VALUES (?);";
                pstmtStreet = connection.prepareStatement(insertStreet, Statement.RETURN_GENERATED_KEYS);
                pstmtStreet.setString(1, newStreet);
                pstmtStreet.executeUpdate();
                rsStreet = pstmtStreet.getGeneratedKeys();
                rsStreet.next();
                streetID = rsStreet.getInt(1);
            }

            // Check if the new city exists
            String checkCity = "SELECT cityID FROM Cities WHERE city = ?;";
            PreparedStatement pstmtCity = connection.prepareStatement(checkCity);
            pstmtCity.setString(1, newCity);
            ResultSet rsCity = pstmtCity.executeQuery();
            int cityID;
            if (rsCity.next()) {
                // If the street exists, get its ID
                cityID = rsCity.getInt("cityID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertCity = "INSERT INTO Cities (city) VALUES (?);";
                pstmtCity = connection.prepareStatement(insertCity, Statement.RETURN_GENERATED_KEYS);
                pstmtCity.setString(1, newCity);
                pstmtCity.executeUpdate();
                rsCity = pstmtCity.getGeneratedKeys();
                rsCity.next();
                cityID = rsCity.getInt(1);
            }
            
            // Check if the new postcode exists
            String checkPostCode = "SELECT postcodeID FROM Postcodes WHERE postcode = ?;";
            PreparedStatement pstmtPostCode = connection.prepareStatement(checkPostCode);
            pstmtPostCode.setString(1, newPostCode);
            ResultSet rsPostCode = pstmtPostCode.executeQuery();
            int postCodeID;
            if (rsPostCode.next()) {
                // If the street exists, get its ID
                postCodeID = rsPostCode.getInt("postcodeID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertPostCode = "INSERT INTO Postcodes (postcode) VALUES (?);";
                pstmtPostCode = connection.prepareStatement(insertPostCode, Statement.RETURN_GENERATED_KEYS);
                pstmtPostCode.setString(1, newPostCode);
                pstmtPostCode.executeUpdate();
                rsPostCode = pstmtPostCode.getGeneratedKeys();
                rsPostCode.next();
                postCodeID = rsPostCode.getInt(1);
            }
    
            // Update the address
            String updateAddress = "UPDATE Addresses SET streetID = ?, cityID = ?, postcodeID = ? WHERE addressID = ?;";
            PreparedStatement pstmtAddress = connection.prepareStatement(updateAddress);
            pstmtAddress.setInt(1, streetID);
            pstmtAddress.setInt(2, cityID);
            pstmtAddress.setInt(3, postCodeID);
            pstmtAddress.setInt(4, this.getAddressID());
            int updated = pstmtAddress.executeUpdate();
    
            if (updated > 0) {
                this.setStreet(newStreet);
                this.setCity(newCity);
                this.setPostCode(newPostCode);
                System.out.println("Address updated successfully.");
            } else {
                System.out.println("Failed to update address.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int insertAddress(String newStreet, String newCity, String newPostCode) {
        int addressID = 0;

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Check if the new street exists
            String checkStreet = "SELECT streetID FROM Streets WHERE street = ?;";
            PreparedStatement pstmtStreet = connection.prepareStatement(checkStreet);
            pstmtStreet.setString(1, newStreet);
            ResultSet rsStreet = pstmtStreet.executeQuery();
            int streetID;
            if (rsStreet.next()) {
                // If the street exists, get its ID
                streetID = rsStreet.getInt("streetID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertStreet = "INSERT INTO Streets (street) VALUES (?);";
                pstmtStreet = connection.prepareStatement(insertStreet, Statement.RETURN_GENERATED_KEYS);
                pstmtStreet.setString(1, newStreet);
                pstmtStreet.executeUpdate();
                rsStreet = pstmtStreet.getGeneratedKeys();
                rsStreet.next();
                streetID = rsStreet.getInt(1);
            }

            // Check if the new city exists
            String checkCity = "SELECT cityID FROM Cities WHERE city = ?;";
            PreparedStatement pstmtCity = connection.prepareStatement(checkCity);
            pstmtCity.setString(1, newCity);
            ResultSet rsCity = pstmtCity.executeQuery();
            int cityID;
            if (rsCity.next()) {
                // If the street exists, get its ID
                cityID = rsCity.getInt("cityID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertCity = "INSERT INTO Cities (city) VALUES (?);";
                pstmtCity = connection.prepareStatement(insertCity, Statement.RETURN_GENERATED_KEYS);
                pstmtCity.setString(1, newCity);
                pstmtCity.executeUpdate();
                rsCity = pstmtCity.getGeneratedKeys();
                rsCity.next();
                cityID = rsCity.getInt(1);
            }
            
            // Check if the new postcode exists
            String checkPostCode = "SELECT postcodeID FROM Postcodes WHERE postcode = ?;";
            PreparedStatement pstmtPostCode = connection.prepareStatement(checkPostCode);
            pstmtPostCode.setString(1, newPostCode);
            ResultSet rsPostCode = pstmtPostCode.executeQuery();
            int postCodeID;
            if (rsPostCode.next()) {
                // If the street exists, get its ID
                postCodeID = rsPostCode.getInt("postcodeID");
            } else {
                // If the street does not exist, insert it and get its ID
                String insertPostCode = "INSERT INTO Postcodes (postcode) VALUES (?);";
                pstmtPostCode = connection.prepareStatement(insertPostCode, Statement.RETURN_GENERATED_KEYS);
                pstmtPostCode.setString(1, newPostCode);
                pstmtPostCode.executeUpdate();
                rsPostCode = pstmtPostCode.getGeneratedKeys();
                rsPostCode.next();
                postCodeID = rsPostCode.getInt(1);
            }

            // Insert new address
            String insertAddress = "INSERT INTO Addresses (streetID, cityID, postcodeID) VALUES (?, ?, ?);";
            PreparedStatement pstmtAddress = connection.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS);
            pstmtAddress.setInt(1, streetID);
            pstmtAddress.setInt(2, cityID);
            pstmtAddress.setInt(3, postCodeID);
            pstmtAddress.executeUpdate();
            ResultSet rsAddress = pstmtAddress.getGeneratedKeys();
            if (rsAddress.next()) {
                addressID = rsAddress.getInt(1);
            }

            System.out.println("New address inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressID;
    }
}