package ChannelListeners.ChannelSpecific;

import Agents.Peer;
import ChannelListeners.Channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChannelMC extends Channel {

    public ChannelMC(Peer _peer, InetAddress _ip, int _port) {
        super(_peer, _ip, _port);
    }

    public void run() {
        System.out.println("Channel MC listening on " + this.getIp() + ":" + this.getPort());

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
                System.out.println("MC - Message received: " + msg);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // TODO: Complete method
    public void send() {
        // send something through Channel MC
    }

}
