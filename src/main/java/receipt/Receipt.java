package receipt;

import lombok.Builder;
import java.util.HashMap;
import java.util.Map;

@Builder
public class Receipt {
    private String id;
    private String name;
    private Double price;
    private String ingredients;

    public Map<String, String> getIngredientHashMap() {
        String[] singleComponents = this.ingredients.split(";");
        Map<String, String> componentsWithCount = new HashMap<>();

        for (String ingredientWithCount : singleComponents) {
            String[] splittedInformation = ingredientWithCount.split("/");
            componentsWithCount.put(splittedInformation[0], splittedInformation[1]);
        }
        return componentsWithCount;
    }
}
