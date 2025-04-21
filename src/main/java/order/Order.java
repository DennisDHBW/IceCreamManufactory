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
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 6);

        // create custom receipt
        HashMap<String, Integer> ingredientsForCustomOrder = new HashMap<>();
        ingredientsForCustomOrder.put("M001", 1);
        ingredientsForCustomOrder.put("F001", 2);
        ingredientsForCustomOrder.put("S001", 1);
        ingredientsForCustomOrder.put("S002", 1);
        ingredientsForCustomOrder.put("D001", 1);
        Receipt customizedReceipt = Receipt.getCustomReceipt(ingredientsForCustomOrder);

        // build order
        ArrayList<Receipt> receiptsForOrder = new ArrayList<>();
        receiptsForOrder.add(customizedReceipt);
        receiptsForOrder.add(customizedReceipt);

        return Order.builder()
                .id(orderId)
                .receipts(receiptsForOrder)
                .build();
    }
}