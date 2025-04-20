// !! PREIS VARIABEL BERECHNET !!

package order;

import container.ContainerType;
import lombok.Builder;
import lombok.Data;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.HashMap;

@Builder
@Data
public class Order {
    private static int orderId = 0;
    private ArrayList<Receipt> receipts;

    public void add(Receipt receipt) {
        this.receipts.add(receipt);
    }

    public static Order generateSampleOrder() {
        ArrayList<Order> orders = new ArrayList<>();

        // customized receipt 1
        HashMap<String, Integer> ingredientsForCustomOrder1 = new HashMap<>();
        ingredientsForCustomOrder1.put("M001", 1);
        ingredientsForCustomOrder1.put("F001", 2);
        ingredientsForCustomOrder1.put("S001", 3);
        ingredientsForCustomOrder1.put("S002", 4);
        Receipt customizedReceipt1 = Receipt.getCustomReceipt(ingredientsForCustomOrder1);

        // build order
        ArrayList<Receipt> receiptsForOrder = new ArrayList<>();
        receiptsForOrder.add(customizedReceipt1);
        Order order = Order.builder()
                .receipts(receiptsForOrder)
                .build();
        return order;
    }
}
