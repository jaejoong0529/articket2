package kjj.articket2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private Integer price;
    private Integer buyNowPrice;
    private Integer highestBid;
    private String image;
    private boolean isSold;
}
