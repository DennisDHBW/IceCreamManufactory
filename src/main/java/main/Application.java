package main;

import lombok.extern.slf4j.Slf4j;
import order.Order;
import receipt.Receipt;
import ingredient.IngredientManager;
import receipt.ReceiptManager;
import production.*;
import customer.CustomerManager;
import customer.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final String INVENTORY_CSV_PATH = "src/inventory.csv";
        final String RECEIPTS_CSV_PATH = "src/receipts.csv";
        final String DELIVERY_CSV_PATH = "src/inventory2.csv";
        final String INVENTORY_BACKUP_CSV = "src/inventory_backup.csv";
        final int CUSTOMER_COUNT = 5;

        // Initialize ingredient and recipe management
        IngredientManager ingredientManager = IngredientManager.create(INVENTORY_CSV_PATH);
        ReceiptManager receiptManager = ReceiptManager.create(RECEIPTS_CSV_PATH);

        // Create production line with fixed stations
        Machine[] productionLine = {
            new MixtureMachine("MIM001", "Mixxi 3000", "Bosch"),
            new PasteurisingMachine("PAM001", "Pasteuri 3000", "Bosch"),
            new FreezingMachine("FRM001", "Freezi 3000", "Bosch"),
            new PortioningMachine("POM001", "Portioni 3000", "Bosch"),
            new GarnishingMachine("GAM001", "Garni 3000", "Bosch"),
            new DispensingMachine("DIM001", "Dispensi 3000", "Bosch")
        };

        // Import new inventory delivery
        ingredientManager.importDelivery(DELIVERY_CSV_PATH);
        
        // Display and check inventory
        ArrayList<String> ingredientIds = ingredientManager.getIngredientIds();
        ingredientManager.displayStockLevels();
        ingredientManager.checkExpirationIngredients();

        // Get available recipes
        ArrayList<String> receiptIds = receiptManager.getReceiptIds();
        log.info("Available recipes: {}", receiptManager.getReceipts().size());

        // Create customer queue
        CustomerManager customerManager = new CustomerManager(ingredientIds, CUSTOMER_COUNT);
        log.info("Customer queue size: {}", customerManager.getQueueSize());

        // Process customers from queue
        Customer customer;
        while ((customer = customerManager.getNextCustomer()) != null) {
            log.info("Processing customer: {}", customer.getName());
            Order customerOrder = customer.getOrder();
            
            // Process the order through each machine in the production line
            for (Machine machine : productionLine) {
                log.info("==== STARTING {} ====", machine.getClass().getSimpleName().toUpperCase());
                machine.startOrder(customerOrder, ingredientManager);
            }
        }

        // Save updated inventory status
        ingredientManager.saveInventoryToCSV(INVENTORY_BACKUP_CSV);
        log.info("Ice cream production completed");
    }
}
