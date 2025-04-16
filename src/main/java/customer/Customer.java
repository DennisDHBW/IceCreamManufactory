package customer;

import java.util.List;
import java.util.UUID;

public class Customer {
    private UUID uuid;

    // 1:n association - unidirectional
    private List<String> ingredientIds;

}
