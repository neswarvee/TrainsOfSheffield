package trainsofsheffield.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class AES {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static String encrypt(String key, String valueToEncrypt) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedValue = cipher.doFinal(valueToEncrypt.getBytes());

            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key, String encryptedValue) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

