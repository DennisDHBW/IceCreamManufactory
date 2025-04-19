package receipt;

import ingredient.Ingredient;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class Receipt {
    private String id;
    private String name;
    private Double price;
    private String ingredients;


    /*public Map<String, Integer> getIngredientHashMap() {
        String[] singleComponents;
        Map<String, Integer> componentsWithCount = new HashMap<>();

        return componentsWithCount;
    }*/


    public Map<String, Integer> getIngredientHashMap() {
        String[] singleComponents = this.ingredients.split(";");
        Map<String, Integer> componentsWithCount = new HashMap<>();

        for (String ingredientWithCount : singleComponents) {
            String[] splitedInformation = ingredientWithCount.split("/");
            componentsWithCount.put(splitedInformation[0], Integer.parseInt(splitedInformation[1]));
        }
        return componentsWithCount;
    }

    public Receipt getReceiptFromCustomisedOrder(HashMap<Ingredient, Integer> ingredientsWithCount) {

        Receipt receipt = Receipt.builder()
                .id("EISCUS")
                .name("Customised Order")
                .price(price)
                .build();
        return receipt;
    }
}
