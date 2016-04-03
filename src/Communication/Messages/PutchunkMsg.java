package Communication.Messages;

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

    public byte[] toByte() {
        String temp = new String();

        temp = this.getMsgType().toString() + " "
                + this.getVersion() + " "
                + this.getSenderID() + " "
                + this.getFileID() + " "
                + this.chunkN + " "
                + this.repDeg + "\r\n\r\n";
        byte[] temp1 = temp.getBytes();

        byte[] last = new byte[temp1.length + body.length];
        System.arraycopy(temp1, 0, last, 0, temp1.length);
        System.arraycopy(body, 0, last, temp1.length, body.length);

        return last;
    }

    public String toString() {
        String result = new String();

        result = this.getMsgType().toString() + " "
                + this.getVersion() + " "
                + this.getSenderID() + " "
                + this.getFileID() + " "
                + this.chunkN + " "
                + this.repDeg + "\r\n\r\n";

        return result;
    }

    @Override
    public byte[] getBytes() {
        byte[] header = this.toString().getBytes();
        byte[] bytes = new byte[header.length + this.body.length];

        System.arraycopy(header, 0, bytes, 0, header.length);
        System.arraycopy(this.body, 0, bytes, header.length, this.body.length);

        return bytes;
    }
}