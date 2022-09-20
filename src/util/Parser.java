package util;

import orders.*;

public class Parser {
    public Order parseLine(String input) {
        // We won't check if input is null on purpose
        final var withoutSpace = input.replaceAll("\\s+", "");
        if (withoutSpace.isEmpty() || withoutSpace.charAt(0) == '#') {
            return null;
        }
        final var splitted = withoutSpace.split(",");
        var type = splitted[0].charAt(0);
        var id = Integer.parseInt(splitted[1]);
        var pricePence = Short.parseShort(splitted[2]);
        var quantity = Integer.parseInt(splitted[3]);
        if (splitted.length != 4) {
            int peakSize = Integer.parseInt(splitted[4]);
            return new IcebergOrder(id, pricePence, quantity, peakSize, type);
        } else {
            return new LimitOrder(id, pricePence, quantity, type);
        }
    }
}
