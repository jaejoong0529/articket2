package kjj.articket2.product.repository;

import jakarta.persistence.LockModeType;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByEndTimeBeforeAndIsSoldFalse(LocalDateTime now);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long id);

    List<Product> findByCategory(Category category);
}
