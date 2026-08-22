package davidepan.capstone.entities;

import davidepan.capstone.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Integer coversCount;

    @Column(nullable = false)
    private BigDecimal coverPrice;

    public Order(Integer tableNumber, OrderStatus orderStatus, BigDecimal totalAmount, String notes, LocalDateTime createdAt, List<OrderItem> items, Integer coversCount, BigDecimal coverPrice) {
        this.tableNumber = tableNumber;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.createdAt = createdAt;
        this.items = items;
        this.coversCount = coversCount;
        this.coverPrice = coverPrice;
    }
}


