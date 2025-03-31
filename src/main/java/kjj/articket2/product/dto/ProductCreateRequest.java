package kjj.articket2.product.dto;

import kjj.articket2.product.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateRequest {
    private String productName;
    private String description;
    private Integer price;
    private Integer buyNowPrice;
    private Category category;  // 추가
}
