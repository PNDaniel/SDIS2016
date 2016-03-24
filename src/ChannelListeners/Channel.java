package ChannelListeners;

import java.net.InetAddress;

public class Channel extends Thread {

    private InetAddress ip;
    private int port;

    public Channel(InetAddress _ip, int _port) {
        this.ip = _ip;
        this.port = _port;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

}