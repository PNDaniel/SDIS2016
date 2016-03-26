package Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

    private static final int body_limit = 64000;

    public static String hashConverter(String filename) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(filename.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // TODO: Complete function
    /*
        This function must open the file(filename) and return
        the chunkN' chunk with maximum size of BODY_LIMIT
        @ivolimasilva: Couldn't make this to work :'(
     */
    public static byte[] getBytesFromFile(String filename, int chunkN) {
        return new String("Say no to spaghetti code!").getBytes();
    }

}
