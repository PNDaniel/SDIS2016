package ChannelListeners.ChannelSpecific;

import Agents.Peer;
import ChannelListeners.Channel;

import java.net.InetAddress;

public class ChannelOrders extends Channel {

    public ChannelOrders(Peer _peer, InetAddress _ip, int _port) {
        super(_peer, _ip, _port);
    }

    public void run () {
        System.out.println("Peer will be listening to orders on port " + this.getPort());
    }

}