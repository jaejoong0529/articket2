package kjj.articket2.global.scheduler;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.repository.ProductRepository;
import kjj.articket2.transaction.TransactionConverter;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    @Transactional
    public void closeExpiredBids() {
        log.info("경매 마감 스케줄러 실행 중...");

        // 경매 시간이 종료된 상품 찾기 (현재 시간보다 종료 시간이 이전인 상품)
        List<Product> expiredProducts = productRepository.findAllByEndTimeBeforeAndIsSoldFalse(LocalDateTime.now());

        for (Product product : expiredProducts) {
            List<Bid> highestBid = bidRepository.findTopByProductIdWithLock(product.getId(), PageRequest.of(0, 1));

            if (!highestBid.isEmpty()) {
                // 최고 입찰자가 존재하면 최종 입찰자로 낙찰 처리
                Bid winningBid = highestBid.get(0);
                Member winner = winningBid.getMember();
                Member seller = product.getMember();

                // 거래 내역 저장
                Transaction trade = TransactionConverter.createTrade(winner, seller, product, winningBid.getBidAmount());
                transactionRepository.save(trade);
                seller.addMoney(winningBid.getBidAmount());
                memberRepository.save(seller);

                log.info("상품 {}이(가) {}님에게 {}원에 낙찰됨!", product.getId(), winner.getUsername(), winningBid.getBidAmount());
            } else {
                log.info("상품 {}의 경매가 종료되었으나 입찰자가 없음.", product.getId());
            }

            // 상품을 판매 완료 상태로 변경
            product.markAsSold();
            productRepository.save(product);
        }
    }
}
