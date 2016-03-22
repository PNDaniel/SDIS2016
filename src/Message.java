/**
 * Created by Daniel Nunes on 18-03-2016.
 */
public class Message
{
    public static final byte[] CRLF= {'\r', '\n', '\r', '\n'};

    public Message()
    {}

    public String createPutChunk()
    {
        String message = "PUTCHUNK " + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + repDegree + CRLF + CRLF + body;

    }

    public String createStored()
    {
        String message = "STORED " + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + CRLF + CRLF;
    }

    public String createGetChunk()
    {
        String message = "GETCHUNK " + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + CRLF + CRLF;
    }

    public String createChunk()
    {
        String message = "CHUNK " + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + CRLF + CRLF + body;
    }

    public String createDelete()
    {
        String message = "DELETE " + " " + version + " " + senderID + " " + fileID +  " " + CRLF + CRLF;
    }

    public String createRemoved()
    {
        String message = "REMOVED " + " " + version + " " + senderID + " " + fileID + " " + chunkNo + " " + CRLF + CRLF;
    }
}
