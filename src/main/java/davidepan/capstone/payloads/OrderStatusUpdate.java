package davidepan.capstone.payloads;

import davidepan.capstone.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdate(

        @NotNull(message = "Lo stato dell'ordine è obbligatorio")
        OrderStatus status
) {
}
