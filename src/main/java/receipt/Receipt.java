package receipt;

import container.ContainerType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class Receipt {
    public static final int MAX_INGREDIENT_COUNT = 30;
    private static int customOrderId = 0;
    private String id;
    private String name;
    private Double price;
    private Map<String, Integer> ingredientsWithCount;
    private ContainerType containerType;

    public int getTotalIngredientCount() {
        int totalIngredientCount = 0;
        for(Map.Entry<String, Integer> entry : ingredientsWithCount.entrySet()) {
            totalIngredientCount += entry.getValue();
        }
        return totalIngredientCount;
    }

    public static Receipt getCustomReceipt(HashMap<String, Integer> ingredientsWithCount) {
        ContainerType type = Math.random() > 0.5 ? ContainerType.CONE : ContainerType.SUNDAE;
        double calculatedPrice = calculatePrice(ingredientsWithCount);

        return Receipt.builder()
                .id("CUS" + String.format("%03d", ++customOrderId))
                .name("Customised Order")
                .price(calculatedPrice)
                .ingredientsWithCount(ingredientsWithCount)
                .containerType(type)
                .build();
    }
    
    private static double calculatePrice(HashMap<String, Integer> ingredients) {
        // Base price
        double basePrice = 2.50;
        
        // Add 1.00 for each ingredient
        return basePrice + ingredients.size() * 1.00;
    }
}
