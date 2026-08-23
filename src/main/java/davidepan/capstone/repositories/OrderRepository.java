package davidepan.capstone.repositories;

import davidepan.capstone.entities.Order;
import davidepan.capstone.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderStatus(OrderStatus status);

    List<Order> findByTableNumber(Integer tableNumber);

    List<Order> findByOrderStatusNotAndOrderStatusNot(OrderStatus status1, OrderStatus status2);
}
