package kjj.articket2.product.dto;

import kjj.articket2.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private String productName;
    private Integer price;
    private Integer buyNowPrice;
}