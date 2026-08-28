package davidepan.capstone.payloads;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO (
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean isAvailable,
        Long categoryId,
        String categoryName,
        List<String> ingredientNames
){
}
