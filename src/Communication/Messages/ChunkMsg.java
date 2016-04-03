package Communication.Messages;

import Communication.Message;

public class ChunkMsg extends Message {

    private int chunkN;
    private byte[] body;

    public ChunkMsg(double _version, int _senderID, String _fileID, int _chunkN, byte[] _body) {
        super(MessageType.CHUNK, _version, _senderID, _fileID);
        this.chunkN = _chunkN;
        this.body = _body;
    }

    public ChunkMsg(int _senderID, String _fileID, int _chunkN, byte[] _body) {
        this(1.0, _senderID, _fileID, _chunkN, _body);
    }

    public String toString() {
        String result = new String();

        result = this.getMsgType().toString() + " "
                + this.getVersion() + " "
                + this.getSenderID() + " "
                + this.getFileID() + " "
                + this.chunkN + " "
                + this.body;
        return result;
    }

}