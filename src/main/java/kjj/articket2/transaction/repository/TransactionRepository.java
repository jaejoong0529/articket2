package kjj.articket2.transaction.repository;

import kjj.articket2.member.domain.Member;
import kjj.articket2.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    // 특정 사용자의 구매 내역 조회 (buyerId 기준)
    List<Transaction> findAllByBuyer(Member buyer);

    // 특정 사용자의 판매 내역 조회 (sellerId 기준)
    List<Transaction> findAllBySeller(Member seller);

    // 최근 거래 개수 (예: 최근 7일)
    long countByTradeTimeAfter(LocalDateTime time);
}
