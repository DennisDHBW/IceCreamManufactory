package production;

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
            if (!ingredientManager.isReceiptProcessable(receipt)) {
                continue;
            }

            // Create a container for this receipt
            Container container = Container.createEmpty(receipt.getContainerType());
            
            // Fill garnishing machine with toppings and decorations
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                char identificationChar = ingredientWithCount.getKey().charAt(0);
                if (!(identificationChar == 'D' || identificationChar == 'C' || identificationChar == 'S')) {
                    continue;
                }
                
                // Add to machine container (Stack)
                this.container.push(ingredientWithCount);
                
                // Reduce stock
                ingredientManager.reduceStockCount(ingredientWithCount.getKey(), ingredientWithCount.getValue());
                
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                log.info("ingredient {} ({}) has been processed with count {} in garnishing machine.",
                        name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
            }
            
            log.info("garnishing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
            this.order.getContainers().add(container);
        }
        
        this.status = MachineStatus.AVAILABLE;
    }
}
