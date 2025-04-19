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
    private Map<String, Integer> ingredients;

}
