/**
 * Created by Daniel Nunes on 17-03-2016.
 */
public class Chunk {

    protected String fileID;
    protected int chunkNo;
    protected int originalRep;
    protected int currentRep;

    public Chunk(String fileID, int chunkNo, int originalRep) {
        this.fileID = fileID;
        this.chunkNo = chunkNo;
        this.originalRep = originalRep;
        currentRep = originalRep;
    }

}
