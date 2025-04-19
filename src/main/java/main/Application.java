package main;

/*
    Ab 20 Minuten stillstand --> Termin vereinbaren und Hilfe anfordern
    Teams mit Matrikelnummer anmelden
    Lineare Maschinen --> Auslastung auf Basis der benutzen Maschinen
 */

import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;
import ingredient.IngredientManager;
import receipt.ReceiptManager;
import production.*;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final String CSV_PATH = "src/inventory.csv";

        IngredientManager inventoryManager = IngredientManager.create(CSV_PATH);

        Machine[] stations = {
            new MixtureMachine("MIM001", "Mixxi 3000", "Bosch"),
            new PasteurisingMachine("PAM001", "Pasteuri 3000", "Bosch"),
            new FreezingMachine("FRM001", "Freezi 3000", "Bosch"),
            new PortioningMachine("POM001", "Portioni 3000", "Bosch"),
            new GarnishingMachine("GAM001", "Garni 3000", "Bosch"),
            new DispensingMachine("DIM001", "Dispensi 3000", "Bosch")
        };

        inventoryManager.importDelivery("src/inventory2.csv");
        ArrayList<String> ingredientIds = inventoryManager.getIngredientIds();
        inventoryManager.displayStockLevels();
        inventoryManager.checkExpirationIngredients(ingredientIds);

        ReceiptManager receiptManager = ReceiptManager.create("src/receipts.csv");
        ArrayList<String> receiptIds = receiptManager.getReceiptIds();
        Receipt receipt1 = receiptManager.getReceipts().get(receiptIds.getFirst());
        //Map<String, Integer> requiredIngredients = receipt1.getIngredientHashMap();
        //Stack<Ingredient> container = new Stack<Ingredient>(requiredIngredients.size());
        log.info("ende");


    }

}
