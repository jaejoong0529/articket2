package kjj.articket2.product.repository;

import kjj.articket2.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByEndTimeBeforeAndIsSoldFalse(LocalDateTime now);
}
