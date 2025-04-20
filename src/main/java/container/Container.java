package container;

import ingredient.Ingredient;
import lombok.Builder;

import java.util.ArrayList;

@Builder
public class Container {
    private final ContainerType containerType;
    private final ArrayList<Ingredient> ingredients;
}
