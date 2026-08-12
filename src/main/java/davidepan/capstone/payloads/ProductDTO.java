package davidepan.capstone.payloads;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        String name,
        String description,
        BigDecimal price,
        Boolean isAvailable,
        Long CategoryId,
        List<Long> ingredientIds
) {
}
