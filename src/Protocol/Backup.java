package Protocol;

import Agents.Peer;
import Utils.Chunk;
import Utils.Message;

/**
 * Created by Daniel Nunes on 25-03-2016.
 */
public class Backup
{

    public Backup(String fileID, int repDegree)
    {
        Message msg = null;
        byte[] body = new byte[Chunk.getChunkSize()];


        msg = new Message("PUTCHUNK","1.0", Peer.getServerID(), fileID,i,repDegree,body);

    }

    public void createChunk()
    {

    }
}
