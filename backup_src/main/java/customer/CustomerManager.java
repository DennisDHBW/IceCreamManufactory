package customer;

import order.Order;
import shared.MyStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerManager {
    /*private final Queue<Customer> customerQueue;

    public CustomerManager(List<String> orderIds, int customerCount) {
        this.customerQueue = generateCustomerQueue(orderIds, customerCount);
    }

    private Queue<Customer> generateCustomerQueue(List<String> articleIds, int numCustomers) {
        return IntStream.range(0,
                numCustomers).mapToObj(i -> createCustomer(articleIds)).collect(Collectors.toCollection(LinkedList::new));
    }

}

private Customer createCustomer(List<String> articleIds) {
    return Customer.builder()
            .customerId(UUID.randomUUID())
            .order(Order.generateSampleOrder())
            .build();
}

private MyStack<String> buildTrolley(List<String> articleIds) {
    // Mischt die Liste der Artikel-IDs zufällig, um einen zufälligen Artikelmix zu gewährleisten.
    Collections.shuffle(articleIds);
    // Bestimmt die Kapazität des Einkaufswagens als zufällige Zahl zwischen MIN_ITEMS und MAX_ITEMS (inklusive).
    int trolleyCapacity = MIN_ORDER + random.nextInt(MAX_ITEMS - MIN_ITEMS + 1);
    // Erstellt einen neuen MyStack mit der ermittelten Kapazität.
    MyStack<String> trolley = new MyStack<>(trolleyCapacity);

    // Fügt die ersten "trolleyCapacity" Artikel-IDs aus der zufällig gemischten Liste in den Einkaufswagen ein.
    articleIds.subList(0, trolleyCapacity).forEach(trolley::push);
    return trolley;
    */
}


