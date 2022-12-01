package bove.order.processing.service.repository;

import bove.order.processing.service.dto.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
}
