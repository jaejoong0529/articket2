package kjj.articket2.product.dto;

import kjj.articket2.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Builder
@Getter
public class ProductCreateRequest {
//    private Long memberId;

    private String productName;
    private String description;
    private Integer price;
//    private String image;
//    private LocalDateTime createdAt;

}
