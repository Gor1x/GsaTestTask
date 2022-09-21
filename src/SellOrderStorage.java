import java.util.Comparator;

public class SellOrderStorage extends OrderStorage {

    public SellOrderStorage() {
        super(Comparator.naturalOrder());
    }
}
