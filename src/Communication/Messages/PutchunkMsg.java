package Communication.Messages;

import Communication.InvalidMessage;
import Communication.Message;

public class PutchunkMsg extends Message {

    private int chunkN;
    private int repDeg;
    private byte[] body;

    public PutchunkMsg(double _version, int _senderID, String _fileID, int _chunkN, int _repDeg, byte[] _body) {
        super(MessageType.PUTCHUNK, _version, _senderID, _fileID);
        this.chunkN = _chunkN;
        this.repDeg = _repDeg;
        this.body = _body;
    }

    public PutchunkMsg(int _senderID, String _fileID, int _chunkN, int _repDeg, byte[] _body) {
        this(1.0, _senderID, _fileID, _chunkN, _repDeg, _body);
    }

    @Override
    public String toString() {
        String result = new String();

        result = this.getMsgType().toString() + " "
                + this.getVersion() + " "
                + this.getSenderID() + " "
                + this.getFileID() + " "
                + this.chunkN + " "
                + this.repDeg + "\r\n\r\n"
                + this.body;

        return result;
    }

}