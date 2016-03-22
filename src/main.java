import Channel.MC;
import Protocol.Backup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by Daniel Nunes on 18-03-2016.
 */
public class main
{
    protected int serverID;
    protected static InetAddress mcIP, mdbIP, mdrIP;
    protected static int mcPort, mdbPort, mdrPort;

    /* args receives the ServerID followed by the IP multicast adresses and ports in the following order: MC, MDB, MDR.
       In total there will be 7 arguments inside args
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length != 7)
        {
            System.out.println("Wrong number of arguments, there should be 7: ServerID, MC_IP, MDB_IP, MDR_IP, MC_Port, MBD_Port, MDR_Port");
            System.exit(-1);
        }
        else argsParser(args);

        new MC(mcIP, mcPort);

        new Backup();
    }

    // args parser into their respective variables
    public static void argsParser(String[] args) throws UnknownHostException
    {
        int serverID;

        // ServerID
        serverID = Integer.parseInt(args[0]);

        // MC, multicast control channel
        mcIP = InetAddress.getByName(args[1]);
        mcPort = Integer.parseInt(args[2]);

        // MDB, multicast data backup channel
        mdbIP = InetAddress.getByName(args[3]);
        mdbPort = Integer.parseInt(args[4]);

        // MDR, multicast data restore channel
        mdbIP = InetAddress.getByName(args[5]);
        mdrPort = Integer.parseInt(args[6]);
    }
}
