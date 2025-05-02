package kjj.articket2.admin.dto;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductResponseDto {
    private Long productId;
    private String productName;
    private Integer price;
    private Integer buyNowPrice;
    private String image;
}
