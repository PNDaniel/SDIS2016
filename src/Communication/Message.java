package Communication;

public class Message {

    public enum MessageType {PUTCHUNK, STORED, GETCHUNK, DELETE, REMOVED, CHUNK, ORDER}

    private MessageType msgType;
    private double version;
    private int senderID;
    private String fileID;

    public Message(MessageType _msgType, double _version, int _senderID, String _fileID) {
        this.msgType = _msgType;
        this.version = _version;
        this.senderID = _senderID;
        this.fileID = _fileID;
    }

    public Message(MessageType _msgType, int _senderID, String _fileID) {
        this(_msgType, 1.0, _senderID, _fileID);
    }

    public MessageType getMsgType() {
        return this.msgType;
    }

    public double getVersion() {
        return this.version;
    }

    public int getSenderID() {
        return this.senderID;
    }

    public String getFileID() {
        return this.fileID;
    }

}