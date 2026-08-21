package davidepan.capstone.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        @NotBlank(message = "Il nome del prodotto è obbligatorio!")
        String name,
        String description,
        @NotNull(message = "Il prezzo del prodotto è obbligatorio!")
        BigDecimal price,
        Boolean isAvailable,
        @NotNull(message = "Id della categoria è obbligatoria")
        Long categoryId,
        List<Long> ingredientIds
) {
}
