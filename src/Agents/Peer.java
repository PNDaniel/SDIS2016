package Agents;

import ChannelListeners.Channel;
import ChannelListeners.ChannelSpecific.ChannelMC;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer {

    private int id;
    private ChannelMC channel_mc;
    private Channel channel_mdb;
    private Channel channel_mdr;

    // TODO : Arguments parsing
    public static void main(String[] args) throws UnknownHostException {
        if (
                args[0].matches("\\d*") &&
                args[1].matches("(\\d{1,3}.){3}\\d{1,3}") &&
                args[2].matches("\\d{4}") &&
                args[3].matches("(\\d{1,3}.){3}\\d{1,3}") &&
                args[4].matches("\\d{4}") &&
                args[5].matches("(\\d{1,3}.){3}\\d{1,3}") &&
                args[6].matches("\\d{4}")) {
            new Peer(
                    Integer.parseInt(args[0]),
                    args[1],
                    Integer.parseInt(args[2]),
                    args[3],
                    Integer.parseInt(args[4]),
                    args[5],
                    Integer.parseInt(args[6]));
        } else {
            System.out.println("Wrong usage: java Peer <id> <mc_ip> <mc_port> <mdb_ip> <mdb_port> <mdr_ip> <mdr_port>");
            System.exit(-1);
        }
    }

    public Peer(int _id, String ip_mc, int port_mc, String ip_mdb, int port_mdb, String ip_mdr, int port_mdr) throws UnknownHostException {

        id = _id;

        channel_mc = new ChannelMC(InetAddress.getByName(ip_mc), port_mc);
        channel_mc.start();

        channel_mdb = new Channel(InetAddress.getByName(ip_mdb), port_mdb);
        channel_mdb.start();

        channel_mdr = new Channel(InetAddress.getByName(ip_mdr), port_mdr);
        channel_mdr.start();

    }

}
