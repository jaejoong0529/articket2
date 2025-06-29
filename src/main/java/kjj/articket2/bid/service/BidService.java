package kjj.articket2.bid.service;

import kjj.articket2.bid.BidConverter;
import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.*;
import kjj.articket2.bid.exception.InvalidBidException;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.AuthenticationException;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import kjj.articket2.transaction.TransactionConverter;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private static final int DEFAULT_HIGHEST_BID = 0;

    @Transactional
    //상품 입찰
    public void bidProduct(BidRequest request, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        Product product = findProductByIdWithLock(request.getProductId());
        validateProductNotSold(product);
        validateBidTimeNotExpired(product);
        validateSufficientFunds(member, request.getBidAmount());
        validateHigherBid(request.getBidAmount(), request.getProductId());
        // 💡 기존 최고 입찰자 환불 처리
        Optional<Bid> highestBidOpt = bidRepository.findTopByProductIdOrderByBidAmountDescWithLock(product.getId());
        if (highestBidOpt.isPresent()) {
            Bid previousBid = highestBidOpt.get();
            Member previousBidder = previousBid.getMember();

            previousBidder.addMoney(previousBid.getBidAmount());
            memberRepository.save(previousBidder);
        }

        // 💸 새 입찰자 금액 차감
        member.deductMoney(request.getBidAmount());
        memberRepository.save(member);

        Bid newBid = BidConverter.fromDto(request, member, product);
        bidRepository.save(newBid);
    }

    @Transactional(readOnly = true)
    //특정 사용자의 입찰 내역 조회
    public List<BidResponse> getUserBids(Long memberId) {
        return bidRepository.findByMemberId(memberId).stream()
                .map(BidConverter::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    //현재 최고 입찰가 조회
    public Integer getHighestBid(Long productId) {
        return bidRepository.findTopByProductIdOrderByBidAmountDescWithLock(productId)
                .map(Bid::getBidAmount)
                .orElse(DEFAULT_HIGHEST_BID);
    }

    @Transactional
    //즉시구매
    public void buyProduct(BuyRequest request, CustomUserDetails userDetails) {
        Member buyer = getAuthenticatedMember(userDetails);
        Product product = findProductByIdWithLock(request.getProductId());
        validateProductNotSold(product);
        validateSufficientFunds(buyer, product.getBuyNowPrice());
        buyer.deductMoney(product.getBuyNowPrice());
        memberRepository.save(buyer);
        product.markAsSold();
        productRepository.save(product);
        Transaction trade = TransactionConverter.createTrade(buyer, product.getMember(), product, product.getBuyNowPrice());

        transactionRepository.save(trade);
        bidRepository.deleteByProduct(product);
    }

    //권환 확인
    private Member getAuthenticatedMember(CustomUserDetails userDetails){
        if (userDetails == null) {
            throw new AuthenticationException("허용되지 않은 접근입니다");
        }
        return memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
    }

    //상품찾기
    private Product findProductByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    //상품이 이미 판매되었는지 확인
    private void validateProductNotSold(Product product) {
        if (product.isSold()) {
            throw new InvalidBidException("이미 판매된 상품입니다.");
        }
    }

    //잔액 확인
    private void validateSufficientFunds(Member member, int amount) {
        if (member.getMoney() < amount) {
            throw new InvalidBidException("잔액이 부족합니다.");
        }
    }

    //입찰 금액이 현재 최고 입찰가보다 높은지 확인
    private void validateHigherBid(int bidAmount, Long productId) {
        Optional<Bid> highestBid = bidRepository.findTopByProductIdOrderByBidAmountDescWithLock(productId);
        if (highestBid.isPresent() && highestBid.get().getBidAmount() >= bidAmount) {
            throw new InvalidBidException("입찰 금액은 현재 최고 입찰 금액보다 높아야 합니다.");
        }
    }

    //입찰 마감 여부
    private void validateBidTimeNotExpired(Product product) {
        if (product.getEndTime() != null && product.getEndTime().isBefore(LocalDateTime.now())) {
            throw new InvalidBidException("입찰 가능 시간이 종료된 상품입니다.");
        }
    }
}
