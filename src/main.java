/**
 * Created by Daniel Nunes on 18-03-2016.
 */
public class main
{
    /* args receives the ServerID followed by the IP multicast adresses and ports in the following order: MC, MDB, MDR.
       In total there will be 7 arguments inside args
     */
    public static void main(String[] args)
    {
        if (args.length != 7)
        {
            System.out.println("Wrong number of arguments, there should be 7: ServerID, MC_IP, MDB_IP, MDR_IP, MC_Port, MBD_Port, MDR_Port");
            System.exit(-1);
        }
        else argsParser(args);
    }

    // args parser into their respective variables
    public static void argsParser(String[] args)
    {
        int serverID;
        String mcIP, mdbIP, mdrIP;
        int mcPort, mdbPort, mdrPort;

        // ServerID
        serverID = Integer.parseInt(args[0]);

        // MC, multicast control channel
        mcIP = args[1];
        mcPort = Integer.parseInt(args[2]);

        // MDB, multicast data backup channel
        mdbIP = args[3];
        mdrPort = Integer.parseInt(args[4]);

        // MDR, multicast data restore channel
        mdbIP = args[5];
        mdrPort = Integer.parseInt(args[6]);
    }
}
