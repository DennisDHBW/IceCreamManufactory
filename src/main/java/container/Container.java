package container;

import ingredient.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.Stack;

@Builder
@Data
public class Container {
    private final ContainerType containerType;
    private final Stack<Ingredient> layers;

    public void addLayer(Ingredient ingredient) {
        layers.push(ingredient);
    }

    public static Container createEmpty(ContainerType type) {
        return Container.builder()
                .containerType(type)
                .layers(new Stack<>())
                .build();
    }

    @Override
    public String toString() {
        return containerType.name() + " with " + layers.size() + " layers";
    }
}