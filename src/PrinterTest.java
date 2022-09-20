// (c) GSA Capital

import orders.LimitOrder;
import org.junit.Test;
import util.Printer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrinterTest {

    @Test
    public void checkSellFormatting() {
        var printer = new Printer();
        var order = new LimitOrder(12341, (short) 32504, 1231234, 'S');

        assertEquals(
                "|          |             |       | 32,504|    1,231,234|     12341|",
                printer.formattedLineWithSell(order));
    }

    @Test
    public void checkBuyFormatting() {
        var printer = new Printer();
        var order = new LimitOrder(12341, (short) 32504, 1231234, 'B');

        assertEquals(
                "|     12341|    1,231,234| 32,504|       |             |          |",
                printer.formattedLineWithBuy(order));
    }

    @Test
    public void checkSellFormattingHasCorrectLength() {
        var printer = new Printer();
        var order = new LimitOrder(12341, (short) 32504, 1231234, 'S');

        assertEquals(67, printer.formattedLineWithSell(order).length());
    }

    @Test
    public void checkBuyFormattingHasCorrectLength() {
        var printer = new Printer();
        var order = new LimitOrder(12341, (short) 32504, 1231234, 'B');

        assertEquals(67, printer.formattedLineWithBuy(order).length());
    }
}