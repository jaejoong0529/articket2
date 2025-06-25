package kjj.articket2.product.domain;

import jakarta.persistence.*;
import kjj.articket2.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드 포함 생성자
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 판매자 정보
    @JoinColumn(name = "member_id")
    private Member member;
    private String productName;
    private String description;
    private Integer price;//입찰가격
    private Integer buyNowPrice;
    private boolean isSold; // 판매 완료 여부
    private String image;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)  // ENUM을 문자열로 저장
    private Category category;
    public void markAsSold() {
        this.isSold = true;
    }

    public void update(String productName, String description, Integer price, Integer buyNowPrice, String image, Category category) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.buyNowPrice = buyNowPrice;
        this.image = image;
        this.category = category;
    }
}
