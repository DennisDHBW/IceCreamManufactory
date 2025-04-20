package receipt;

import container.ContainerType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class Receipt {
    public static final int MAX_INGREDIENT_COUNT = 15;
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
        Receipt receipt = Receipt.builder()
                .id("CUS" + String.format("%03d", ++customOrderId))
                .name("Customised Order")
                .price(5.00)
                .ingredientsWithCount(ingredientsWithCount)
                .containerType(ContainerType.CONE)
                .build();
        return receipt;
    }


}
