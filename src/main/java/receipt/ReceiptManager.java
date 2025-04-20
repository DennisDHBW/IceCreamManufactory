package receipt;

import ingredient.Ingredient;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Data
public class ReceiptManager {

    // csv auslesen (statisch erstellen) und csv auslesen (aktualisieren)

    private final Map<String, Receipt> receipts;

    @SneakyThrows
    public static ReceiptManager create(String csvPath) {
        Map<String, Receipt> receipts = new HashMap<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))) {
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");

                String id = data[0];
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                String ingredientId = data[3];
                int ingredientCount = Integer.parseInt(data[4]);

                // Wenn Rezept schon existiert → Zutatenliste erweitern
                Receipt existing = receipts.get(id);
                if (existing != null) {
                    existing.getIngredientsWithCount().put(ingredientId, ingredientCount);
                } else {
                    // Neue Zutatenliste anlegen
                    Map<String, Integer> ingredients = new HashMap<>();
                    ingredients.put(ingredientId, ingredientCount);

                    Receipt newReceipt = Receipt.builder()
                            .id(id)
                            .name(name)
                            .price(price)
                            .ingredientsWithCount(ingredients)
                            .build();

                    receipts.put(id, newReceipt);
                }
            });
        }
        return new ReceiptManager(receipts);
    }

    public ArrayList<String> getReceiptIds() {
        ArrayList<String> receiptIds = new ArrayList<>();
        for (Map.Entry<String, Receipt> entry : receipts.entrySet()) {
            receiptIds.add(entry.getKey());
        }
        return receiptIds;
    }
}
