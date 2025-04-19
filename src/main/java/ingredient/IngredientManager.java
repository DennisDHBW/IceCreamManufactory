package ingredient;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Data
public class IngredientManager {
    private final Map<String, Ingredient> inventory;

    @SneakyThrows
    public static IngredientManager create(String csvPath) {
        Map<String, Ingredient> inventory = new HashMap<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                inventory.put(data[0], Ingredient.builder()
                        .id(data[0])
                        .name(data[1])
                        .expirationDate(LocalDate.parse(data[2]))
                        .price(Double.parseDouble(data[3]))
                        .stockCount(Integer.parseInt(data[4]))
                        .build());
            });
        }
        return new IngredientManager(inventory);
    }


    @SneakyThrows
    public IngredientManager importDelivery(String csvPath) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                inventory.merge(
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
            });
        }
        return new IngredientManager(inventory);
    }

    public ArrayList<String> getIngredientIds() {
        ArrayList<String> ingredientIds = new ArrayList<>();
        for (Map.Entry<String, Ingredient> entry : inventory.entrySet()) {
            ingredientIds.add(entry.getKey());
        }
        return ingredientIds;
    }

    public void checkExpirationIngredients(ArrayList<String> ingredientIds) {

        log.info("--- current ingredient expirations ---");
        for (String ingredientId : ingredientIds) {
            LocalDate today = LocalDate.now();
            LocalDate ingredientExpirationDate = inventory.get(ingredientId).getExpirationDate();
            String name = inventory.get(ingredientId).getName();
            int stockCount = inventory.get(ingredientId).getStockCount();
            long difference = ingredientExpirationDate.toEpochDay() - today.toEpochDay();

            if (difference >= 0) {
                log.info("ingredient {} ({}) has a shelf life of {} days", ingredientId, name, difference);
            }
            else {
                log.info("ingredient {} ({}) has been expired for {} days", ingredientId, name, Math.abs(difference));
                inventory.remove(ingredientId);
                log.info("ingredient {} ({}) has been removed out of inventory with stock count {}",
                        ingredientId, name, stockCount);
            }
        }
    }


    public void displayStockLevels() {
        log.info("--- current stock levels ---");
        inventory.values().stream().sorted(
            Comparator.comparing(Ingredient::getName))
                .forEach(a -> log.info("{}: {}", a.getName(), a.getStockCount()));
    }

    // validierung von rezept und menge in inventar
    // OPTIONAL: HashMap als CSV zwischenspeichern nach aktualisierung des inventars

}
