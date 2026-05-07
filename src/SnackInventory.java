import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SnackInventory {
    private final List<Product> snacks;
    private static final int RESTOCK_AMOUNT = 15;

    public SnackInventory() {
        snacks = new ArrayList<>();
        loadDefaultSnacks();
    }

    private void loadDefaultSnacks() {
        snacks.add(new Product(101, "Chips", 1.50, 0));
        snacks.add(new Product(102, "Chocolate Bar", 1.25, 0));
        snacks.add(new Product(103, "Gummy Bears", 1.75, 0));
        snacks.add(new Product(104, "Pretzels", 1.40, 0));
        snacks.add(new Product(105, "Cookies", 1.95, 0));
        snacks.add(new Product(106, "Trail Mix", 2.25, 0));
        snacks.add(new Product(107, "Popcorn", 1.85, 0));
        snacks.add(new Product(108, "Granola Bar", 1.30, 0));
        snacks.add(new Product(109, "Gold Bar", 1000000, 0));
    }

    public List<Product> getAllSnacks() {
        return Collections.unmodifiableList(snacks);
    }

    public Product getSnackById(int id) {
        for (Product snack : snacks) {
            if (snack.getId() == id) {
                return snack;
            }
        }
        return null;
    }

    public void restockAll() {
        for (Product snack : snacks) {
            snack.setStock(RESTOCK_AMOUNT);
        }
    }
}
