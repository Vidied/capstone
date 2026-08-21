package davidepan.capstone.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
        @NotBlank(message = "Il nome non può essere vuoto")
        String name,
        @NotNull(message = "Il numero dell'ordine non può essere vuoto")
        Integer displayOrder
) {
}
