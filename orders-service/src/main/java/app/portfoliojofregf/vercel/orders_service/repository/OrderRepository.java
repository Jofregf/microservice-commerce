package app.portfoliojofregf.vercel.orders_service.repository;

import app.portfoliojofregf.vercel.orders_service.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
