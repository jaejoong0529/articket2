package kjj.articket2.bid.domain;

import jakarta.persistence.*;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드 포함 생성자
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) // 입찰한 상품
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) // 입찰한 사용자
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Integer bidAmount; // 입찰 금액

    private LocalDateTime bidTime; // 입찰 시간
    public boolean isExpired() {
        return bidTime.plusHours(24).isBefore(LocalDateTime.now());
    }
}
