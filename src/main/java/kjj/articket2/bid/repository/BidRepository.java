package kjj.articket2.bid.repository;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Long> {
    // 특정 상품의 최고 입찰 금액 조회
    Optional<Bid> findTopByProductOrderByBidAmountDesc(Product product);

    // 특정 사용자의 입찰 내역 조회
    List<Bid> findByMemberId(Long memberId);

    void deleteByProduct(Product product);
}
