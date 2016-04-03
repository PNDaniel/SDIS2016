package Utils;

public class Chunk implements Comparable<Chunk> {

    private String fileID;
    private int chunkN;
    private byte[] body;

    public Chunk(String _fileID, int _chunkN, byte[] _body) {
        this.fileID = _fileID;
        this.chunkN = _chunkN;
        this.body = _body;
    }

    public String getFileID() {
        return fileID;
    }

    public int getChunkN() {
        return chunkN;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public int compareTo(Chunk o) {
        return this.chunkN - o.getChunkN();
    }
}
