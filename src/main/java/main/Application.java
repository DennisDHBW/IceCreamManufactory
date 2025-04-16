package main;

/*

    Ab 20 Minuten stillstand --> Termin vereinbaren und Hilfe anfordern
    Teams mit Matrikelnummer anmelden

 */

import lombok.extern.slf4j.Slf4j;
import shared.InventoryManager;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final String CSV_PATH = "src/inventory.csv";
        InventoryManager inventoryManager = InventoryManager.create(CSV_PATH);
        log.info("Application finished processing.");


    }

}
