package kjj.articket2.product.repository;

import jakarta.persistence.LockModeType;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByEndTimeBeforeAndIsSoldFalse(LocalDateTime now);
    @Lock(LockModeType.PESSIMISTIC_WRITE) // ✅ 상품 데이터를 잠금
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    List<Product> findByCategory(ProductCategory category);
}
