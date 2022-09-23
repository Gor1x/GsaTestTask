public class OrderBookEntry {
    // To simplify an implementation of this task I won't use real timestamps
    private static int TIME_VALUE = 0;

    private int numberAdded;
    private final Order orderData;

    public OrderBookEntry(Order orderData) {
        this.numberAdded = getNextTimeValue();
        this.orderData = orderData;
    }

    public int getNumberAdded() {
        return numberAdded;
    }

    public Order getOrderData() {
        return orderData;
    }

    public void notifyVolumeTraded(int tradeVolume) {
        orderData.notifyTraded(tradeVolume);
        if (orderData instanceof IcebergOrder asIcebergOrder) {
            if (orderData.getTradeAmount() == 0 && asIcebergOrder.hasHiddenVolume()) {
                asIcebergOrder.refreshPeakSafe();
                numberAdded = getNextTimeValue();
            }
        }
    }

    private static int getNextTimeValue() {
        return ++TIME_VALUE;
    }
}


