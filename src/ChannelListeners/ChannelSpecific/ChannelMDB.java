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
import java.util.concurrent.ThreadLocalRandom;

public class ChannelMDB extends Channel {

    public ChannelMDB(Peer _peer, InetAddress _ip, int _port) {
        super(_peer, _ip, _port);
    }

    public void run() {
        System.out.println("Channel MDB listening on " + this.getIp() + ":" + this.getPort());

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
                //System.out.println("MDB - Message received: " + msg);

                //retirar null (delete)
                msg = msg.replace("\r\n\r\n", " ");
                String[] msg_parts = msg.split(" ");
                // Ignore message here
                if (!msg_parts[0].equals("PUTCHUNK")) {
                    System.out.println("Received a message other than PUTCHUNK on MDB.");
                } else if (!msg_parts[1].equals("1.0")) {
                    System.out.println("Received a message with version higher than v1.0. No hablo v" + msg_parts[1]);
                } else if (Integer.parseInt(msg_parts[2]) == this.getPeer().getServerID()) {
                    System.out.println("Message from same computer. Ignoring...");
                } else {
                    System.out.println("MDB - Message received: " + msg);

                    System.out.println("ServerID: " + msg_parts[2]);
                    System.out.println("FileID: " + msg_parts[3]);
                    System.out.println("ChunkNo: " + msg_parts[4]);
                    System.out.println("RepDegree: " + msg_parts[5]);
                    System.out.println("Body: " + msg_parts[6]);
                    byte[] body = msg_parts[6].getBytes();
                    //System.out.println("Body 2: " + new String(body));
                    FileUtils.createChunk(msg_parts[3], Integer.parseInt(msg_parts[4]), msg_parts[6].getBytes());

                    StoredMsg msgStored = new StoredMsg(this.getPeer().getServerID(), msg_parts[3], Integer.parseInt(msg_parts[4]));
                    /*
                    https://examples.javacodegeeks.com/core-java/util/concurrent/threadlocalrandom/java-util-concurrent-threadlocalrandom-example/
                     Random delay between 0 and 400ms
                    */
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(401));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Peer.getChannelMC().send(msgStored);
                   /* if(!(Integer.parseInt(msg_parts[2]) == this.getPeer().getServerID()))
                    {
                        System.out.println("From msg: " + msg_parts[2]);
                        System.out.println("This Server ID: " + this.getPeer().getServerID());

                    }*/
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void send(PutchunkMsg msg) {

        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(this.getPort())) {

            //Join the Multicast group.
            serverSocket.joinGroup(this.getIp());

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(msg.toString().getBytes(), msg.toString().getBytes().length, this.getIp(), this.getPort());
            serverSocket.send(msgPacket);

            System.out.println("Sent over MDB: " + msg.toString());
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
