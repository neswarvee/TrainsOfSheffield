package trainsofsheffield.models;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import trainsofsheffield.util.AES;

public class BankDetails {
    public enum CardType {
        MASTERCARD,
        VISA,
        UNIONPAY,
        MIR;

        public String toString() {
            return switch (this) {
                case MASTERCARD -> "Mastercard";
                case VISA -> "Visa";
                case UNIONPAY -> "UnionPay";
                case MIR -> "Mir";
            };
        }

        static public CardType stringToRole(String string) {
            string = string.toLowerCase();
            return switch (string) {
                case "mastercard" -> MASTERCARD;
                case "visa" -> VISA;
                case "unionpay" -> UNIONPAY;
                case "mir" -> MIR;

                default -> throw new IllegalArgumentException("Can't convert this string to CardType enum");
            };
        }
    }

    private boolean bankDetailsAdded;
    private String userID;

    public BankDetails(String userID) {
        this.userID = userID;

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            // Query the database to fetch address information
            String sql = "SELECT * FROM BankDetails WHERE userID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();

            // If resultSet is empty, then there are no records for the provided userID
            if (resultSet.next()) {
                this.bankDetailsAdded = true;
            } else {
                this.bankDetailsAdded = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean getBankDetailsAdded() { return bankDetailsAdded; }
    
    public void insertUpdateBankDetails(CardType cardType, String cardHolderName, String cardNumber, String cardExpiryDate, String cardCVV) {
        // Key generation. We assume that this key will be securely stored on the different server
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        // Data encryption
        String encryptedCardType = AES.encrypt(encodedKey, cardType.toString());
        String encryptedCardHolderName = AES.encrypt(encodedKey, cardHolderName);
        String encryptedCardNumber = AES.encrypt(encodedKey, cardNumber);
        String encryptedCardExpiryDate = AES.encrypt(encodedKey, cardExpiryDate);
        String encryptedCardCVV = AES.encrypt(encodedKey, cardCVV);

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            boolean update;
            String sql;
            PreparedStatement preparedStatement;

            if (!bankDetailsAdded) {
                update = false;
                sql = "INSERT INTO BankDetails " +
                        "(userID, cardType, cardHolderName, cardNumber, cardExpiryDate, cardCVV) " + 
                        "VALUES (?, ?, ?, ?, ?, ?);";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, encryptedCardType);
                preparedStatement.setString(3, encryptedCardHolderName);
                preparedStatement.setString(4, encryptedCardNumber);
                preparedStatement.setString(5, encryptedCardExpiryDate);
                preparedStatement.setString(6, encryptedCardCVV);
            }
            else {
                update = true;
                sql = "UPDATE BankDetails " +
                        "SET cardType = ?, cardHolderName = ?, cardNumber = ?, cardExpiryDate = ?, cardCVV = ? " +
                        "WHERE userID = ?;";
                
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, encryptedCardType);        
                preparedStatement.setString(2, encryptedCardHolderName);
                preparedStatement.setString(3, encryptedCardNumber);
                preparedStatement.setString(4, encryptedCardExpiryDate);
                preparedStatement.setString(5, encryptedCardCVV);
                preparedStatement.setString(6, userID);
            }
    
            int inserted = preparedStatement.executeUpdate();
            if (inserted > 0) {
                if (update) {
                    System.out.println("Bank details updated successfully.");
                }
                else {
                    System.out.println("Bank details inserted successfully.");
                }
            } else {
                if (update) {
                    System.out.println("Failed to update bank details.");
                }
                else {
                    System.out.println("Failed to insert bank details.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String validateBankDetails(String cardHolderName, String cardNumber, String expiryDate, String cvv) {
        // Check if the card holder's name only contains alphabetic characters
        if (!cardHolderName.matches("[a-zA-Z ]+")) {
            return "Card Holder's Name should only contain alphabetic characters";
        }
    
        // Check if the card number is 16 digits long
        if (!cardNumber.matches("\\d{16}")) {
            return "Card Number should be 16 digits long";
        }

        // Check if the expiry date is in the format "MM/yy"
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return "Expiry Date should be in the format MM/yy";
        }
    
        // Check if the CVV is 3 digits long
        if (!cvv.matches("\\d{3}")) {
            return "CVV should be 3 digits long";
        }
    
        return null;
    }
}
