package Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Registry {

    private String fileID;
    private int chunkN;
    private int repDeg;
    private ArrayList<Integer> serverID = new ArrayList<Integer>();

    public Registry(String _fileID, int _chunkN, int _repDeg) {
        this.fileID = _fileID;
        this.chunkN = _chunkN;
        this.repDeg = _repDeg;
    }

    public Registry(String _fileID, int _chunkN) {
        this.fileID = _fileID;
        this.chunkN = _chunkN;
    }

    public void setRepDeg(int _repDeg) {
        this.repDeg = _repDeg;
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

    public int getRepDeg() {
        return this.repDeg;
    }

    public ArrayList<Integer> getServerID() {
        return this.serverID;
    }

    public boolean isBackedup() {
        if (this.serverID.size() >= this.repDeg) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "FileID: " + this.fileID +
                "\nChunkN: " + this.chunkN +
                "\nRepDeg: " + this.repDeg +
                "\nServerID: " + this.serverID;
    }

    @Override
    public int hashCode() {
        return this.fileID.hashCode() + this.chunkN + this.repDeg;
    }

}