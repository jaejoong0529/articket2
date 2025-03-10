package kjj.articket2.bid.service;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.exception.InvalidBidException;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public void bidProduct(BidRequest request, String accessToken) {
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            throw new RuntimeException("허용되지 않았습니다");
        }
        String username = jwtUtil.getUsernameFromToken(accessToken);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 최고 입찰가 가져오기
        Optional<Bid> highestBid = bidRepository.findTopByProductOrderByBidAmountDesc(product);

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

    // ✅ 내 입찰 내역 조회 (username 사용)
    public List<Bid> getUserBids(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        return bidRepository.findByMember(member);
    }
}

