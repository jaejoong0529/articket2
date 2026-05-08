package kjj.articket2.bid.service;

import kjj.articket2.bid.BidConverter;
import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.*;
import kjj.articket2.bid.exception.InvalidBidException;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.domain.Role;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        validateNotSeller(product, member);

        Optional<Bid> currentHighestBid = findHighestBidWithLock(product.getId());
        validateHigherBid(request.getBidAmount(), product, currentHighestBid);
        validateSufficientFunds(member, request.getBidAmount(), currentHighestBid);

        currentHighestBid.ifPresent(this::refundBidder);

        member.deductMoney(request.getBidAmount());
        memberRepository.save(member);

        Bid newBid = BidConverter.fromDto(request, member, product);
        bidRepository.save(newBid);
    }

    @Transactional(readOnly = true)
    //특정 사용자의 입찰 내역 조회
    public List<BidResponse> getUserBids(Long memberId, CustomUserDetails userDetails) {
        Member authenticatedMember = getAuthenticatedMember(userDetails);
        validateBidHistoryAccess(memberId, authenticatedMember);

        return bidRepository.findByMemberId(memberId).stream()
                .map(BidConverter::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    //현재 최고 입찰가 조회
    public Integer getHighestBid(Long productId) {
        List<Bid> topBids = bidRepository.findTopByProductId(productId, PageRequest.of(0, 1));
        return topBids.isEmpty() ? DEFAULT_HIGHEST_BID : topBids.get(0).getBidAmount();
    }

    @Transactional
    //즉시구매
    public void buyProduct(BuyRequest request, CustomUserDetails userDetails) {
        Member buyer = getAuthenticatedMember(userDetails);
        Product product = findProductByIdWithLock(request.getProductId());
        validateProductNotSold(product);
        validateNotSeller(product, buyer);
        validateBuyNowPrice(product);

        Optional<Bid> currentHighestBid = findHighestBidWithLock(product.getId());
        validateSufficientFunds(buyer, product.getBuyNowPrice(), currentHighestBid);
        currentHighestBid.ifPresent(this::refundBidder);

        buyer.deductMoney(product.getBuyNowPrice());
        memberRepository.save(buyer);
        Member seller = product.getMember();
        seller.addMoney(product.getBuyNowPrice());
        memberRepository.save(seller);
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
    private void validateSufficientFunds(Member member, int amount, Optional<Bid> currentHighestBid) {
        int refundableAmount = currentHighestBid
                .filter(bid -> Objects.equals(bid.getMember().getId(), member.getId()))
                .map(Bid::getBidAmount)
                .orElse(0);
        if (member.getMoney() + refundableAmount < amount) {
            throw new InvalidBidException("잔액이 부족합니다.");
        }
    }

    //입찰 금액이 현재 최고 입찰가보다 높은지 확인
    private void validateHigherBid(int bidAmount, Product product, Optional<Bid> currentHighestBid) {
        if (currentHighestBid.isPresent() && currentHighestBid.get().getBidAmount() >= bidAmount) {
            throw new InvalidBidException("입찰 금액은 현재 최고 입찰 금액보다 높아야 합니다.");
        }
        if (currentHighestBid.isEmpty() && product.getPrice() != null && bidAmount < product.getPrice()) {
            throw new InvalidBidException("입찰 금액은 시작가 이상이어야 합니다.");
        }
    }

    //입찰 마감 여부
    private void validateBidTimeNotExpired(Product product) {
        if (product.getEndTime() != null && product.getEndTime().isBefore(LocalDateTime.now())) {
            throw new InvalidBidException("입찰 가능 시간이 종료된 상품입니다.");
        }
    }

    private void validateBuyNowPrice(Product product) {
        if (product.getBuyNowPrice() == null || product.getBuyNowPrice() <= 0) {
            throw new InvalidBidException("즉시 구매가 설정되지 않은 상품입니다.");
        }
    }

    private void validateNotSeller(Product product, Member member) {
        if (Objects.equals(product.getMember().getId(), member.getId())) {
            throw new InvalidBidException("판매자는 자신의 상품에 입찰하거나 구매할 수 없습니다.");
        }
    }

    private void validateBidHistoryAccess(Long memberId, Member authenticatedMember) {
        boolean isOwner = Objects.equals(memberId, authenticatedMember.getId());
        boolean isAdmin = authenticatedMember.getRole() == Role.ROLE_ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AuthenticationException("본인의 입찰 내역만 조회할 수 있습니다.");
        }
    }

    private Optional<Bid> findHighestBidWithLock(Long productId) {
        return bidRepository.findTopByProductIdWithLock(productId, PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }

    private void refundBidder(Bid bid) {
        Member previousBidder = bid.getMember();
        previousBidder.addMoney(bid.getBidAmount());
        memberRepository.save(previousBidder);
    }
}
