public class LimitOrder extends Order {
    private int quantityForTrade;

    public LimitOrder(int id, short price, int quantityForTrade, char type) {
        super(id, price, type);
        this.quantityForTrade = quantityForTrade;
    }

    @Override
    public void notifyTraded(int quantity) {
        quantityForTrade -= quantity;
    }

    @Override
    public int getTradeAmount() {
        return quantityForTrade;
    }
}
