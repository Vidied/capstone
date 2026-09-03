package davidepan.capstone.controllers;

import davidepan.capstone.enums.OrderStatus;
import davidepan.capstone.payloads.OrderItemRequestDTO;
import davidepan.capstone.payloads.OrderRequestDTO;
import davidepan.capstone.payloads.OrderResponseDTO;
import davidepan.capstone.payloads.OrderStatusUpdateDTO;
import davidepan.capstone.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders(@RequestParam(required = false) OrderStatus status) {
        if (status != null) {
            return orderService.findByStatus(status);
        }
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(@RequestBody @Validated OrderRequestDTO body) {
        return orderService.save(body);
    }

    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@RequestBody @Validated OrderRequestDTO body, @PathVariable Long id) {
        return orderService.update(body, id);
    }

    @PostMapping("/{id}/items")
    public OrderResponseDTO appendItemsToOrder(@PathVariable Long id, @RequestBody @Validated List<OrderItemRequestDTO> items) {
        return orderService.appendItems(id, items);
    }

    @PatchMapping("/{id}/status")
    public OrderResponseDTO updateOrderStatus(@PathVariable Long id, @RequestBody @Validated OrderStatusUpdateDTO body) {
        return orderService.updateStatus(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }
}