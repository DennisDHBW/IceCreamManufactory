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
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvPath))){
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] data = line.split(",");
                Set<String> receiptIds = new HashSet<>(Arrays.asList(data));
                HashMap<String, Integer> ingredientsWithCount = new HashMap<>();


                /*for (String receiptId : receiptIds) {
                    for (String csvLine : data){}

                    }
                }*/

                receipts.put(data[0], Receipt.builder()
                        .id(data[0])
                        .name(data[1])
                        .price(Double.parseDouble(data[2]))
                        .ingredients(data[3])
                        .build());
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
