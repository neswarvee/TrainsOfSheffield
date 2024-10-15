package trainsofsheffield.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The HashedPasswordGenerator class provides methods for hashing passwords with salt.
 */
public class HashedPasswordGenerator {

    /**
     * Hashes the provided password with the given salt using SHA-256 algorithm.
     * @param password The password to be hashed.
     * @param salt The salt to be used in the hashing process.
     * @return The hashed password as a hexadecimal string.
     */
    public static String hashPassword(char[] password, String salt) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Decode the Base64 salt into bytes
            byte[] saltBytes = Base64.getDecoder().decode(salt);

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes(saltBytes, new String(password).getBytes());

            // Update the digest with the salted password bytes
            md.update(saltedPasswordBytes);

            // Get the hashed password bytes
            byte[] hashedPasswordBytes = md.digest();

            // Convert the hashed password bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Concatenates two byte arrays.
     * @param arr1 The first byte array.
     * @param arr2 The second byte array.
     * @return The combined byte array.
     */
    public static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }
}