import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class OrderStorage {
    private final TreeSet<OrderBookEntry> orders;

    public List<Order> getOrdersList() {
        List<Order> list = new ArrayList<>();
        for (OrderBookEntry order : orders) {
            Order orderData = order.getOrderData();
            list.add(orderData);
        }
        return list;
    }

    public OrderStorage(Comparator<Short> priceComparator) {
        Comparator<OrderBookEntry> buyOrderBookEntryComparator = (entry1, entry2) -> {
            short price1 = entry1.getOrderData().getPrice();
            short price2 = entry2.getOrderData().getPrice();
            if (price1 != price2) {
                return priceComparator.compare(price1, price2);
            }
            return Integer.compare(entry1.getNumberAdded(), entry2.getNumberAdded());
        };
        orders = new TreeSet<>(buyOrderBookEntryComparator);
    }


    public void add(OrderBookEntry entry) {
        orders.add(entry);
    }

    public short getPeakPrice() {
        return orders.first().getOrderData().getPrice();
    }

    public OrderBookEntry removeFirst() {
        return orders.pollFirst();
    }

    public OrderBookEntry getFirst() {
        return orders.first();
    }

    public boolean hasEntries() {
        return !orders.isEmpty();
    }
}
