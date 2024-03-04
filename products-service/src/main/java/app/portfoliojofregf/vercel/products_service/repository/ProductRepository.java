package app.portfoliojofregf.vercel.products_service.repository;

import app.portfoliojofregf.vercel.products_service.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
