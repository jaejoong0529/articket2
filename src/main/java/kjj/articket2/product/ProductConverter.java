package kjj.articket2.product;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductCreateResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductConverter {
    public static Product fromDto(ProductCreateRequest requestDto, Member member) {
        return Product.builder()
                .member(member)
                .productName(requestDto.getProductName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .build();
    }
    public static ProductCreateResponse toCreateProductResponse(Product product) {
        return new ProductCreateResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt()
        );
    }
}


