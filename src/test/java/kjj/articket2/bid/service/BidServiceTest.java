package kjj.articket2.bid.service;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.dto.BuyRequest;
import kjj.articket2.bid.exception.InvalidBidException;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.domain.Role;
import kjj.articket2.member.exception.AuthenticationException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.domain.Category;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.repository.ProductRepository;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BidService bidService;

    @Test
    void bidProduct_refundsPreviousHighestBidderAndDeductsNewBidder() {
        Member seller = member(1L, "seller", 0, Role.ROLE_USER);
        Member previousBidder = member(2L, "previous", 0, Role.ROLE_USER);
        Member bidder = member(3L, "bidder", 1_000, Role.ROLE_USER);
        Product product = product(10L, seller, 100, 500, false, LocalDateTime.now().plusHours(1));
        Bid previousBid = bid(100L, product, previousBidder, 150);

        when(memberRepository.findByUsername("bidder")).thenReturn(Optional.of(bidder));
        when(productRepository.findByIdWithLock(10L)).thenReturn(Optional.of(product));
        when(bidRepository.findTopByProductIdWithLock(eq(10L), any(Pageable.class))).thenReturn(List.of(previousBid));

        bidService.bidProduct(
                BidRequest.builder().productId(10L).bidAmount(200).build(),
                new CustomUserDetails(bidder)
        );

        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidCaptor.capture());
        assertThat(previousBidder.getMoney()).isEqualTo(150);
        assertThat(bidder.getMoney()).isEqualTo(800);
        assertThat(bidCaptor.getValue().getBidAmount()).isEqualTo(200);
        assertThat(bidCaptor.getValue().getMember()).isEqualTo(bidder);
    }

    @Test
    void bidProduct_rejectsBidBelowStartPrice() {
        Member seller = member(1L, "seller", 0, Role.ROLE_USER);
        Member bidder = member(2L, "bidder", 1_000, Role.ROLE_USER);
        Product product = product(10L, seller, 100, 500, false, LocalDateTime.now().plusHours(1));

        when(memberRepository.findByUsername("bidder")).thenReturn(Optional.of(bidder));
        when(productRepository.findByIdWithLock(10L)).thenReturn(Optional.of(product));
        when(bidRepository.findTopByProductIdWithLock(eq(10L), any(Pageable.class))).thenReturn(List.of());

        assertThatThrownBy(() -> bidService.bidProduct(
                BidRequest.builder().productId(10L).bidAmount(90).build(),
                new CustomUserDetails(bidder)
        )).isInstanceOf(InvalidBidException.class)
                .hasMessage("입찰 금액은 시작가 이상이어야 합니다.");
    }

    @Test
    void buyProduct_refundsHighestBidderCreatesTransactionAndMarksProductSold() {
        Member seller = member(1L, "seller", 0, Role.ROLE_USER);
        Member highestBidder = member(2L, "highest", 0, Role.ROLE_USER);
        Member buyer = member(3L, "buyer", 700, Role.ROLE_USER);
        Product product = product(10L, seller, 100, 500, false, LocalDateTime.now().plusHours(1));
        Bid highestBid = bid(100L, product, highestBidder, 250);

        when(memberRepository.findByUsername("buyer")).thenReturn(Optional.of(buyer));
        when(productRepository.findByIdWithLock(10L)).thenReturn(Optional.of(product));
        when(bidRepository.findTopByProductIdWithLock(eq(10L), any(Pageable.class))).thenReturn(List.of(highestBid));

        bidService.buyProduct(
                BuyRequest.builder().productId(10L).build(),
                new CustomUserDetails(buyer)
        );

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        verify(bidRepository).deleteByProduct(product);
        assertThat(highestBidder.getMoney()).isEqualTo(250);
        assertThat(buyer.getMoney()).isEqualTo(200);
        assertThat(seller.getMoney()).isEqualTo(500);
        assertThat(product.isSold()).isTrue();
        assertThat(transactionCaptor.getValue().getBuyer()).isEqualTo(buyer);
        assertThat(transactionCaptor.getValue().getSeller()).isEqualTo(seller);
        assertThat(transactionCaptor.getValue().getPrice()).isEqualTo(500);
    }

    @Test
    void getUserBids_rejectsOtherUserHistoryForNormalUser() {
        Member currentUser = member(1L, "current", 0, Role.ROLE_USER);
        when(memberRepository.findByUsername("current")).thenReturn(Optional.of(currentUser));

        assertThatThrownBy(() -> bidService.getUserBids(2L, new CustomUserDetails(currentUser)))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("본인의 입찰 내역만 조회할 수 있습니다.");
    }

    private Member member(Long id, String username, int money, Role role) {
        return Member.builder()
                .id(id)
                .name(username)
                .username(username)
                .password("encoded")
                .nickname(username)
                .email(username + "@example.com")
                .phoneNumber("010-0000-0000")
                .role(role)
                .money(money)
                .build();
    }

    private Product product(Long id, Member seller, int price, int buyNowPrice, boolean isSold, LocalDateTime endTime) {
        return Product.builder()
                .id(id)
                .member(seller)
                .productName("맥북")
                .description("중고 노트북")
                .price(price)
                .buyNowPrice(buyNowPrice)
                .isSold(isSold)
                .category(Category.ELECTRONICS)
                .createdAt(LocalDateTime.now())
                .endTime(endTime)
                .build();
    }

    private Bid bid(Long id, Product product, Member bidder, int amount) {
        return Bid.builder()
                .id(id)
                .product(product)
                .member(bidder)
                .bidAmount(amount)
                .bidTime(LocalDateTime.now())
                .build();
    }
}
