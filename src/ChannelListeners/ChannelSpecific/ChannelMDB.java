package ChannelListeners.ChannelSpecific;

import Agents.Peer;
import ChannelListeners.Channel;
import Communication.Message;
import Communication.Messages.ChunkMsg;
import Communication.Messages.PutchunkMsg;
import Communication.Messages.StoredMsg;
import Utils.FileUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChannelMDB extends Channel {

    public ChannelMDB(Peer _peer, InetAddress _ip, int _port) {
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

            //Join the Multicast group.
            clientSocket.joinGroup(this.getIp());

            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                msg = msg.replace("\r\n\r\n", " ");
                String[] msg_parts = msg.split(" ");

                if (!msg_parts[0].equals("PUTCHUNK")) {
                    this.log("Invalid message in the MDB: " + msg_parts[0]);
                } else if (!msg_parts[1].equals("1.0")) {
                    this.log("What? Version " + msg_parts[1] + "?! No hablo español...");
                } else if (Integer.parseInt(msg_parts[2]) == this.getPeer().getServerID()) {
                    //this.log(msg + "\nWhat? I sent this! Ignoring..");
                } else {
                    //System.out.println("THE FUCKING SERVER THAT SENT MSG:" + msg_parts[2]);
                    //System.out.println("THIS FUCKING SERVER MSG:" + this.getPeer().getServerID());
                    this.log("Received transmition:\nServerID:    " +
                            msg_parts[2] +
                            "\nFileID:      " +
                            msg_parts[3] +
                            "\nChunkNo:     " +
                            msg_parts[4] +
                            "\nRepDegree:   " +
                            msg_parts[5] +
                            "\nBody:        " +
                            msg_parts[6]);

                    String bodyNull = msg_parts[6].replaceAll("\\x00", "");
                    byte[] body = bodyNull.getBytes();

                    // Save the chunk on a file
                    if (FileUtils.createChunk(this.getPeer().getServerID(), msg_parts[3], Integer.parseInt(msg_parts[4]), body)) {
                        // Save the chunk on the database
                        this.getPeer().stored(msg_parts[3], Integer.parseInt(msg_parts[4]), Integer.parseInt(msg_parts[5]));
                    } else {
                        System.out.println("This file was already backed up before.");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
