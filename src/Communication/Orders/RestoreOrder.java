package Communication.Orders;

import Communication.Order;

public class RestoreOrder extends Order {

    private String filename;

    public RestoreOrder(String _filename) {
        super(SubProtocol.RESTORE);
        this.filename = _filename;
    }

    @Override
    public String toString() {
        String result = new String();

        result = this.getSubprotocol().toString() + " "
                + this.filename
                + "\r\n\r\n";

        return result;
    }

}