package Agents;

import ChannelListeners.ChannelSpecific.ChannelMC;
import ChannelListeners.ChannelSpecific.ChannelMDB;
import ChannelListeners.ChannelSpecific.ChannelMDR;
import ChannelListeners.ChannelSpecific.ChannelOrders;
import Communication.Messages.GetchunkMsg;
import Communication.Messages.PutchunkMsg;
import Communication.Messages.StoredMsg;
import Utils.FileUtils;
import Utils.Registry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Peer {

    private final int multicast_port = 8000;
    private final int body_limit = 64000;

    private static int id;
    private String multicast_ip;
    private ChannelOrders channel_orders;
    private static ChannelMC channel_mc;
    private static ChannelMDB channel_mdb;
    private static ChannelMDR channel_mdr;

    private ArrayList<Registry> database;

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

        this.multicast_ip = new String("224.0.224." + this.id);

        channel_orders = new ChannelOrders(this, InetAddress.getByName(this.multicast_ip), multicast_port);
        channel_orders.start();

        channel_mc = new ChannelMC(this, InetAddress.getByName(ip_mc), port_mc);
        channel_mc.start();

        channel_mdb = new ChannelMDB(this, InetAddress.getByName(ip_mdb), port_mdb);
        channel_mdb.start();

        channel_mdr = new ChannelMDR(this, InetAddress.getByName(ip_mdr), port_mdr);
        channel_mdr.start();
    }

    public void backup(String filename, int repDeg) throws IOException {
        ArrayList<byte[]> chunkList = FileUtils.getBytesFromFile(filename);
        byte[] body;

        for (int i = 0; i < chunkList.size(); i++) {
            body = chunkList.get(i);

            PutchunkMsg msg = new PutchunkMsg(this.id, FileUtils.hashConverter(filename), i, repDeg, body);
            channel_mdb.send(msg);
        }
    }

    public void stored(String _fileid, int chunkN) {
        StoredMsg msg = new StoredMsg(this.id, _fileid, chunkN);
        channel_mc.send(msg);
    }

    public void restore(String filename) {
        GetchunkMsg msg = new GetchunkMsg(this.id, FileUtils.hashConverter(filename), 0);
        channel_mc.send(msg);
    }

    public void delete(String filename) {
        System.out.println("DELETE " + filename);
    }

    public void reclaim(int size) {
        System.out.println("RECLAIM " + size);
    }

    public static int getServerID() {
        return id;
    }

    public void insert(int _serverID, String _fileID, int _chunkN) {
        database.add(new Registry(_serverID, _fileID, _chunkN));
    }

    public void select() {
        for (int i = 0; i < database.size(); i++) {
            System.out.println(database.get(i));
        }
    }

}