package kjj.articket2.product.dto;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Builder
@Getter
public class ProductCreateRequest {
    private String productName;
    private String description;
    private Integer price;
    private Integer buyNowPrice;
    private ProductCategory category;  // 추가


}
