package trainsofsheffield.models;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;
import trainsofsheffield.util.HashedPasswordGenerator;


public class Register {
    private Session session; 
    private String message;  
    /**
     * Default constructor
     */
    public Register() {}

    /**
     * Constructor with message parameter
     * @param message the message produced during register attempt
     */
    public Register(String message) {
        this.message = message;
    }

    //Getters and setters
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

    public Register verifyRegister(String firstname,String surname,String email,char[] registerPasswordChars,String streetLine, String city, String postcode) {
        
        //Validating all the fields with Regex
        String nameRegex = "^[a-zA-Z]{1,36}$";
        if (firstname == "" || firstname.matches(nameRegex) == false){
            return(new Register("Invalid Firstname"));
        }

        String surnameRegex = "^[a-zA-Z]+('[a-zA-Z]+)?(-[a-zA-Z]+('[a-zA-Z]+)?)?$";
        if (surname == "" || surname.matches(surnameRegex) == false){
            return(new Register("Invalid Surname"));
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == "" || email.matches(emailRegex) == false){
            return(new Register("Invalid Email"));
        }

        String streetLineRegex = "^[a-zA-Z0-9]+('[a-zA-Z0-9]+)?(-[a-zA-Z0-9]+('[a-zA-Z0-9]+)?)*( [a-zA-Z0-9]+('[a-zA-Z0-9]+)?(-[a-zA-Z0-9]+('[a-zA-Z0-9]+)?)*)*$";
        if (surname == "" || streetLine.matches(streetLineRegex) == false){
            return(new Register("Invalid Street Name"));
        }

        if (city == "" || city.matches(surnameRegex) == false){
            return(new Register("Invalid City Name"));
        }

        String postcodeRegex  = "^[A-Z]{1,2}\\d[A-Z\\d]? \\d[A-Z\\d]{2}$";
        if (postcode == "" || postcode.matches(postcodeRegex) == false){
            return(new Register("Invalid Postcode"));
        }

        int addressID = Address.insertAddress(streetLine, city, postcode);

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            UUID uniqueID = UUID.randomUUID();
            String userID = uniqueID.toString();
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String hashedPassword = HashedPasswordGenerator.hashPassword(registerPasswordChars, encodedSalt).toString();
            String sql = "INSERT INTO Users (userID, email, salt, passwordHash, accountLocked, firstname, surname, role, addressID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Setting values for each placeholder
            statement.setString(1, userID);
            statement.setString(2, email);
            statement.setString(3, encodedSalt);
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, false);
            statement.setString(6, firstname);
            statement.setString(7, surname);
            statement.setString(8, "Customer");
            statement.setInt(9 , addressID);

            statement.executeUpdate();
            return new Register("Registration Successful");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return new Register("Register Unsuccessful");
    }
}
