/**
 * Created by Daniel Nunes on 17-03-2016.
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;

public class File
{
    protected String fileName;
    protected byte[] fileID;
    protected String owner;
    protected Date dateModified;
    protected int replicationNo;
    protected int fileSize;
    protected LinkedList chunksList;


    public File(String filename, Date dateModified, String owner, int replicationNo)
    {
        this.fileName = filename;
        this.dateModified = dateModified;
        this.owner = owner;
        this.replicationNo = replicationNo;
        String temp = filename + owner + dateModified + replicationNo;
        MessageDigest tempSha = null;
        try
        {
            tempSha = MessageDigest.getInstance("SHA-256");
            fileID = tempSha.digest(temp.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
