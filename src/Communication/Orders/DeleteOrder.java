package Communication.Orders;

import Communication.Order;

public class DeleteOrder extends Order {

    private String filename;

    public DeleteOrder(String _filename) {
        super(SubProtocol.DELETE);
        this.filename = _filename;
    }

    @Override
    public String toString() {
        return null;
    }

}