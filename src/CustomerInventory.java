import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerInventory {
    private final List<String> items = new ArrayList<>();

    public void add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        items.add(product.getName());
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }
}
