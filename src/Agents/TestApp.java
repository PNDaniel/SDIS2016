package Agents;

import Communication.Order;
import Communication.Orders.BackupOrder;
import Communication.Orders.DeleteOrder;
import Communication.Orders.ReclaimOrder;
import Communication.Orders.RestoreOrder;
import sun.awt.image.ShortInterleavedRaster;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TestApp {

    private InetAddress ip;
    private int port;

    public static void main(String[] args) {
        new TestApp(args);
    }

    // TODO : Only prepared for <IP> <PORT> and not <IP:PORT> or <PORT>
    public TestApp(String[] args) {
        if ((!args[0].matches("^((\\d*){1,3}.){3}.(\\d)*$")) ||
                (!args[1].matches("\\d{4}")) ||
                (!args[2].matches("BACKUP|RESTORE|DELETE|RECLAIM"))) {
            System.out.println("Wrong usage! Try: java TestApp <ip> <port> <sub-protocol> <opnd1> <opnd2>");
            System.exit(-1);
        } else {

            try {
                this.ip = InetAddress.getByName(args[0]);
                this.port = Integer.parseInt(args[1]);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }

            switch (args[2].toUpperCase()) {
                case "BACKUP":
                    this.backup(args[3], Integer.parseInt(args[4]));
                    break;
                case "DELETE":
                    this.delete(args[3]);
                    break;
                case "RECLAIM":
                    this.reclaim(Integer.parseInt(args[3]));
                    break;
                case "RESTORE":
                    this.restore(args[3]);
                    break;
            }
        }
    }

    public void backup(String _filename, int _degree) {
        BackupOrder order = new BackupOrder(_filename, _degree);
        this.sendOrder(order);
    }

    public void delete(String _filename) {
        DeleteOrder order = new DeleteOrder(_filename);
        this.sendOrder(order);
    }

    public void reclaim(int _size) {
        ReclaimOrder order = new ReclaimOrder(_size);
        this.sendOrder(order);
    }

    public void restore(String _filename) {
        RestoreOrder order = new RestoreOrder(_filename);
        this.sendOrder(order);
    }

    public void sendOrder(Order order) {
        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(this.port)) {
            String msg = order.toString();
            serverSocket.joinGroup(this.ip);

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
