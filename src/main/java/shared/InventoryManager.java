package shared;

import ingredient.Ingredient;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class InventoryManager {
    private final Map<String, Ingredient> inventory;

    @SneakyThrows
    public static InventoryManager create(String csvPath) {
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
        return new InventoryManager(inventory);
    }

    // validierung von rezept und menge in inventar

}
