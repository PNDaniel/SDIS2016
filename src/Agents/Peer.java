package Agents;

import ChannelListeners.ChannelSpecific.ChannelMC;
import ChannelListeners.ChannelSpecific.ChannelMDB;
import ChannelListeners.ChannelSpecific.ChannelMDR;
import ChannelListeners.ChannelSpecific.ChannelOrders;
import Communication.Messages.*;
import Utils.Chunk;
import Utils.FileUtils;
import Utils.Registry;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Peer {

    private static int id;
    private static String foldername;
    private final int multicast_port = 8000;
    private final int body_limit = 64000;
    private final int trials = 5;

    private static ChannelMC channel_mc;
    private static ChannelMDB channel_mdb;
    private static ChannelMDR channel_mdr;
    private static ChannelOrders channel_orders;

    private String multicast_ip;

    private HashSet<Registry> database = new HashSet<Registry>();
    private TreeSet<Chunk> chunksToFile = new TreeSet<>();
    private String filenameToRestore;

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

    public static int getServerID() {
        return id;
    }

    public void backup(String filename, int repDeg) throws IOException {
        ArrayList<byte[]> chunkList = FileUtils.getBytesFromFile(filename);
        byte[] body;

        for (int i = 0; i < chunkList.size(); i++) {
            body = chunkList.get(i);

            String _filename = FileUtils.hashConverter(filename);

            // Add chunk to the database
            this.insert(_filename, i, repDeg);

            int j;
            for (j = 0; j < trials; j++) {

                PutchunkMsg msg = new PutchunkMsg(this.id, _filename, i, repDeg, body);
                channel_mdb.send(msg);

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
        StoredMsg msg = new StoredMsg(this.id, _fileid, _chunkN);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(401));

            channel_mc.send(msg);

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
        String _filename = FileUtils.hashConverter(filename);

        this.filenameToRestore = filename;

        for (Registry reg : database) {
            if (reg.getFileID().equals(_filename)) {
                GetchunkMsg msg = new GetchunkMsg(this.id, _filename, reg.getChunkN());
                channel_mc.send(msg);
            }
        }
    }

    public void sendChunk(String fileID, int chunkN) {
        try {
            System.out.println("SEND CHUNK!");
            Thread.sleep(ThreadLocalRandom.current().nextInt(401));
            ChunkMsg msg = new ChunkMsg(this.id, fileID, chunkN, FileUtils.sendFile("Backup_" + this.id + "/" + fileID + "_" + chunkN));
            channel_mdr.send(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addChunk(String fileID, int chunkN, byte[] body) {
        Chunk chunk = new Chunk(fileID, chunkN, body);
        this.chunksToFile.add(chunk);

        for (Chunk c : this.chunksToFile) {
            System.out.println(c.getFileID() + " - " + c.getChunkN());
        }

        Set<Integer> chunkList = new TreeSet<>();
        for (Chunk c : chunksToFile) {
            if (c.getFileID().equals(fileID)) {
                chunkList.add(c.getChunkN());
            }
        }

        int n = 0;
        for (Integer i : chunkList) {
            System.out.println(i);
            if (i == n) {
                n++;
            } else {
                break;
            }
        }

        if (n == chunkList.size()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(this.filenameToRestore);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (Chunk c : chunksToFile) {
                if (c.getFileID().equals(fileID)) {
                    try {
                        out.write(c.getBody());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            this.log("I don't have all the chunks of " + fileID);
        }
    }

    public void delete(String filename) {
        DeleteMsg msg = new DeleteMsg(this.id, FileUtils.hashConverter(filename));
        channel_mc.send(msg);
    }

    public void removeFile(String fileID) {
        ArrayList<Integer> chunks = new ArrayList<Integer>();
        chunks = FileUtils.removeFile(this.id, fileID);

        // Update database
        for (Integer i : chunks) {
            for (Registry reg : database) {
                if (reg.getFileID().equals(fileID) && reg.getChunkN() == i) {
                    reg.getServerID().remove(new Integer(this.id));
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
        // Verify how many space the chunks ocupy
        for (Registry reg : database) {
            if (FileUtils.spaceFolder(this.id) > size) {
                this.printDatabase();
                if (reg.getServerID().contains(this.id) && reg.getServerID().size() > reg.getRepDeg()) {
                    // Remove chunk
                    this.removeChunk(reg.getFileID(), reg.getChunkN());
                    // Send REMOVED message
                    RemovedMsg msg = new RemovedMsg(this.id, reg.getFileID(), reg.getChunkN());
                    channel_mc.send(msg);
                    // Remove this server from database
                    reg.getServerID().remove(this.id);
                } else {
                    this.log("Can't remove this chunk!\nFileID: " + reg.getFileID() + "\nChunkN: " + reg.getChunkN());
                }
            } else {
                break;
            }
        }
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