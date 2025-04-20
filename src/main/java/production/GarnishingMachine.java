package production;

import ingredient.Ingredient;
import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;
import container.Container;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class GarnishingMachine extends Machine {

    public GarnishingMachine(String id, String model, String manufacturer) {
        super(id, model, manufacturer);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();

        for (Receipt receipt : receipts) {
            if (ingredientManager.isReceiptProcessable(receipt)) {
                log.info("Cannot process receipt {} - ingredients not available", receipt.getId());
                continue;
            }

            // Create a container for this receipt
            Container container = Container.createEmpty(receipt.getContainerType());
            log.info("Created {} container for receipt {}", receipt.getContainerType(), receipt.getId());

            // Fill garnishing machine with toppings and decorations
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                String ingredientId = ingredientWithCount.getKey();
                int count = ingredientWithCount.getValue();
                char identificationChar = ingredientId.charAt(0);

                // Only process decorations (D), chocolates (C) and sauces (S) in garnishing machine
                if (!(identificationChar == 'D' || identificationChar == 'C' || identificationChar == 'S')) {
                    continue;
                }

                Ingredient ingredient = ingredientManager.getIngredientManager().get(ingredientId);

                // Add to machine container (Stack)
                this.container.push(ingredientWithCount);

                // Add ingredient to container layers (multiple times based on count)
                for (int i = 0; i < count; i++) {
                    container.addLayer(ingredient);
                }

                // Reduce stock
                ingredientManager.reduceStockCount(ingredientId, count);

                log.info("Added {} x {} ({}) to container", count, ingredient.getName(), ingredientId);
            }

            log.info("Garnishing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
            this.order.getContainers().add(container);
        }

        this.status = MachineStatus.AVAILABLE;
    }
}