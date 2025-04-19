package order;

import customer.Customer;
import ingredient.Ingredient;
import lombok.Builder;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.HashMap;

@Builder
public class Order {
    private static int orderId = 0;
    private static int customOrderId = 0;
    private ArrayList<Receipt> receipts;


    // !! PREIS VARIABEL BERECHNET !!

    public static Receipt getCustomReceipt(HashMap<String, Integer> ingredientsWithCount) {
        Receipt receipt = Receipt.builder()
                .id("CUS" + String.format("%03d", ++customOrderId))
                .name("Customised Order")
                .price(5.00)
                .ingredients(ingredientsWithCount)
                .build();
        return receipt;
    }

    public void add(Receipt receipt) {
        this.receipts.add(receipt);
    }


}
