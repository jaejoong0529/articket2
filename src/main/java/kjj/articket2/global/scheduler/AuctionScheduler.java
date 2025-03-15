package kjj.articket2.global.scheduler;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final MemberRepository memberRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행 (실제 운영에서는 1시간 or 10분마다 실행 추천)
    @Transactional
    public void closeExpiredBids() {
        log.info("📢 경매 마감 스케줄러 실행 중...");

        // 경매 시간이 종료된 상품 찾기 (현재 시간보다 종료 시간이 이전인 상품)
        List<Product> expiredProducts = productRepository.findAllByEndTimeBeforeAndIsSoldFalse(LocalDateTime.now());

        for (Product product : expiredProducts) {
            Optional<Bid> highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);

            if (highestBid.isPresent()) {
                // 최고 입찰자가 존재하면 최종 입찰자로 낙찰 처리
                Bid winningBid = highestBid.get();
                Member winner = winningBid.getMember();

                // 구매자의 잔액 차감
                winner.deductMoney(winningBid.getBidAmount());
                memberRepository.save(winner);

                log.info("🎉 상품 {}이(가) {}님에게 {}원에 낙찰됨!", product.getId(), winner.getUsername(), winningBid.getBidAmount());
            } else {
                log.info("⏳ 상품 {}의 경매가 종료되었으나 입찰자가 없음.", product.getId());
            }

            // 상품을 판매 완료 상태로 변경
            product.markAsSold();
            productRepository.save(product);
        }
    }
}

