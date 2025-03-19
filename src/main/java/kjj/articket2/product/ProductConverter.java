package kjj.articket2.product;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductDetailResponse;
import kjj.articket2.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static Product fromDto(ProductCreateRequest requestDto, Member member) {
        return Product.builder()
                .member(member)
                .productName(requestDto.getProductName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .buyNowPrice(requestDto.getBuyNowPrice())
                .build();
    }
    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .buyNowPrice(product.getBuyNowPrice())
                .image(product.getImage()) // 이미지 URL 설정
                .build();
    }
    public static ProductDetailResponse fromDetailEntity(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .buyNowPrice(product.getBuyNowPrice())
                .sellerUsername(product.getMember().getUsername())
                .createdAt(product.getCreatedAt())
                .image(product.getImage()) // 이미지 URL 설정
                .build();
    }


}


