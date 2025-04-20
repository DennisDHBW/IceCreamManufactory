package ingredient;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Data
public class IngredientManager {
    private final Map<String, Ingredient> ingredientManager;
    private final ArrayList<Ingredient> availableIngredients;

    public IngredientManager(Map<String, Ingredient> ingredientManager) {
        this.ingredientManager = ingredientManager;
        this.availableIngredients = new ArrayList<>();
        updateAvailableIngredients();
    }

    private void updateAvailableIngredients() {
        availableIngredients.clear();
        for (Ingredient ingredient : ingredientManager.values()) {
            if (ingredient.getStockCount() > 0 && 
                !ingredient.getExpirationDate().isBefore(LocalDate.now())) {
                availableIngredients.add(ingredient);
            }
        }
    }

    @SneakyThrows
    public static IngredientManager create(String csvPath) {
        Map<String, Ingredient> ingredientManager = new HashMap<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                // Skip empty lines
                if (data.length >= 5) {
                    ingredientManager.put(data[0], Ingredient.builder()
                            .id(data[0])
                            .name(data[1])
                            .expirationDate(LocalDate.parse(data[2]))
                            .price(Double.parseDouble(data[3]))
                            .stockCount(Integer.parseInt(data[4]))
                            .build());
                }
            });
        }
        return new IngredientManager(ingredientManager);
    }

    @SneakyThrows
    public void importDelivery(String csvPath) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    ingredientManager.merge(
                            data[0],
                            Ingredient.builder()
                                    .id(data[0])
                                    .name(data[1])
                                    .expirationDate(LocalDate.parse(data[2]))
                                    .price(Double.parseDouble(data[3]))
                                    .stockCount(Integer.parseInt(data[4]))
                                    .build(),
                            (a, b) -> Ingredient.builder()
                                    .id(a.getId())
                                    .name(a.getName())
                                    .expirationDate(a.getExpirationDate())
                                    .price(a.getPrice())
                                    .stockCount(a.getStockCount() + b.getStockCount())
                                    .build()
                    );
                }
            });
        }
        updateAvailableIngredients();
    }

    public ArrayList<String> getIngredientIds() {
        ArrayList<String> ingredientIds = new ArrayList<>();
        for (Map.Entry<String, Ingredient> entry : ingredientManager.entrySet()) {
            ingredientIds.add(entry.getKey());
        }
        return ingredientIds;
    }

    public void checkExpirationIngredients() {
        log.info("--- checking ingredient expirations ---");
        LocalDate today = LocalDate.now();
        
        Iterator<Ingredient> iterator = availableIngredients.iterator();
        while (iterator.hasNext()) {
            Ingredient ingredient = iterator.next();
            LocalDate expirationDate = ingredient.getExpirationDate();
            String id = ingredient.getId();
            String name = ingredient.getName();
            int stockCount = ingredient.getStockCount();
            long difference = expirationDate.toEpochDay() - today.toEpochDay();

            if (difference >= 0) {
                log.info("ingredient {} ({}) has a shelf life of {} days", id, name, difference);
            } else {
                log.info("ingredient {} ({}) has been expired for {} days", id, name, Math.abs(difference));
                ingredientManager.remove(id);
                iterator.remove();
                log.info("ingredient {} ({}) has been removed from inventory with stock count {}", 
                        id, name, stockCount);
            }
        }
    }

    public void displayStockLevels() {
        log.info("--- current stock levels ---");
        availableIngredients.stream()
            .sorted(Comparator.comparing(Ingredient::getName))
            .forEach(a -> log.info("{}: {}", a.getName(), a.getStockCount()));
    }

    public void reduceStockCount(String ingredientId, int reduceStockCount) {
        if (ingredientManager.containsKey(ingredientId)) {
            Ingredient ingredient = ingredientManager.get(ingredientId);
            ingredient.setStockCount(ingredient.getStockCount() - reduceStockCount);
            String name = ingredient.getName();
            log.info("inventory updated: ingredient {} ({}) has been reduced by {}",
                    name, ingredientId, reduceStockCount);
                    
            if (ingredient.getStockCount() <= 0) {
                availableIngredients.removeIf(i -> i.getId().equals(ingredientId));
            }
        }
    }


    public boolean isReceiptProcessable(Receipt receipt) {
        for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
            if (!(isIngredientAvailable(ingredientWithCount.getKey(), ingredientWithCount.getValue()))) {
                log.info("receipt {} ({}) is not available", receipt.getName(), receipt.getId());
                return true;
            }
        }
        return false;
    }

    private boolean isIngredientAvailable(String ingredientId, int requiredStockCount) {
        boolean isListed = ingredientManager.containsKey(ingredientId);
        if (!isListed) {
            log.info("Ingredient {} is not listed in our inventory.", ingredientId);
            return false;
        }

        int stockCount = ingredientManager.get(ingredientId).getStockCount();
        boolean isSufficient = stockCount >= requiredStockCount;
        if (!isSufficient) {
            log.info("Ingredient {} is not sufficient ({} instead of {}).",
                    ingredientId, stockCount, requiredStockCount);
        }
        return isSufficient;
    }

    @SneakyThrows
    public void saveInventoryToCSV(String csvPath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath))) {
            writer.write("id,name,expirationDate,price,stockCount\n");
            for (Ingredient ingredient : ingredientManager.values()) {
                writer.write(String.format("%s,%s,%s,%.2f,%d\n",
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getExpirationDate(),
                        ingredient.getPrice(),
                        ingredient.getStockCount()));
            }
        }
        log.info("Inventory saved to {}", csvPath);
    }
}
