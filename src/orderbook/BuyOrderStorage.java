package orderbook;

import java.util.Comparator;

public class BuyOrderStorage extends OrderStorage {
    public BuyOrderStorage() {
        super(Comparator.reverseOrder());
    }
}
