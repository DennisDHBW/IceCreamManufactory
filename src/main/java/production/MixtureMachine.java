package production;

import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
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
            if (ingredientManager.isReceiptProcessable(receipt)) {
                unavailableReceipts.add(receipt);
                continue;
            }

            String ingredientNames = "";
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                char identificationChar = ingredientWithCount.getKey().charAt(0);
                if (!(identificationChar == 'M' || identificationChar == 'F' || identificationChar == 'S')) {
                    continue;
                }
                this.container.push(ingredientWithCount);
                ingredientManager.reduceStockCount(ingredientWithCount.getKey(), ingredientWithCount.getValue());
                
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                ingredientNames = ingredientNames.concat(ingredientNames + name + ", ");
                log.info("ingredient {} ({}) has been processed with count {} in mixture machine.",
                        name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
            }
            
            if (!ingredientNames.isEmpty()) {
                ingredientNames = ingredientNames.substring(0, ingredientNames.length() - 2);
                log.info("Mixing ingredients: {}", ingredientNames);
            }
            
            log.info("mixing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
        }
        for (Receipt receipt : unavailableReceipts) {
            receipts.remove(receipt);
        }
        
        this.status = MachineStatus.AVAILABLE;
    }
}
