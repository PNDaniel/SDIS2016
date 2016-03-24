package Agents;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Initiator {

    private InetAddress ip;
    private int port;

    public Initiator(String _ip, int _port) throws UnknownHostException {
        ip = InetAddress.getByName(_ip);
        port = _port;

        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(port)) {
            String msg = "Hi, I'm a initiator.";
            serverSocket.joinGroup(ip);

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ip, port);
            serverSocket.send(msgPacket);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //TODO : Arguments parsing
    public static void main(String[] args) throws UnknownHostException {
        new Initiator(args[0], Integer.parseInt(args[1]));
    }

}
