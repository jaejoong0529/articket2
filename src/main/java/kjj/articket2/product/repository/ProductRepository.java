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
    Optional<Product> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    List<Product> findByCategory(Category category);

    // 판매 상태로 필터링
    List<Product> findByIsSold(boolean isSold);
}
