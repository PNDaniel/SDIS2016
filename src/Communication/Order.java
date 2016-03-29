package Communication;

public abstract class Order {

    public enum SubProtocol {BACKUP, RESTORE, DELETE, RECLAIM}

    private SubProtocol subprotocol;

    public Order(SubProtocol _subprotocol) {
        this.subprotocol = _subprotocol;
    }

    public SubProtocol getSubprotocol() {
        return this.subprotocol;
    }

    @Override
    public abstract String toString();

}
