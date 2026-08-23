package davidepan.capstone.payloads;

import davidepan.capstone.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequestDTO(

        @NotNull(message = "Il numero del tavolo è obbligatorio")
        @Min(value = 1, message = "Il numero del tavolo deve essere maggiore di 1")
        Integer tableNumber,

        @NotNull(message = "Il numero dei coperti è obbligatorio")
        @Min(value = 1, message = "Il tavolo deve avere almeno 1 coperto")
        Integer coverCount,
        BigDecimal coverPrice,
        OrderStatus orderStatus,
        String notes,

        @NotEmpty(message = "La comanda deve avere almeno un prodotto")
        List<OrderItemRequestDTO> items
) {
}
