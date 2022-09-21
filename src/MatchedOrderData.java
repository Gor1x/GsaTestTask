import java.util.Objects;

public class MatchedOrderData {
    private final int buyOrderId;
    private final int sellOrderId;
    private final short pricePence;
    private final int quantity;

    public MatchedOrderData(int buyOrderId, int sellOrderId, short pricePence, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.pricePence = pricePence;
        this.quantity = quantity;
    }

    public int getBuyOrderId() {
        return buyOrderId;
    }

    public int getSellOrderId() {
        return sellOrderId;
    }

    public short getPricePence() {
        return pricePence;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchedOrderData that = (MatchedOrderData) o;
        return buyOrderId == that.buyOrderId && sellOrderId == that.sellOrderId && pricePence == that.pricePence && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyOrderId, sellOrderId, pricePence, quantity);
    }

    @Override
    public String toString() {
        return "Matched{" +
                "buyId=" + buyOrderId +
                ",sellId=" + sellOrderId +
                ",price=" + pricePence +
                ",quantity=" + quantity +
                '}';
    }
}
