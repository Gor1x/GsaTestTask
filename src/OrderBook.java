import java.util.List;

public class OrderBook {
    private final OrderStorage buyOrders;
    private final OrderStorage sellOrders;

    public Pair<List<Order>, List<Order>> getOrders() {
        return new Pair<>(buyOrders.getOrdersList(), sellOrders.getOrdersList());
    }

    public OrderBook(BuyOrderStorage buyOrders, SellOrderStorage sellOrders) {
        this.buyOrders = buyOrders;
        this.sellOrders = sellOrders;
    }

    public List<MatchedOrderData> addOrder(Order order) {
        var entry = new OrderBookEntry(order);
        if (order.isBuyOrder()) {
            buyOrders.add(entry);
        } else {
            sellOrders.add(entry);
        }
        var newOrderId = order.getId();
        return getMatches(newOrderId);
    }

    private List<MatchedOrderData> getMatches(int lastOrderId) {
        var storage = new MatchedOrderDataStorage();

        boolean shouldTakeSellPrice = true;
        if (buyOrders.hasEntries() && sellOrders.hasEntries()) {
            if (sellOrders.getFirst().getNumberAdded() > buyOrders.getFirst().getNumberAdded()) {
                // It means that we match new buy order with existing sell orders
                shouldTakeSellPrice = false;
            }
        }


        while (buyOrders.hasEntries() && sellOrders.hasEntries() && buyOrders.getPeakPrice() >= sellOrders.getPeakPrice()) {
            var buyOrder = buyOrders.removeFirst();
            var sellOrder = sellOrders.removeFirst();

            var buyVolume = buyOrder.getOrderData().getTradeAmount();
            var sellVolume = sellOrder.getOrderData().getTradeAmount();

            int tradeVolume = Math.min(buyVolume, sellVolume);

            buyOrder.notifyVolumeTraded(tradeVolume);
            if (buyOrder.getOrderData() instanceof IcebergOrder) {
                ((IcebergOrder) buyOrder.getOrderData()).notifyAggressiveOrder(lastOrderId);
            }

            sellOrder.notifyVolumeTraded(tradeVolume);
            if (sellOrder.getOrderData() instanceof IcebergOrder) {
                ((IcebergOrder) sellOrder.getOrderData()).notifyAggressiveOrder(lastOrderId);
            }

            short price = shouldTakeSellPrice ? sellOrder.getOrderData().getPrice() : buyOrder.getOrderData().getPrice();
            storage.add(buyOrder.getOrderData().getId(), sellOrder.getOrderData().getId(), price, tradeVolume);

            if (buyOrder.getOrderData().getTradeAmount() != 0)
                buyOrders.add(buyOrder);
            if (sellOrder.getOrderData().getTradeAmount() != 0)
                sellOrders.add(sellOrder);
        }
        return storage.convertToList();
    }

}
