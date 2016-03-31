package Utils;

import java.util.ArrayList;

public class Registry {

    private String fileID;
    private int chunkN;
    private int regDeg;
    private ArrayList<Integer> serverID = new ArrayList<Integer>();

    public Registry(String _fileID, int _chunkN, int _repDeg) {
        this.fileID = _fileID;
        this.chunkN = _chunkN;
        this.regDeg = _repDeg;
    }

    public void addServerID(int _serverID) {
        this.serverID.add(_serverID);
    }

    public String getFileID() {
        return this.fileID;
    }

    public int getChunkN() {
        return this.chunkN;
    }

    public int getRegDeg() {
        return this.regDeg;
    }

    public ArrayList<Integer> getServerID() {
        return this.serverID;
    }

    @Override
    public String toString() {
        String result = new String("FileID: " + this.fileID +
                "\nChunkN: " + this.chunkN +
                "\nRepDeg: " + this.regDeg +
                "\nServerID:");
        for (Integer id : this.serverID) {
            result.concat("\n " + id);
        }
        return result;
    }

}