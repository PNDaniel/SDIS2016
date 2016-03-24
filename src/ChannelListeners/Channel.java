package ChannelListeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Channel extends Thread {

    private InetAddress ip;
    private int port;

    public Channel(InetAddress _ip, int _port) {
        this.ip = _ip;
        this.port = _port;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    // TODO: Erase this method because every child will override it
    public void run() {

        System.out.println("Channel listening...");

        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];

        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(port)) {

            //Joint the Multicast group.
            clientSocket.joinGroup(ip);

            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Message received: " + msg);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}