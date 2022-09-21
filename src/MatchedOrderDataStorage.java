import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchedOrderDataStorage {
    private final List<Pair<Integer, Integer>> matchedPairsOrdering = new ArrayList<>();
    private final Map<Pair<Integer, Integer>, MatchedOrderData> mapWithMatchedOrders = new HashMap<>();

    public void add(int buyId, int sellId, short pricePence, int quantity) {
        var buySellPair = new Pair<>(buyId, sellId);
        if (mapWithMatchedOrders.containsKey(buySellPair)) {
            var existingInfo = mapWithMatchedOrders.get(buySellPair);
            mapWithMatchedOrders.put(buySellPair, new MatchedOrderData(buyId, sellId, pricePence, quantity + existingInfo.getQuantity()));
        } else {
            matchedPairsOrdering.add(buySellPair);
            mapWithMatchedOrders.put(buySellPair, new MatchedOrderData(buyId, sellId, pricePence, quantity));
        }
    }

    public List<MatchedOrderData> convertToList() {
        return matchedPairsOrdering.stream().map(mapWithMatchedOrders::get).collect(Collectors.toList());
    }
}
