package Communication.Orders;

import Communication.Order;

public class ReclaimOrder extends Order {

    private int size;

    public ReclaimOrder(int _size) {
        super(SubProtocol.RECLAIM);
        this.size = _size;
    }

    @Override
    public String toString() {
        String result = new String();

        result = this.getSubprotocol().toString() + " "
                + this.size
                + "\r\n\r\n";

        return result;
    }

}