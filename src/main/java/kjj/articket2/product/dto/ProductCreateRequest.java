package kjj.articket2.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kjj.articket2.product.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateRequest {
    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "경매 시작가는 필수입니다.")
    @Positive(message = "경매 시작가는 0보다 커야 합니다.")
    private Integer price;

    @NotNull(message = "즉시 구매가는 필수입니다.")
    @Positive(message = "즉시 구매가는 0보다 커야 합니다.")
    private Integer buyNowPrice;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;
}
