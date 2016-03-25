package Protocols;

public class Order {

    private String subprotocol;
    private String path;
    private int degree = 0;
    private int size = 0;

    public Order(String _subprotocol, String _path, int _degree) {
        this.subprotocol = _subprotocol.toUpperCase();
        this.path = _path.toLowerCase();
        this.degree = _degree;
    }

    public Order(String _subprotocol, String _path) {
        this.subprotocol = _subprotocol.toUpperCase();
        this.path = _path.toLowerCase();
    }

    public Order(String _subprotocol, int _size) {
        this.subprotocol = _subprotocol.toUpperCase();
        this.size = _size;
    }

    public String toString() {
        String output = new String("ORDER ");
        if (degree == 0 && size == 0) {
            output += this.subprotocol + " " + this.path + "\r\n\r\n";
        } else if (size == 0) {
            output += this.subprotocol + " " + this.path + " " + this.degree + "\r\n\r\n";
        } else if (degree == 0) {
            output += this.subprotocol + " " + this.size + "\r\n\r\n";
        } else {
            System.out.println("Wrong usage of TestApp's args.");
            System.exit(-1);
        }
        return output;
    }

}
