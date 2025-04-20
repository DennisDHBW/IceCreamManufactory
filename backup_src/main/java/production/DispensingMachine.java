package production;

import ingredient.IngredientManager;
import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class DispensingMachine extends Machine {

    public DispensingMachine(String id, String model, String manufacturer) {
        super(id, model, manufacturer);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();
        for (Receipt receipt : receipts) {
            for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
                String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
                log.info("{} ({}) with count {}", name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
            }

            log.info("dispensing: {} ({})", receipt.getName(), receipt.getId());
            this.order = null;
            this.status = MachineStatus.AVAILABLE;
        }
    }
}
