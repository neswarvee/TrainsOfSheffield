package trainsofsheffield.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import trainsofsheffield.util.HashedPasswordGenerator;

/**
 * The Login class represents a login attempt and its result.
 * It contains methods to verify the login information and check the account status.
 */
public class Login {
    private String message;
    private Session session;

    /**
     * Default constructor
     */
    public Login() {}

    /**
     * Constructor with message parameter
     * @param message the message produced during login attempt
     */
    public Login(String message) {
        this.message = message;
    }

    /**
     * Constructor with message and User parameters
     * @param message the message produced during login attempt
     * @param user The User object of the logged in user
     */
    public Login(String message, User user) {
        this.message = message;
        this.session = new Session(user);
    }

    // getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Method to verify the login information.
     * It queries the database for the user information and checks the entered password against the stored hash.
     * @param email The email entered by the user.
     * @param enteredPassword The password entered by the user.
     * @return A Login object containing the result of the login attempt and the session object if the login is successful.
     */
    public Login verifyLogin(String email, char[] enteredPassword) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            User user = new User(email);
            String userID = user.getUserID();

            // Query the database to fetch user information
            String sql = "SELECT salt, passwordHash FROM Users WHERE userID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String storedPasswordHash = resultSet.getString("passwordHash");

                // Check if the account is locked
                if (user.getSuspended()) {
                    return new Login("Account is locked. Please contact support.");
                } 
                else {
                    // Verify the entered password against the stored hash
                    if (verifyPassword(enteredPassword, storedPasswordHash, salt)) {
                        return new Login("Login successful", user);
                    }
                    else {
                        return new Login("Login unsuccessful");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Login("Login unsuccessful");
    }

    /**
     * Method to verify the entered password against the stored hash.
     * It hashes the entered password with the stored salt and compares it with the stored hash.
     * @param enteredPassword The password entered by the user.
     * @param storedPasswordHash The hash of the stored password.
     * @param salt The salt used to hash the stored password.
     * @return A boolean value indicating whether the entered password is correct.
     */
    private static boolean verifyPassword(char[] enteredPassword, String storedPasswordHash, String salt) {
        try {
            String hashedEnteredPassword = HashedPasswordGenerator.hashPassword(enteredPassword, salt);
            return hashedEnteredPassword.equals(storedPasswordHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
