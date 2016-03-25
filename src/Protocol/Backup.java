package Protocol;

import Agents.Peer;
import Utils.Chunk;
import Utils.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel Nunes on 25-03-2016.
 */
public class Backup
{

    public Backup(String fileID, int repDegree) throws IOException, NoSuchAlgorithmException {
        Message msg = null;
        FileInputStream file = new FileInputStream(fileID);
        byte[] body = new byte[Chunk.getChunkSize()];
        int i = 1;
        int halt = 0;
        if ((file.getChannel().size() % Chunk.getChunkSize()) == 0)
        {
            do
            {
                halt = file.read(body);
                msg = new Message("PUTCHUNK","1.0", Peer.getServerID(), fileID,i,repDegree,body);
                i++;
            }while(halt != -1);
            msg = new Message("PUTCHUNK","1.0", Peer.getServerID(), fileID,i,repDegree,body);
        }
        else
        {
            do
            {
                halt = file.read(body);
                msg = new Message("PUTCHUNK","1.0", Peer.getServerID(), fileID,i,repDegree,body);
                i++;
            }while(halt != -1);
        }
        file.close();
    }
}