package kjj.articket2.transaction.domain;

import jakarta.persistence.*;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드 포함 생성자
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) // 입찰한 상품
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY) // 입찰한 사용자
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;
    @ManyToOne(fetch = FetchType.LAZY) // 판매한 사용자
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;
    private int price; // 거래 금액
    private LocalDateTime tradeTime; // 거래 시각
}
