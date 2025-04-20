package ingredient;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import receipt.Receipt;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Data
public class IngredientManager {
    private final Map<String, Ingredient> ingredientManager;

    @SneakyThrows
    public static IngredientManager create(String csvPath) {
        Map<String, Ingredient> ingredientManager = new HashMap<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                // KOMMENTAR: Leerzeilen werden nicht gehandelt
                ingredientManager.put(data[0], Ingredient.builder()
                        .id(data[0])
                        .name(data[1])
                        .expirationDate(LocalDate.parse(data[2]))
                        .price(Double.parseDouble(data[3]))
                        .stockCount(Integer.parseInt(data[4]))
                        .build());
            });
        }
        return new IngredientManager(ingredientManager);
    }


    @SneakyThrows
    public IngredientManager importDelivery(String csvPath) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
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
            });
        }
        return new IngredientManager(ingredientManager);
    }

    public ArrayList<String> getIngredientIds() {
        ArrayList<String> ingredientIds = new ArrayList<>();
        for (Map.Entry<String, Ingredient> entry : ingredientManager.entrySet()) {
            ingredientIds.add(entry.getKey());
        }
        return ingredientIds;
    }

    public void checkExpirationIngredients(ArrayList<String> ingredientIds) {

        log.info("--- current ingredient expirations ---");
        for (String ingredientId : ingredientIds) {
            LocalDate today = LocalDate.now();
            LocalDate ingredientExpirationDate = ingredientManager.get(ingredientId).getExpirationDate();
            String name = ingredientManager.get(ingredientId).getName();
            int stockCount = ingredientManager.get(ingredientId).getStockCount();
            long difference = ingredientExpirationDate.toEpochDay() - today.toEpochDay();

            if (difference >= 0) {
                log.info("ingredient {} ({}) has a shelf life of {} days", ingredientId, name, difference);
            }
            else {
                log.info("ingredient {} ({}) has been expired for {} days", ingredientId, name, Math.abs(difference));
                ingredientManager.remove(ingredientId);
                log.info("ingredient {} ({}) has been removed out of ingredientManager with stock count {}",
                        ingredientId, name, stockCount);
            }
        }
    }

    public void displayStockLevels() {
        log.info("--- current stock levels ---");
        ingredientManager.values().stream().sorted(
            Comparator.comparing(Ingredient::getName))
                .forEach(a -> log.info("{}: {}", a.getName(), a.getStockCount()));
    }

    public void reduceStockCount(IngredientManager ingredientManager, String ingredientId, int reduceStockCount) {
        ingredientManager.getIngredientManager().get(ingredientId)
                .setStockCount(ingredientManager.getIngredientManager()
                .get(ingredientId).getStockCount() - reduceStockCount);
        String name = ingredientManager.getIngredientManager().get(ingredientId).getName();
        log.info("inventory updated: ingredient {} ({}) has been reduced by {}",
                name, ingredientId, reduceStockCount);
    }

    /*
    public void reduceStockCountForReceipt(IngredientManager ingredientManager, Receipt receipt) {
        for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
            ingredientManager.getIngredientManager().get(ingredientWithCount.getKey())
                    .setStockCount(ingredientManager.getIngredientManager()
                            .get(ingredientWithCount.getKey()).getStockCount() - ingredientWithCount.getValue());
            String name = ingredientManager.getIngredientManager().get(ingredientWithCount.getKey()).getName();
            log.info("inventory updated: ingredient {} ({}) has been reduced by {}",
                    name, ingredientWithCount.getKey(), ingredientWithCount.getValue());
        }
    }
     */

    public boolean isReceiptProcessable(IngredientManager ingredientManager, Receipt receipt) {
        for (Map.Entry<String, Integer> ingredientWithCount : receipt.getIngredientsWithCount().entrySet()) {
            if (!(isIngredientAvailable(ingredientWithCount.getKey(), ingredientWithCount.getValue()))) {
                log.info("receipt {} ({}) is not available", receipt.getName(), receipt.getId());
                return false;
            }
        }
        return true;
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

    // validierung von rezept und menge in inventar
    // Bestand verringern bei abgelaufenen?
    // OPTIONAL: HashMap als CSV zwischenspeichern nach aktualisierung des inventars

}
