package production;


import ingredient.IngredientManager;
import lombok.Data;
import order.Order;

@Data
public abstract class Machine {
    protected final String id;
    protected final String model;
    protected final String manufacturer;
    protected MachineStatus status;
    protected Order order;

    public Machine(String id, String model, String manufacturer) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.order = null;
        this.status = MachineStatus.AVAILABLE;
    }

    protected void startOrder(Order order, IngredientManager ingredientManager) {
        this.order = order;
        this.status = MachineStatus.IN_PROCESS;
        this.processOrder(ingredientManager);
    }

    protected void processOrder(IngredientManager ingredientManager) {


    }

}
