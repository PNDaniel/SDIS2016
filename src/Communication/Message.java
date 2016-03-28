package Communication;

public abstract class Message {

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

    // NOTE: Maybe will not be implemented
    public Message(String _msg) {
        try {
            _msg = _msg.replace("\r\n\r\n", " ");
            String[] msg_parts = _msg.split(" ");
            for (int i = 0; i < MessageType.values().length; i++) {
                if (!msg_parts[0].equals(MessageType.values()[i])) {
                    throw new InvalidMessage(MessageType.values()[i].toString());
                }
            }
            this.msgType = MessageType.valueOf(msg_parts[0]);
        } catch (InvalidMessage err) {
            System.out.println("Invalid message.");
            err.printStackTrace();
        }
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