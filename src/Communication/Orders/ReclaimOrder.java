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
        return null;
    }

}