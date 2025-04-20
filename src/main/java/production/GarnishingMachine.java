package production;

import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
import order.Order;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class GarnishingMachine extends Machine {

    public GarnishingMachine(String id, String model, String manufacturer) {
        super(id, model, manufacturer);
    }

    public void startOrder(Order order, IngredientManager ingredientManager) {
        super.startOrder(order, ingredientManager);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();
        for (Receipt receipt : receipts) {
            if (!ingredientManager.isReceiptProcessable(ingredientManager, receipt)) {
                continue;
            }

            // fill garnishing machine
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                char identificationChar =  ingredientWithCount.getKey().charAt(0);
                if (!(identificationChar == 'D' || identificationChar == 'C' || identificationChar == 'S')) {
                    continue;
                }
                ingredientManager.reduceStockCount(ingredientManager, ingredientWithCount.getKey(), ingredientWithCount.getValue());
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                this.container.push(ingredientWithCount);
                log.info("ingredient {} ({}) has been processed with count {} in garnishing machine.",
                        name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
            }
            log.info("garnishing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
        }
    }

}
