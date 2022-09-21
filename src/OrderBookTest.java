import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookTest {

    public BuyOrderStorage buyOrderStorage;
    public SellOrderStorage sellOrderStorage;
    OrderBook book;

    @Before
    public void setup() {
        buyOrderStorage = new BuyOrderStorage();
        sellOrderStorage = new SellOrderStorage();
        book = new OrderBook(buyOrderStorage, sellOrderStorage);
    }

    private OrderBookEntry limitEntry(int id, int price, int quantity, char type) {
        return new OrderBookEntry(new LimitOrder(id, (short) price, quantity, type));
    }

    @Test
    public void checkIcebergPassiveWithAggressiveLimitOrder() {
        var buyLimitOrder1 = limitEntry(1, 99, 50000, 'B');
        var buyLimitOrder2 = limitEntry(2, 98, 25500, 'B');

        var sellLimitOrder1 = limitEntry(3, 100, 500, 'S');
        var sellLimitOrder2 = limitEntry(4, 103, 100, 'S');
        buyOrderStorage.add(buyLimitOrder1);
        buyOrderStorage.add(buyLimitOrder2);
        sellOrderStorage.add(sellLimitOrder1);
        sellOrderStorage.add(sellLimitOrder2);

        IcebergOrder s = new IcebergOrder(5, (short) 100, 100000, 10000, 'S');
        sellOrderStorage.add(new OrderBookEntry(s));


        var data = book.addOrder(new LimitOrder(6, (short) 100, 16000, 'B'));
        assertEquals(List.of(new MatchedOrderData(6, 3, (short) 100, 500),
                new MatchedOrderData(6, 5, (short) 100, 15500)), data);
        assertEquals(4500, s.getTradeAmount());
    }

    @Test
    public void checkAggressiveIcebergOrderEntry() {
        // Arrange
        var buyLimitOrder1 = limitEntry(1, 99, 50000, 'B');
        var buyLimitOrder2 = limitEntry(2, 98, 25500, 'B');

        var sellLimitOrder1 = limitEntry(3, 100, 10000, 'S');
        var sellLimitOrder2 = limitEntry(4, 100, 7500, 'S');
        var sellLimitOrder3 = limitEntry(5, 101, 20000, 'S');

        buyOrderStorage.add(buyLimitOrder1);
        buyOrderStorage.add(buyLimitOrder2);
        sellOrderStorage.add(sellLimitOrder1);
        sellOrderStorage.add(sellLimitOrder2);
        sellOrderStorage.add(sellLimitOrder3);

        // Act
        var data = book.addOrder(new IcebergOrder(6, (short) 100, 100000, 10000, 'B'));

        // Assert
        assertEquals(List.of(new MatchedOrderData(6, 3, (short) 100, 10000),
                new MatchedOrderData(6, 4, (short) 100, 7500)), data);
    }


    @Test
    public void passiveIcebergExecution() {
        // Arrange
        IcebergOrder iceOrder1 = new IcebergOrder(1, (short) 100, 50000, 10000, 'B');
        var buyLimitOrder1 = new OrderBookEntry(iceOrder1);
        IcebergOrder iceOrder2 = new IcebergOrder(2, (short) 100, 50000, 20000, 'B');
        var buyLimitOrder2 = new OrderBookEntry(iceOrder2);
        IcebergOrder iceOrder3 = new IcebergOrder(3, (short) 100, 50000, 1000, 'B');
        var buyLimitOrder3 = new OrderBookEntry(iceOrder3);

        buyOrderStorage.add(buyLimitOrder1);
        buyOrderStorage.add(buyLimitOrder2);
        buyOrderStorage.add(buyLimitOrder3);

        // Act
        var data = book.addOrder(new LimitOrder(6, (short) 100, 35000, 'S'));

        // Assert
        assertEquals(List.of(new MatchedOrderData(1, 6, (short) 100, 14000),
                new MatchedOrderData(2, 6, (short) 100, 20000),
                new MatchedOrderData(3, 6, (short) 100, 1000)), data);

        assertEquals(6000, iceOrder1.getTradeAmount());
        assertEquals(20000, iceOrder2.getTradeAmount());
    }


}