import orderbook.BuyOrderStorage;
import orderbook.OrderBook;
import orderbook.SellOrderStorage;
import util.Parser;
import util.Printer;

import java.util.Scanner;

public class SETSOrderBookExercise {
    public static void main(String[] args) {
        final var buyOrders = new BuyOrderStorage();
        final var sellOrders = new SellOrderStorage();

        final var parser = new Parser();
        final var orderBook = new OrderBook(buyOrders, sellOrders);

        final var printer = new Printer();

        Scanner consoleReader = new Scanner(System.in);
        while (consoleReader.hasNextLine()) {
            var inputLine = consoleReader.nextLine();
            var order = parser.parseLine(inputLine);
            if (order == null) continue;
            var matches = orderBook.addOrder(order);
            matches.forEach(printer::print);

            printer.printOrders(orderBook);

        }
    }
}