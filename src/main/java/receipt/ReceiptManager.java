package receipt;

import container.ContainerType;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Data
public class ReceiptManager {
    private final Map<String, Receipt> receipts;

    @SneakyThrows
    public static ReceiptManager create(String csvPath) {
        Map<String, Receipt> receipts = new HashMap<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))) {
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String id = data[0];
                    String name = data[1];
                    double price = Double.parseDouble(data[2]);
                    String ingredientId = data[3];
                    int ingredientCount = Integer.parseInt(data[4]);
                    ContainerType containerType = ContainerType.SUNDAE;
                    if (data.length > 5) {
                        containerType = "CONE".equalsIgnoreCase(data[5]) ? ContainerType.CONE : ContainerType.SUNDAE;
                    }

                    // If recipe already exists, expand ingredient list
                    Receipt existing = receipts.get(id);
                    if (existing != null) {
                        existing.getIngredientsWithCount().put(ingredientId, ingredientCount);
                    } else {
                        // Create new ingredient list
                        Map<String, Integer> ingredients = new HashMap<>();
                        ingredients.put(ingredientId, ingredientCount);

                        Receipt newReceipt = Receipt.builder()
                                .id(id)
                                .name(name)
                                .price(price)
                                .ingredientsWithCount(ingredients)
                                .containerType(containerType)
                                .build();

                        receipts.put(id, newReceipt);
                    }
                }
            });
        }
        return new ReceiptManager(receipts);
    }

    public ArrayList<String> getReceiptIds() {
        return new ArrayList<>(receipts.keySet());
    }
    
    public Receipt getReceipt(String receiptId) {
        return receipts.get(receiptId);
    }
    
    public Receipt createCustomReceipt(Map<String, Integer> ingredients, ContainerType containerType) {
        return Receipt.builder()
                .id("CUS" + UUID.randomUUID().toString().substring(0, 8))
                .name("Custom Recipe")
                .price(calculateCustomPrice(ingredients))
                .ingredientsWithCount(ingredients)
                .containerType(containerType)
                .build();
    }
    
    private double calculateCustomPrice(Map<String, Integer> ingredients) {
        double basePrice = 2.0;
        
        // Add up ingredient quantities
        int totalIngredients = ingredients.values().stream().mapToInt(Integer::intValue).sum();
        
        return basePrice + (totalIngredients * 0.5);
    }
}
