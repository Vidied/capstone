package davidepan.capstone.services;

import davidepan.capstone.entities.Order;
import davidepan.capstone.entities.OrderItem;
import davidepan.capstone.entities.Product;
import davidepan.capstone.enums.OrderStatus;
import davidepan.capstone.exceptions.BadRequestException;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.OrderItemRequestDTO;
import davidepan.capstone.payloads.OrderRequestDTO;
import davidepan.capstone.payloads.OrderStatusUpdateDTO;
import davidepan.capstone.repositories.OrderRepository;
import davidepan.capstone.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${restaurant.cover-price}")
    private BigDecimal defaultCoverPrice;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order findById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Comanda con ID " + id + " non è stata trovata"));
    }

    public List<Order> findByStatus(OrderStatus status){
        return orderRepository.findByOrderStatus(status);
    }

    @Transactional
    public Order save(OrderRequestDTO body){
        Order newOrder = new Order();

        newOrder.setTableNumber(body.tableNumber());
        newOrder.setCoversCount(body.coverCount());
        newOrder.setCoverPrice(defaultCoverPrice);
        newOrder.setNotes(body.notes());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setCreatedAt(LocalDateTime.now());

        this.processOrderItemsAndTotal(newOrder, body.items(), body.coverCount());

        return orderRepository.save(newOrder);
    }

    @Transactional
    public Order update(OrderRequestDTO body, Long id){
        Order found = this.findById(id);

        found.setTableNumber(body.tableNumber());
        found.setCoversCount(body.coverCount());
        found.setOrderStatus(body.orderStatus());
        found.setNotes(body.notes());

        found.getItems().clear();

        this.processOrderItemsAndTotal(found, body.items(), body.coverCount());

        return orderRepository.save(found);
    }

    public Order updateStatus(Long id, OrderStatusUpdateDTO body){
        Order found = this.findById(id);
        found.setOrderStatus(body.orderStatus());
        return orderRepository.save(found);
    }

    public void delete(Long id){
        Order found = this.findById(id);
        orderRepository.delete(found);
    }

    //Metodo helper per evitare di duplicare il codice massiccio tra save ed update
    private void processOrderItemsAndTotal(Order order, List<OrderItemRequestDTO> itemDTOs, Integer coverCount){
        List<OrderItem> items = new ArrayList<>();
        BigDecimal productsTotal = BigDecimal.ZERO;

        for(OrderItemRequestDTO itemDTO : itemDTOs){
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(()-> new NotFoundException("Prodotto con ID " + itemDTO.productId() + " non è stato trovato"));

            if(Boolean.FALSE.equals(product.getIsAvailable())){
                throw new BadRequestException("Il prodotto " + product.getName() + " non è attualmente disponibile");
            }

            BigDecimal unitPrice = product.getPrice();

            OrderItem orderItem = new OrderItem(order, product, itemDTO.quantity(), unitPrice, itemDTO.notes());
            items.add(orderItem);

            BigDecimal itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(itemDTO.quantity()));
            productsTotal = productsTotal.add(itemSubtotal);
        }

        order.getItems().addAll(items);

        BigDecimal totalCoversAmount = defaultCoverPrice.multiply(BigDecimal.valueOf(coverCount));

        order.setTotalAmount(productsTotal.add(totalCoversAmount));

    }
}
