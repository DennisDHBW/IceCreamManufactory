package ingredient;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Ingredient {
    private String id;
    private String name;
    private LocalDate expirationDate;
    private Double price;
    private int stockCount;
}
