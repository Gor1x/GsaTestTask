import java.text.NumberFormat;
import java.util.Locale;

public class Printer {
    public void print(MatchedOrderData data) {
        System.out.printf("%d,%d,%d,%d%n",
                data.getBuyOrderId(),
                data.getSellOrderId(),
                data.getPricePence(),
                data.getQuantity());
    }

    public void printOrders(OrderBook orderBook) {
        printHeader();
        var orders = orderBook.getOrders();
        var buyOrders = orders.getFirst();
        var sellOrders = orders.getSecond();
        int maxLength = Math.max(buyOrders.size(), sellOrders.size());
        for (int i = 0; i < maxLength; i++) {
            if (i < buyOrders.size() && i < sellOrders.size()) {
                System.out.println(formattedLineWithBuySell(buyOrders.get(i), sellOrders.get(i)));
            } else if (i < buyOrders.size()) {
                System.out.println(formattedLineWithBuy(buyOrders.get(i)));
            } else {
                System.out.println(formattedLineWithSell(sellOrders.get(i)));
            }
        }
        printBorderLine();
    }

    public String formattedLineWithSell(Order sellOrder) {
        return String.format("|          |             |       |%7s|%13s|%10d|",
                withCommas(sellOrder.getPrice()),
                withCommas(sellOrder.getTradeAmount()),
                sellOrder.getId());
    }

    public String formattedLineWithBuy(Order buyOrder) {
        return String.format("|%10d|%13s|%7s|       |             |          |",
                buyOrder.getId(),
                withCommas(buyOrder.getTradeAmount()),
                withCommas(buyOrder.getPrice()));
    }

    private void printEmptyLine() {
        System.out.println("+----------+-------------+-------+-------+-------------+----------+");
    }

    private void printBorderLine() {
        System.out.println("+-----------------------------------------------------------------+");
    }

    private String withCommas(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public String formattedLineWithBuySell(Order buyOrder, Order sellOrder) {
        return String.format("|%10d|%13s|%7s|%7s|%13s|%10d|",
                buyOrder.getId(),
                withCommas(buyOrder.getTradeAmount()),
                withCommas(buyOrder.getPrice()),
                withCommas(sellOrder.getPrice()),
                withCommas(sellOrder.getTradeAmount()),
                sellOrder.getId());
    }

    public void printHeader() {
        printBorderLine();
        System.out.println(""
                .concat("| BUY                            | SELL                           |\n")
                .concat("| Id       | Volume      | Price | Price | Volume      | Id       |"));
        printEmptyLine();
    }
}
