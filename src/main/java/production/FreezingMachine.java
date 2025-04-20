package production;

import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class FreezingMachine extends Machine {

    public FreezingMachine(String id, String model, String manufacturer) {
        super(id, model, manufacturer);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();
        for (Receipt receipt : receipts) {

            String ingredientNames = "";
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                char identificationChar =  ingredientWithCount.getKey().charAt(0);
                if (!(identificationChar == 'M' || identificationChar == 'F' || identificationChar == 'S')) {
                    continue;
                }
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                ingredientNames = ingredientNames.concat(name + ",");
            }
            ingredientNames = ingredientNames.substring(0, ingredientNames.length() - 1);
            log.info("mass consisting of {} is freezing in pasteurising machine.", ingredientNames);
            log.info("freezing finished for receipt: {} ({})", receipt.getName(), receipt.getId());
            this.order = null;
            this.status = MachineStatus.AVAILABLE;
        }
    }
}
