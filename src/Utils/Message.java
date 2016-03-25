package Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel Nunes on 25-03-2016.
 */
public class Message
{
    protected String messageType;
    protected String version;
    protected int senderID;
    protected byte[] fileID;
    protected int chunkNo;
    protected int repDegree;
    protected byte[] body;

    public Message(String messageType, String version, int senderID, String fileID, int chunkNo, int repDegree, byte[] body) throws NoSuchAlgorithmException
    {
        this.messageType = messageType;
        this.version = version;
        this.senderID = senderID;
        this.fileID = hashConverter(fileID);
        this.chunkNo = chunkNo;
        this.repDegree = repDegree;
        this.body = body;
    }

    public String createMessage()
    {
        String message = null;
        switch (messageType) {
            case "PUTCHUNK" : message = messageType + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + repDegree + " " +  "\r\n\r\n" + " " +  body;
                break;
            case "STORED"  : message = messageType + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + repDegree + " " +  "\r\n\r\n";
                break;
            case "GETCHUNK" : message = messageType + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + "\r\n\r\n";
                break;
            case "CHUNK"  : message = messageType + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + repDegree + " " +  "\r\n\r\n" + " " +  body;
                break;
            case "DELETE"  : message = messageType + " " + version + " " + senderID + " " + fileID + " " + repDegree + " " +  "\r\n\r\n";
                break;
            case "REMOVED"  : message = messageType + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + repDegree + " " +  "\r\n\r\n";
                break;
        }
        return message;
    }

    public byte[] hashConverter(String fileID) throws NoSuchAlgorithmException
    {
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        return msgDigest.digest(fileID.getBytes(StandardCharsets.UTF_8));
    }
}
