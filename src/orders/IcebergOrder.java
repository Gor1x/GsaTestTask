package orders;

public class IcebergOrder extends Order {
    private final int peakSize;
    private int hiddenVolume;
    private int quantityForTrade;

    public IcebergOrder(int id, short price, int totalVolume, int peakSize, char type) {
        super(id, price, type);
        this.peakSize = peakSize;
        this.quantityForTrade = peakSize;
        this.hiddenVolume = totalVolume - this.quantityForTrade;
    }

    @Override
    public void notifyTraded(int quantity) {
        if (quantity > quantityForTrade) {
            throw new IllegalArgumentException("Trade quantity" + quantity + " can't be bigger than existing quantity" + quantityForTrade);
        }
        quantityForTrade -= quantity;
    }

    public void refreshPeak() {
        if (getTradeAmount() != 0) {
            // This exception is left for testing purposes. It should never be called during real run
            throw new IllegalStateException("Refresh shouldn't be called when trade amount is not empty");
        }
        if (hiddenVolume == 0) {
            // This exception is left for testing purposes. It should never be called during real run
            throw new IllegalStateException("Hidden volume can't be empty while refreshing peak");
        }
        quantityForTrade = Math.min(peakSize, hiddenVolume);
        hiddenVolume -= quantityForTrade;
    }

    public boolean hasHiddenVolume() {
        return hiddenVolume > 0;
    }

    @Override
    public int getTradeAmount() {
        return quantityForTrade;
    }
}
