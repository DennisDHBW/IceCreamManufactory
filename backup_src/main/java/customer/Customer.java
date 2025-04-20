package customer;

import lombok.Builder;
import order.Order;

import java.util.List;
import java.util.UUID;

@Builder
public class Customer {
    private UUID customerId;
    private Order order;

    // 1:n association - unidirectional
    private List<String> ingredientIds;

}
