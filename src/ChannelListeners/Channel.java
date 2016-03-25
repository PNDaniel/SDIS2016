package ChannelListeners;

import Agents.Peer;

import java.net.InetAddress;

public class Channel extends Thread {

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

}