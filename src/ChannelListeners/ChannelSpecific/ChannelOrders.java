package ChannelListeners.ChannelSpecific;

import ChannelListeners.Channel;

import java.net.InetAddress;

public class ChannelOrders extends Channel {

    public ChannelOrders(InetAddress _ip, int _port) {
        super(_ip, _port);
    }

    public void run () {
        System.out.println("Peer will be listening to orders on port " + this.getPort());
    }

}