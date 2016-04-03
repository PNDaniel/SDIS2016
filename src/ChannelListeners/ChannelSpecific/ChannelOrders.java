package ChannelListeners.ChannelSpecific;

import Agents.Peer;
import ChannelListeners.Channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;

public class ChannelOrders extends Channel {

    public ChannelOrders(Peer _peer, InetAddress _ip, int _port) {
        super(_peer, _ip, _port);
    }

    public void run() {
        this.log("I'm listening on " + this.getIp() + ":" + this.getPort());

        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];

        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(this.getPort())) {

            //Joint the Multicast group.
            clientSocket.joinGroup(this.getIp());

            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                msg = msg.replace("\r\n\r\n", " ");

                String[] msg_parts = msg.split(" ");

                if (!msg_parts[0].matches("BACKUP|RESTORE|DELETE|RECLAIM")) {
                    this.log("Received invalid order: " + msg_parts[0]);
                } else if (msg_parts[0].equals("BACKUP")) {
                    this.getPeer().backup(msg_parts[1], Integer.parseInt(msg_parts[2]));
                } else if (msg_parts[0].equals("RESTORE")) {
                    this.getPeer().restore(msg_parts[1]);
                } else if (msg_parts[0].equals("DELETE")) {
                    this.getPeer().delete(msg_parts[1]);
                } else if (msg_parts[0].equals("RECLAIM")) {
                    this.getPeer().reclaim(Integer.parseInt(msg_parts[1]));
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}