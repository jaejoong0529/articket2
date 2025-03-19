package kjj.articket2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private String productName;
    private String description;
    private Integer price;
    private Integer buyNowPrice;
    private String sellerUsername;
    private LocalDateTime createdAt;
    private String image; // 이미지 URL 추가
}
