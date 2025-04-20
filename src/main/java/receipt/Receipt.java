package receipt;

import ingredient.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class Receipt {
    private String id;
    private String name;
    private Double price;
    private static int customOrderId = 0;
    private Map<String, Integer> ingredientsWithCount;

    public static Receipt getCustomReceipt(HashMap<String, Integer> ingredientsWithCount) {
        Receipt receipt = Receipt.builder()
                .id("CUS" + String.format("%03d", ++customOrderId))
                .name("Customised Order")
                .price(5.00)
                .ingredientsWithCount(ingredientsWithCount)
                .build();
        return receipt;
    }


}
