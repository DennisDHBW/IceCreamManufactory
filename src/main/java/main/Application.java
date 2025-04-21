package main;

import lombok.extern.slf4j.Slf4j;
import order.Order;
import ingredient.IngredientManager;
import receipt.ReceiptManager;
import production.*;
import customer.CustomerManager;
import customer.Customer;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final String INVENTORY_CSV_PATH = "src/inventory.csv";
        final String RECEIPTS_CSV_PATH = "src/receipts.csv";
        final String DELIVERY_CSV_PATH = "src/inventory_delivery.csv";
        final String INVENTORY_BACKUP_CSV = "src/inventory_backup.csv";
        final int CUSTOMER_COUNT = 5;

        log.info("===== STARTING ICE CREAM PRODUCTION SYSTEM =====");

        // initialize ingredient and recipe management
        log.info("Loading inventory from {}", INVENTORY_CSV_PATH);
        IngredientManager ingredientManager = IngredientManager.create(INVENTORY_CSV_PATH);

        log.info("Loading recipes from {}", RECEIPTS_CSV_PATH);
        ReceiptManager receiptManager = ReceiptManager.create(RECEIPTS_CSV_PATH);

        // create production line
        log.info("Setting up production line machines");
        Machine[] productionLine = {
                new MixtureMachine("MIX001", "Mixxi 3000", "Bosch"),
                new PasteurisingMachine("PAS001", "Pasteuri 3000", "Bosch"),
                new FreezingMachine("FRZ001", "Freezi 3000", "Bosch"),
                new PortioningMachine("POR001", "Portioni 3000", "Bosch"),
                new GarnishingMachine("GAR001", "Garni 3000", "Bosch"),
                new DispensingMachine("DIS001", "Dispensi 3000", "Bosch")
        };

        // import delivery
        log.info("Processing new inventory delivery from {}", DELIVERY_CSV_PATH);
        ingredientManager.importDelivery(DELIVERY_CSV_PATH);
        ingredientManager.displayStockLevels();
        ingredientManager.checkExpirationIngredients();
        log.info("Available recipes: {}", receiptManager.getReceipts().size());
        receiptManager.displayAvailableRecipes();

        // create customer queue
        log.info("Creating customer queue with {} customers", CUSTOMER_COUNT);
        CustomerManager customerManager = new CustomerManager(ingredientManager.getIngredientIds(), CUSTOMER_COUNT);
        log.info("Customer queue size: {}", customerManager.getQueueSize());

        // process customers from queue
        log.info("===== STARTING CUSTOMER ORDER PROCESSING =====");
        Customer customer;
        int customerIndex = 1;
        while ((customer = customerManager.getNextCustomer()) != null) {
            log.info("===== PROCESSING CUSTOMER {} =====", customerIndex++);
            log.info("Customer: {}", customer.getName());
            Order customerOrder = customer.getOrder();

            for (Machine machine : productionLine) {
                log.info("==== STARTING {} ====", machine.getClass().getSimpleName().toUpperCase());
                machine.startOrder(customerOrder, ingredientManager);
            }

            log.info("===== COMPLETED ORDER FOR CUSTOMER {} =====", customer.getName());
        }

        ingredientManager.saveInventoryToCSV(INVENTORY_BACKUP_CSV);
        log.info("Inventory saved to {}", INVENTORY_BACKUP_CSV);
        log.info("===== ICE CREAM PRODUCTION COMPLETED =====");
    }
}