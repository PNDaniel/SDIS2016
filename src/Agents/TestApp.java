package Agents;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TestApp {

    private InetAddress ip;
    private int port;

    //TODO : Arguments parsing
    public static void main(String[] args) throws UnknownHostException {

        System.out.println(InetAddress.getLocalHost().getHostAddress());

        if (true) {
            new TestApp(args[0], Integer.parseInt(args[1]));
        } else {
            System.out.println("Wrong usage! Try: java TestApp <ip> <port> <sub-protocol> <opnd1> <opnd2>");
            System.exit(-1);
        }
    }

    public TestApp(String _ip, int _port) throws UnknownHostException {
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

}
