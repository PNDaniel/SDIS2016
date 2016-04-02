package Utils;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtils {

    private static final int body_limit = 64000;
    private static final String foldername = "Backup_";

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

    public static ArrayList<byte[]> getBytesFromFile(String filename) throws IOException {
        ArrayList<byte[]> fileChunks = new ArrayList<>();
        FileInputStream file = new FileInputStream(filename);
        byte[] body = new byte[body_limit];
        int halt;
        if ((file.getChannel().size() % body_limit) == 0) {
            //System.out.println("64KB");
            do {
                halt = file.read(body);
                if (halt != -1) {
                    fileChunks.add(Arrays.copyOf(body, halt));
                    //System.out.println("BodyCreator:" + new String(Arrays.copyOf(body, halt)));
                }
            } while (halt != -1);
            byte[] last = new byte[0];
            fileChunks.add(Arrays.copyOf(last, 0));
        } else {
            //System.out.println("65KB");
            do {
                halt = file.read(body);
                if (halt != -1) {
                    fileChunks.add(Arrays.copyOf(body, halt));
                    //System.out.println("BodyCreator:" + new String(Arrays.copyOf(body, halt)));
                }
            } while (halt != -1);
        }
        file.close();

        return fileChunks;
    }

    public static void createFolder(int _id) {
        File theDir = new File(foldername + _id);

        if (!theDir.exists()) {
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException err) {
                err.printStackTrace();
            }
        }
    }

    public static boolean createChunk(int id, String fileID, int chunkNo, byte[] data) {
        String filename = foldername + id + "/" + fileID + "_" + chunkNo;
        File chunkfile = new File(filename);
        if (chunkfile.exists()) {
            return false;
        } else {
            FileOutputStream file = null;
            try {
                file = new FileOutputStream(filename);
                file.write(data);
                file.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static ArrayList<Integer> removeFile(String fileID, int id) {
        ArrayList<Integer> chunks = new ArrayList<Integer>();
        File folder = new File(System.getProperty("user.dir/" + foldername));
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(fileID)) {
                chunks.add(Integer.parseInt(listOfFiles[i].getName().split("_")[1]));
                listOfFiles[i].delete();
                break;
            }
        }
        return chunks;
    }

}
