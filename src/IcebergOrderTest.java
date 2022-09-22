import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IcebergOrderTest {

    @Test
    public void simpleSetupCheck() {
        var order = new IcebergOrder(13, (short) 27, 16, 10, 'B');

        assertEquals(order.getId(), 13);
        assertEquals(order.getPrice(), 27);
        assertTrue(order.isBuyOrder());
        assertEquals(10, order.getTradeAmount());
    }


    @Test
    public void checkTradingWithoutNewPeakCorrect() {
        var order = new IcebergOrder(13, (short) 27, 16, 10, 'B');

        assertEquals(order.getTradeAmount(), 10);
        order.notifyTraded(7);
        assertEquals(order.getTradeAmount(), 3);

    }

    @Test
    public void checkNewPeakArrangementDoesntWorkWhenTradeAmountIsNotEmpty() {
        var order = new IcebergOrder(13, (short) 27, 16, 10, 'B');

        assertEquals(order.getTradeAmount(), 10);
        order.notifyTraded(7);
        assertEquals(order.getTradeAmount(), 3);
        var exception = assertThrows(IllegalStateException.class, order::refreshPeak);
        assertEquals(exception.getMessage(), "Refresh shouldn't be called when trade amount is not empty");
    }


    @Test
    public void checkTradingWithNewPeakCorrect() {
        var order = new IcebergOrder(13, (short) 27, 16, 10, 'B');

        order.notifyTraded(10);
        order.refreshPeak();

        assertFalse(order.hasHiddenVolume());
        assertEquals(order.getTradeAmount(), 6);
    }

    @Test
    public void testCantRefreshPeakWithEmptyAmount() {
        var order = new IcebergOrder(13, (short) 27, 10, 10, 'B');

        order.notifyTraded(10);
        var exception = assertThrows(IllegalStateException.class, order::refreshPeak);
        assertEquals(exception.getMessage(), "Hidden volume can't be empty while refreshing peak");
    }

    @Test
    public void testCantCallNotifyWithToBigQuantity() {
        var order = new IcebergOrder(13, (short) 27, 10, 10, 'B');

        var exception = assertThrows(IllegalArgumentException.class, () -> order.notifyTraded(13));
        assertEquals(exception.getMessage(), "Trade quantity" + 13 + " can't be bigger than existing quantity" + 10);
    }

    @Test
    public void testMoreThanOnePeakRefreshing() {
        var order = new IcebergOrder(13, (short) 27, 17, 8, 'B');

        order.notifyTraded(8);
        order.refreshPeak();
        order.notifyTraded(8);
        order.refreshPeak();
        assertEquals(1, order.getTradeAmount());
    }

    @Test
    public void parseBuyLimitOrder() {
        var parser = new Parser();
        var order = parser.parseLine("B,1,135,16");

        assertNotNull(order);
        assertTrue(order instanceof LimitOrder);
        assertTrue(order.isBuyOrder());
        assertEquals(1, order.getId());
        assertEquals(135, order.getPrice());
        assertEquals(16, order.getTradeAmount());
    }

    @Test
    public void parseSellLimitOrder() {
        var parser = new Parser();
        var order = parser.parseLine("S,1,135,16");

        assertNotNull(order);
        assertTrue(order instanceof LimitOrder);
        assertFalse(order.isBuyOrder());
        assertEquals(1, order.getId());
        assertEquals(135, order.getPrice());
        assertEquals(16, order.getTradeAmount());
    }

    @Test
    public void parseSellIcebergOrder() {
        var parser = new Parser();
        var order = parser.parseLine("S,1,135,16,10");

        assertNotNull(order);
        assertTrue(order instanceof IcebergOrder);
        assertFalse(order.isBuyOrder());
        assertEquals(1, order.getId());
        assertEquals(135, order.getPrice());
        assertEquals(10, order.getTradeAmount());
        IcebergOrder order1 = (IcebergOrder) order;
        assertTrue(order1.hasHiddenVolume());
    }

    @Test
    public void parseBuyIcebergOrder() {
        var parser = new Parser();
        var order = parser.parseLine("B,1,135,16,10");

        assertNotNull(order);
        assertTrue(order instanceof IcebergOrder);
        assertTrue(order.isBuyOrder());
        assertEquals(1, order.getId());
        assertEquals(135, order.getPrice());
        assertEquals(10, order.getTradeAmount());
        IcebergOrder order1 = (IcebergOrder) order;
        assertTrue(order1.hasHiddenVolume());
    }

}