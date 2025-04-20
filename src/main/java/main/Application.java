package main;

/*
    Ab 20 Minuten stillstand --> Termin vereinbaren und Hilfe anfordern
    Teams mit Matrikelnummer anmelden
    Lineare Maschinen --> Auslastung auf Basis der benutzen Maschinen
 */

import lombok.extern.slf4j.Slf4j;
import order.Order;
import receipt.Receipt;
import ingredient.IngredientManager;
import receipt.ReceiptManager;
import production.*;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final String CSV_PATH = "src/inventory.csv";

        IngredientManager ingredientManager = IngredientManager.create(CSV_PATH);
        ReceiptManager receiptManager = ReceiptManager.create("src/receipts.csv");

        Machine[] stations = {
            new MixtureMachine("MIM001", "Mixxi 3000", "Bosch"),
            new PasteurisingMachine("PAM001", "Pasteuri 3000", "Bosch"),
            new FreezingMachine("FRM001", "Freezi 3000", "Bosch"),
            new PortioningMachine("POM001", "Portioni 3000", "Bosch"),
            new GarnishingMachine("GAM001", "Garni 3000", "Bosch"),
            new DispensingMachine("DIM001", "Dispensi 3000", "Bosch")
        };

        ingredientManager.importDelivery("src/inventory2.csv");
        ArrayList<String> ingredientIds = ingredientManager.getIngredientIds();
        ingredientManager.displayStockLevels();
        ingredientManager.checkExpirationIngredients(ingredientIds);

        ArrayList<String> receiptIds = receiptManager.getReceiptIds();

        // customized receipt 1
        HashMap<String, Integer> ingredientsForCustomOrder1 = new HashMap<>();
        ingredientsForCustomOrder1.put("M001", 1);
        ingredientsForCustomOrder1.put("F001", 2);
        ingredientsForCustomOrder1.put("S001", 3);
        ingredientsForCustomOrder1.put("S002", 4);
        Receipt customizedReceipt1 = Receipt.getCustomReceipt(ingredientsForCustomOrder1);

        // customized receipt 2
        HashMap<String, Integer> ingredientsForCustomOrder2 = new HashMap<>();
        ingredientsForCustomOrder2.put("M001", 5);
        ingredientsForCustomOrder2.put("D001", 6);
        ingredientsForCustomOrder2.put("V001", 7);
        ingredientsForCustomOrder2.put("E001", 8);
        Receipt customizedReceipt2 = Receipt.getCustomReceipt(ingredientsForCustomOrder2);

        // build order
        Receipt receiptHazelnutIceCream = receiptManager.getReceipts().get(receiptIds.getFirst());
        ArrayList<Receipt> receiptsForOrder = new ArrayList<>();
        receiptsForOrder.add(receiptHazelnutIceCream);
        receiptsForOrder.add(customizedReceipt1);
        receiptsForOrder.add(customizedReceipt2);
        Order order1 = Order.builder()
                .receipts(receiptsForOrder)
                .build();

        // produce order
        MixtureMachine mixture = new MixtureMachine("MIM001", "Mixxi 3000", "Bosch");
        mixture.startOrder(order1, ingredientManager);


        log.info("end");

    }

}
