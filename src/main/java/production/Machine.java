package production;

import ingredient.IngredientManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import order.Order;
import receipt.Receipt;
import shared.MyStack;
import java.util.Map;

@Slf4j
@Data
public abstract class Machine {
    public static int MAX_FILL_COUNT = 500;

    protected final String id;
    protected final String model;
    protected final String manufacturer;
    protected MachineStatus status;
    protected Order order;
    protected MyStack<Map.Entry<String, Integer>> container;
    protected int fillCount;
    protected int totalFillCount;
    protected double utilisationPercentage;


    public Machine(String id, String model, String manufacturer) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.order = null;
        this.status = MachineStatus.AVAILABLE;
        this.container = new MyStack<>(Receipt.MAX_INGREDIENT_COUNT);
        this.fillCount = 0;
        this.totalFillCount = Machine.MAX_FILL_COUNT;
        this.utilisationPercentage = 0.0;
    }

    private void calculateUtilisation() {
        int orderFillCount = 0;
        for (Receipt receipt : order.getReceipts()) {
            orderFillCount += receipt.getTotalIngredientCount();
        }
        if (totalFillCount >= orderFillCount) {
            this.utilisationPercentage = orderFillCount * 100.0 / totalFillCount;
        }
    }

    public void startOrder(Order order, IngredientManager ingredientManager) {
        this.order = order;
        calculateUtilisation();

        if(this.utilisationPercentage > 0.0) {
            this.status = MachineStatus.IN_PROCESS;
            this.processOrder(ingredientManager);
        }
        else {
            log.info("the order is too big for our capacity.");
        }




    }

    protected void processOrder(IngredientManager ingredientManager) {
    }

}
