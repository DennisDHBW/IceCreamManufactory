package container;

import ingredient.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Stack;

@Builder
@Data
public class Container {
    private final ContainerType containerType;
    private final Stack<Ingredient> layers;
    
    public void addLayer(Ingredient ingredient) {
        layers.push(ingredient);
    }
    
    public Ingredient removeTopLayer() {
        if (!layers.isEmpty()) {
            return layers.pop();
        }
        return null;
    }
    
    public static Container createEmpty(ContainerType type) {
        return Container.builder()
                .containerType(type)
                .layers(new Stack<>())
                .build();
    }
}
