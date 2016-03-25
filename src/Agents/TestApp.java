package Agents;

import Protocols.Order;

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

        //System.out.println(InetAddress.getLocalHost().getHostAddress());

        if (true) {
            new TestApp(args[0], Integer.parseInt(args[1]), args[3], Integer.parseInt(args[4]));
        } else {
            System.out.println("Wrong usage! Try: java TestApp <ip> <port> <sub-protocol> <opnd1> <opnd2>");
            System.exit(-1);
        }
    }

    public TestApp(String _ip, int _port, String _filename, int _degree) throws UnknownHostException {

        this.ip = InetAddress.getByName(_ip);
        this.port = _port;

        Order order = new Order("BACKUP", _filename, _degree);
        this.sendOrder(order);

    }

    public void sendOrder(Order order) {
        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(this.port)) {
            String msg = order.toString();
            serverSocket.joinGroup(ip);

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, this.ip, this.port);
            serverSocket.send(msgPacket);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
