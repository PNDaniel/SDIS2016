package ChannelListeners;

import Agents.Peer;
import Communication.Message;
import Communication.Messages.PutchunkMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Channel extends Thread {

    private Peer peer;
    private InetAddress ip;
    private int port;

    public Channel(Peer _peer, InetAddress _ip, int _port) {
        this.peer = _peer;
        this.ip = _ip;
        this.port = _port;
    }

    public Peer getPeer() {
        return this.peer;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void send(Message msg) {
        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(this.getPort())) {

            //Join the Multicast group.
            serverSocket.joinGroup(this.getIp());

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            System.out.println("Converting to string " + msg.toByte().toString());
            System.out.println("Converting to bytes " + msg.toString().getBytes());
            //DatagramPacket msgPacket = new DatagramPacket(msg.toByte(), msg.toByte().length, this.getIp(), this.getPort());
            DatagramPacket msgPacket = new DatagramPacket(msg.toByte(), msg.toByte().length, this.getIp(), this.getPort());
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

   /* public void send1(String msg) {
        // Open a new DatagramSocket, which will be used to send the data.
        try (MulticastSocket serverSocket = new MulticastSocket(this.getPort())) {

            //Join the Multicast group.
            serverSocket.joinGroup(this.getIp());

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            System.out.println("Converting to string " + msg);
            System.out.println("Converting to bytes " + msg.getBytes());
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.toString().getBytes().length, this.getIp(), this.getPort());
            serverSocket.send(msgPacket);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/

    public void log(String _log) {
        System.out.println("▼ ----------------------------------- ▼\n" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " - " +
                this.getClass().getSimpleName() +
                " says:\n" +
                _log +
                "\n▲ ----------------------------------- ▲");
    }

}