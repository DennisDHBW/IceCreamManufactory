package production;

import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
import order.Order;
import receipt.Receipt;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class MixtureMachine extends Machine {

    public MixtureMachine(String id, String model, String manufacturer) {
        super(id, model, manufacturer);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();
        ArrayList<Receipt> unavailableReceipts = new ArrayList<>();
        for (Receipt receipt : receipts) {
            if (!ingredientManager.isReceiptProcessable(ingredientManager, receipt)) {
                unavailableReceipts.add(receipt);
                continue;
            }

            // fill mixture machine
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                char identificationChar =  ingredientWithCount.getKey().charAt(0);
                if (!(identificationChar == 'M' || identificationChar == 'F' || identificationChar == 'S')) {
                    continue;
                }
                ingredientManager.reduceStockCount(ingredientManager, ingredientWithCount.getKey(), ingredientWithCount.getValue());
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                log.info("ingredient {} ({}) has been processed with count {} in mixture machine.",
                        name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
            }
            log.info("mixing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
            this.order = null;
            this.status = MachineStatus.AVAILABLE;
        }
        for (Receipt receipt : unavailableReceipts) {
            receipts.remove(receipt);
        }
    }
}
