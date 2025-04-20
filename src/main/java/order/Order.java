package order;

import container.Container;
import lombok.Builder;
import lombok.Data;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Builder
@Data
public class Order {
    private String id;
    private ArrayList<Receipt> receipts;
    @Builder.Default private ArrayList<Container> containers = new ArrayList<>();

    public Order(String id, ArrayList<Receipt> receipts, ArrayList<Container> containers) {
        this.id = id;
        this.receipts = receipts;
        this.containers = containers;
    }

    public void add(Receipt receipt) {
        this.receipts.add(receipt);
    }

    public static Order generateSampleOrder() {
        // Generate a random order ID
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 6);

        // Create custom receipt with ingredients
        HashMap<String, Integer> ingredientsForCustomOrder = new HashMap<>();
        ingredientsForCustomOrder.put("M001", 1); // Milk base
        ingredientsForCustomOrder.put("F001", 2); // Fruit ingredient
        ingredientsForCustomOrder.put("S001", 1); // Sauce
        ingredientsForCustomOrder.put("S002", 1); // Another sauce
        ingredientsForCustomOrder.put("D001", 1); // Decoration
        Receipt customizedReceipt = Receipt.getCustomReceipt(ingredientsForCustomOrder);

        // build order
        ArrayList<Receipt> receiptsForOrder = new ArrayList<>();
        receiptsForOrder.add(customizedReceipt);

        return Order.builder()
                .id(orderId)
                .receipts(receiptsForOrder)
                .build();
    }
}