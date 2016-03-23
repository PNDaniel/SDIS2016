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
        byte[] receiveBufffer = new byte[CHUNK_SIZE];
        byte[] sendBufffer = new byte[CHUNK_SIZE];
        try
        {
            DatagramPacket sendPacket = new DatagramPacket(sendBufffer, sendBufffer.length, address, port);
            mcSocket.send(sendPacket);
            Thread.sleep(500);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveBufffer, receiveBufffer.length);
            mcSocket.receive(receivePacket);

            String msg = new String(receiveBufffer, 0, receiveBufffer.length);
            System.out.println("Message received: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void run()
    {
        byte[] buf = new byte[CHUNK_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        send(packet);
    }*/

    /*public void send(DatagramPacket packet) throws InterruptedException {
        try
        {
            mcSocket.send(packet);
            Thread.sleep(500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive()
    {
        try
        {
            byte[] buf = new byte[CHUNK_SIZE];
            while(true)
            {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                mcSocket.receive(packet);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Message received: " + msg);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    */
}
