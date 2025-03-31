package kjj.articket2.product;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductDetailResponse;
import kjj.articket2.product.dto.ProductResponse;
import kjj.articket2.product.dto.ProductUpdateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductConverter {
    public static Product fromDto(ProductCreateRequest request, Member member,String imageUrl) {
        return Product.builder()
                .member(member)
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .buyNowPrice(request.getBuyNowPrice())
                .createdAt(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .image(imageUrl)
                .category(request.getCategory())  // 카테고리 추가
                .build();
    }
    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
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
                .endTime(product.getEndTime())
                .image(product.getImage()) // 이미지 URL 설정
                .build();
    }
    public static Product fromUpdateDto(ProductUpdateRequest request, Product product, String imageUrl) {
        return product.toBuilder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .image(imageUrl)
                .build();
    }
}


