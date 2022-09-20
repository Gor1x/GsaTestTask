package orders;

public abstract class Order {
    private final int id;
    private final short price;

    private final char type;

    public Order(int id, short price, char type) {
        this.id = id;
        this.price = price;
        this.type = type;
    }

    public boolean isBuyOrder() {
        return type == 'B';
    }

    public short getPrice() {
        return price;
    }

    public abstract void notifyTraded(int quantity);

    public abstract int getTradeAmount();

    public int getId() {
        return id;
    }
}
