package kjj.articket2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductCreateResponse {
    private Long id;
    private String productName;
    private String description;
    private Integer price;
//    private String image;
    private LocalDateTime createdAt;
}
