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

    public void startOrder(Order order, IngredientManager ingredientManager) {
        super.startOrder(order, ingredientManager);
    }

    @Override
    protected void processOrder(IngredientManager ingredientManager) {
        ArrayList<Receipt> receipts = this.order.getReceipts();
        for (Receipt receipt : receipts) {
            if (!(ingredientManager.isReceiptProcessable(receipt))) {
                continue;
            }
            Map<String, Integer> ingredientsWithCount = receipt.getIngredientsWithCount();
        }
    }
}
