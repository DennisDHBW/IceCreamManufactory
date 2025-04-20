package customer;

import order.Order;
import shared.MyStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CustomerManager {
    private final Queue<Customer> customerQueue;
    private static final int MIN_ITEMS = 1;
    private static final int MAX_ITEMS = 5;
    private static final Random random = new Random();

    public CustomerManager(List<String> ingredientIds, int customerCount) {
        this.customerQueue = generateCustomerQueue(ingredientIds, customerCount);
    }

    private Queue<Customer> generateCustomerQueue(List<String> ingredientIds, int numCustomers) {
        return IntStream.range(0, numCustomers)
                .mapToObj(i -> createCustomer(ingredientIds, "Customer " + (i+1)))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Customer createCustomer(List<String> ingredientIds, String name) {
        return Customer.builder()
                .customerId(UUID.randomUUID())
                .name(name)
                .order(Order.generateSampleOrder())
                .preferredIngredientIds(selectRandomIngredients(ingredientIds))
                .build();
    }

    private List<String> selectRandomIngredients(List<String> ingredientIds) {
        Collections.shuffle(ingredientIds);
        int preferenceCount = MIN_ITEMS + random.nextInt(MAX_ITEMS - MIN_ITEMS + 1);
        return new ArrayList<>(ingredientIds.subList(0, Math.min(preferenceCount, ingredientIds.size())));
    }

    public Customer getNextCustomer() {
        if (customerQueue.isEmpty()) {
            log.info("No more customers in queue");
            return null;
        }
        return customerQueue.poll();
    }

    public void addCustomer(Customer customer) {
        customerQueue.offer(customer);
        log.info("Added new customer: {} to queue", customer.getName());
    }

    public int getQueueSize() {
        return customerQueue.size();
    }
}
