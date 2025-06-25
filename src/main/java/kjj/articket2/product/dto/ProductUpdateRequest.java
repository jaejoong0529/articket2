package kjj.articket2.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kjj.articket2.product.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private Integer price;

    @NotNull(message = "즉시구매가는 필수입니다.")
    @Positive(message = "즉시구매가는 0보다 커야 합니다.")
    private Integer buyNowPrice;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;
}
