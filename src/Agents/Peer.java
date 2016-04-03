package Agents;

import ChannelListeners.ChannelSpecific.ChannelMC;
import ChannelListeners.ChannelSpecific.ChannelMDB;
import ChannelListeners.ChannelSpecific.ChannelMDR;
import ChannelListeners.ChannelSpecific.ChannelOrders;
import Communication.Messages.*;
import Utils.FileUtils;
import Utils.Registry;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Peer {

    private final int multicast_port = 8000;
    private final int body_limit = 64000;
    private final int trials = 5;

    private static int id;
    private static String foldername;
    private String multicast_ip;
    private ChannelOrders channel_orders;
    private static ChannelMC channel_mc;
    private static ChannelMDB channel_mdb;
    private static ChannelMDR channel_mdr;

    private HashSet<Registry> database = new HashSet<Registry>();

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

        this.id = _id;

        FileUtils.createFolder(this.id);

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
            //System.out.println("le address:" + body);
            //System.out.println("FUCKING BODY:" + new String(body));
            String _filename = FileUtils.hashConverter(filename);

            // Add chunk to the database
            this.insert(_filename, i, repDeg);

            int j;
            for (j = 0; j < trials; j++) {

                //PutchunkMsg msg = new PutchunkMsg(this.id, _filename, i, repDeg, body);
                //channel_mdb.send(msg);
                //System.out.println("Server FUCKING ID: " + this.id);
                String msg = "PUTCHUNK" + " " + "1.0" + " " + this.id + " " + _filename + " " + i + " " + repDeg + "\r\n\r\n";

                byte[] temp1 = msg.getBytes();

                byte[] last = new byte[temp1.length + body.length];
                System.arraycopy(temp1, 0, last, 0, temp1.length);
                System.arraycopy(body, 0, last, temp1.length, body.length);

                channel_mdb.send1(last);

                try {
                    Thread.sleep(1000);
                    for (Registry reg : database) {
                        if (reg.getFileID().equals(_filename) && reg.getChunkN() == i) {
                            if (reg.isBackedup()) {
                                j = trials;
                                this.log("The chunk was 100% backup. Check the database!");
                                this.printDatabase();
                            } else {
                                this.log("The following Chunk is on its trial #" + (j + 1) + ":\n" + reg.toString());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (j == trials) {
                this.log("Maximum trials reached. The chunk was not 100% backup. Check the database!");
                this.printDatabase();
            }
        }
    }

    public void stored(String _fileid, int _chunkN, int _repDeg) {
        // Send stored message
        //StoredMsg msg = new StoredMsg(this.id, _fileid, _chunkN);
        try {
            String msg = "STORED" + " " + 1.0 + " " + this.id + " " + _fileid + " " + _chunkN + " " + _repDeg + "\r\n\r\n";
            channel_mc.send1(msg.getBytes());

            Thread.sleep(ThreadLocalRandom.current().nextInt(401));
            //channel_mc.send(msg);

            // Add chunk to the database
            this.insert(_fileid, _chunkN, _repDeg);

            // Add my server to the list of servers of this chunk
            this.register(this.getServerID(), _fileid, _chunkN);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(String _fileID, int _chunkN, int _repDeg) {
        int n = 0;
        for (Registry reg : database) {
            if (reg.getFileID().equals(_fileID) && reg.getChunkN() == _chunkN) {
                reg.setRepDeg(_repDeg);
            } else {
                n++;
            }
        }
        if (n == database.size()) {
            database.add(new Registry(_fileID, _chunkN, _repDeg));
        }
    }

    public void register(int _serverID, String _fileID, int _chunkN) {
        int n = 0;
        for (Registry reg : database) {
            if (reg.getFileID().equals(_fileID) && reg.getChunkN() == _chunkN) {
                reg.addServerID(_serverID);
            } else {
                n++;
            }
        }
        if (n == database.size()) {
            database.add(new Registry(_fileID, _chunkN));
            for (Registry reg : database) {
                if (reg.getFileID().equals(_fileID) && reg.getChunkN() == _chunkN) {
                    reg.addServerID(_serverID);
                }
            }
        }
    }

    public void restore(String filename) {
        int chunkNo = 0;

        //GetchunkMsg msg = new GetchunkMsg(this.id, FileUtils.hashConverter(filename), chunkNo);
        //channel_mc.send(msg);
        String msg = "GETCHUNK" + " " + 1.0 + " " + this.id + " " + filename + " " + chunkNo + " " + "\r\n\r\n";
        channel_mc.send1(msg.getBytes());
    }

    public void searchChunk(String fileID, int chunkNo) {
        File folder = new File(System.getProperty("user.dir"));
        File[] listOfFiles = folder.listFiles();
        String chunk = fileID + "_" + chunkNo;
        byte[] body = null;
        try {
            body = FileUtils.sendFile(chunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().equals(chunk)) {
                ChunkMsg msg = new ChunkMsg(this.id, fileID, chunkNo, body);
                //channel_mdr.send(msg);
                break;
            }
        }
    }

    public void delete(String filename) {
        DeleteMsg msg = new DeleteMsg(this.id, FileUtils.hashConverter(filename));
        //channel_mc.send(msg);
    }

    public void removeFile(String fileID) {
        ArrayList<Integer> chunks = new ArrayList<Integer>();
        chunks = FileUtils.removeFile(this.id, fileID);

        // Update database
        for (Integer i : chunks) {
            for (Registry reg : database) {
                if (reg.getFileID().equals(fileID) && reg.getChunkN() == i) {
                    reg.getServerID().remove(this.id);
                }
            }
        }

        this.log("Removed chunks " + chunks + " that belong to " + fileID);
    }

    public void deleteReg(String fileID, int chunkN, int serverID) {
        // Update database
        for (Registry reg : database) {
            if (reg.getFileID().equals(fileID) && reg.getChunkN() == chunkN) {
                reg.getServerID().remove(new Integer(serverID));
                break;
            }
        }
    }

    public void removeChunk(String fileID, int chunkN) {
        FileUtils.removeChunk(this.id, fileID, chunkN);
        this.log("Removed chunk " + chunkN + " that belong to " + fileID);
    }

    public void reclaim(int size) {
        System.out.println("RECLAIM " + size + "\n" + FileUtils.spaceFolder(this.id));
        // Verify how many space the chunks ocupy
        for (Registry reg : database) {
            if (FileUtils.spaceFolder(this.id) > size) {
                this.printDatabase();
                if (reg.getServerID().contains(this.id) && reg.getServerID().size() > reg.getRepDeg()) {
                    // Remove chunk
                    this.removeChunk(reg.getFileID(), reg.getChunkN());
                    // Send REMOVED message
                    //RemovedMsg msg = new RemovedMsg(this.id, reg.getFileID(), reg.getChunkN());
                    //channel_mc.send(msg);
                    // Remove this server from database
                    reg.getServerID().remove(this.id);
                }
            } else {
                break;
            }
        }
    }

    public static int getServerID() {
        return id;
    }

    public void printDatabase() {
        System.out.println("▼ ----------------------------------- ▼\n" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " - Database:");
        for (Registry reg : database) {
            System.out.println(reg);
        }
        System.out.println("▲ ----------------------------------- ▲");
    }

    public void log(String _log) {
        System.out.println("▼ ----------------------------------- ▼\n" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " - Peer #" +
                this.id +
                " says:\n" +
                _log +
                "\n▲ ----------------------------------- ▲");
    }

}