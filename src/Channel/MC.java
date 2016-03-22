package Channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Daniel Nunes on 22-03-2016.
 */
public class MC extends Thread
{
    static int CHUNK_SIZE = 64*1024;
    protected static InetAddress address;
    protected static int port;
    protected static MulticastSocket mcSocket;

    public MC(InetAddress address, int port) throws IOException
    {
        this.address = address;
        this.port = port;

        mcSocket = new MulticastSocket(port);
        mcSocket.joinGroup(address);
        this.start();
    }

    public void run()
    {
        DatagramPacket receivePacket;
        byte[] buf = new byte[CHUNK_SIZE];
        while(true) {
            try (MulticastSocket mcSocket = new MulticastSocket(port))
            {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
