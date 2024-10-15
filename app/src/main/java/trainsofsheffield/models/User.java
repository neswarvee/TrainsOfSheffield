package trainsofsheffield.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The User class represents a user in the application.
 * It retrieves user information from the database based on the email address.
 */
public class User {
    private String userID;
    private String email;
    private String firstName;
    private String surname;
    private Role role;
    private Boolean suspended;
    private int addressID;
    public enum Role {
        CUSTOMER,
        STAFF,
        MANAGER;

        public String toString() {
            return switch (this) {
                case CUSTOMER -> "Customer";
                case STAFF -> "Staff";
                case MANAGER -> "Manager";
            };
        }

        static public Role stringToRole(String string) {
            string = string.toLowerCase();
            return switch (string) {
                case "customer" -> CUSTOMER;
                case "staff" -> STAFF;
                case "manager" -> MANAGER;

                default -> throw new IllegalArgumentException("Can't convert this string to Role enum");
            };
        }
    }

    /**
     * Constructs a new User object by fetching information from the database based on the provided email.
     * @param email The email address of the user.
     */
    public User(String email) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Query the database to fetch user information
            String sql = "SELECT userID, firstName, surname, role, accountLocked, addressID FROM Users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            // Populate user information if the query result is not empty
            if (resultSet.next()) {
                this.userID = resultSet.getString("userID");
                this.email = email;
                this.firstName = resultSet.getString("firstName");
                this.surname = resultSet.getString("surname");
                this.role = Role.stringToRole(resultSet.getString("role"));
                this.suspended = resultSet.getBoolean("accountLocked");
                this.addressID = resultSet.getInt("addressID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters, setters probably needed for Register and Manager actions
    public String getUserID() { return userID; }

    public String getEmail() { return email; }
    public void setEmail(String newEmail) { this.email = newEmail; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String newName) { this.firstName = newName; }

    public String getSurname() { return surname; }
    public void setSurname(String newSurname) { this.surname = newSurname; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Boolean getSuspended() { return suspended; }
    public void setSuspended() { this.suspended = !this.suspended; }

    public int getAddressID() { return addressID; }

    public void updateUserDetails(String newName, String newEmail, String newSurname) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String query = "UPDATE Users SET firstName = ?, surname = ?, email = ? WHERE userID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newName);
            pstmt.setString(2, newSurname);
            pstmt.setString(3, newEmail);
            pstmt.setString(4, this.getUserID());

            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                this.setFirstName(newName);
                this.setSurname(newSurname);
                this.setEmail(newEmail);
                
                System.out.println("User details updated successfully.");
            } else {
                System.out.println("Failed to update user details.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
