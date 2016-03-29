package Utils;

public class Registry {

    private int serverID;
    private String fileID;
    private int chunkN;

    public Registry(int _serverID, String _fileID, int _chunkN) {
        this.serverID = _serverID;
        this.fileID = _fileID;
        this.chunkN = _chunkN;
    }

}
