package customer;

import lombok.Builder;
import lombok.Data;
import order.Order;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class Customer {
    private UUID customerId;
    private String name;
    private Order order;
    private List<String> preferredIngredientIds;
}
