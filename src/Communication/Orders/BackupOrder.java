package Communication.Orders;

import Communication.Order;

public class BackupOrder extends Order {

    private String filename;
    private int deg;

    public BackupOrder(String _filename, int _deg) {
        super(SubProtocol.BACKUP);
        this.filename = _filename;
        this.deg = _deg;
    }

    @Override
    public String toString() {
        String result = new String();

        result = this.getSubprotocol().toString() + " "
                + this.filename + " "
                + this.deg;

        return result;
    }

}