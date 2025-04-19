package order;

import customer.Customer;
import ingredient.Ingredient;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    private static int orderId = 0;
    private Customer customer;
    private ArrayList<Receipt> receipts;

    public void add(Receipt receipt) {
        this.receipts.add(receipt);
    }

    /*public Receipt customisedOrder(HashMap<Ingredient, Integer> ingredientsWithCount) {
        return receipt;
    }*/
}
