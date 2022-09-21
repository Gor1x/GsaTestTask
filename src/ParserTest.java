import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parseCommentIsNull() {
        var parser = new Parser();
        assertNull(parser.parseLine("    #"));
    }


    @Test
    public void parseCommentIsNull2() {
        var parser = new Parser();
        assertNull(parser.parseLine("#"));
    }

    @Test
    public void parseWhitespacesIsNull() {
        var parser = new Parser();
        assertNull(parser.parseLine("         "));
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