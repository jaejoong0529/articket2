package kjj.articket2.bid.service;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.dto.BidResponse;
import kjj.articket2.bid.dto.BuyRequest;
import kjj.articket2.bid.dto.FinalBidRequest;
import kjj.articket2.bid.exception.InvalidBidException;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public void bidProduct(BidRequest request, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다");
        }
        String username = userDetails.getUsername(); // 사용자 이름 가져오기
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 최고 입찰가 가져오기
        Optional<Bid> highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);

        if (member.getMoney() < request.getBidAmount()) {
            throw new InvalidBidException("잔액이 부족합니다.");
        }

        // 현재 최고 입찰가보다 높은 금액이어야 함
        if (highestBid.isPresent() && highestBid.get().getBidAmount() >= request.getBidAmount()) {
            throw new InvalidBidException("입찰 금액은 현재 최고 입찰 금액보다 높아야 합니다.");
        }

        // 입찰 저장
        Bid bid = Bid.builder()
                .product(product)
                .member(member)
                .bidAmount(request.getBidAmount())
                .bidTime(LocalDateTime.now())
                .build();

        bidRepository.save(bid);
    }

    public void finalBid(FinalBidRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));
        Optional<Bid> highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);
        if (highestBid.isEmpty() || !highestBid.get().isExpired()) {
            LocalDateTime bidEndTime = highestBid.get().getBidTime().plusHours(24);
            Duration remainingTime = Duration.between(LocalDateTime.now(), bidEndTime);

            long hours = remainingTime.toHours();
            long minutes = remainingTime.toMinutesPart();
            throw new InvalidBidException("아직 입찰이 종료되지 않았습니다. 남은 시간: " + hours + "시간 " + minutes + "분");
        }

        Bid winningBid = highestBid.get();
        Member winner = winningBid.getMember();

        winner.deductMoney(winningBid.getBidAmount());
        memberRepository.save(winner);
        product.markAsSold();
        productRepository.save(product);
    }

    // 특정 사용자의 입찰 내역 조회 (DTO 변환)
    public List<BidResponse> getUserBids(Long memberId) {
        return bidRepository.findByMemberId(memberId).stream()
                .map(bid -> BidResponse.builder()
                        .bidId(bid.getId())
                        .productId(bid.getProduct().getId())
                        .memberId(bid.getMember().getId())
                        .bidAmount(bid.getBidAmount())
                        .bidTime(bid.getBidTime())
                        .build())
                .collect(Collectors.toList());
    }


    // 현재 최고 입찰가 조회
    public Integer getHighestBid(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        return bidRepository.findTopByProductOrderByBidAmountDesc(product)
                .map(Bid::getBidAmount)
                .orElse(0);  // 입찰이 없으면 0 반환
    }

    public void buyProduct(BuyRequest request, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다");
        }
        String username = userDetails.getUsername(); // 사용자 이름 가져오기
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));
        if (product.isSold()) {
            throw new InvalidBidException("이미 판매된 상품입니다.");
        }

        Member buyer = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        if (buyer.getMoney() < request.getBuyAmount()) {
            throw new InvalidBidException("잔액이 부족합니다.");
        }
        buyer.deductMoney(product.getBuyNowPrice());
        memberRepository.save(buyer);
        product.markAsSold();
        productRepository.save(product);
    }
}

