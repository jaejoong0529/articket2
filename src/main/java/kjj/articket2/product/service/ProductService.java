package kjj.articket2.product.service;

import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.ProductConverter;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductCreateResponse;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request,Member member) {
        Product product = ProductConverter.fromDto(request, member).toBuilder()
                .createdAt(LocalDateTime.now())
                .build();
        productRepository.save(product);
        return ProductConverter.toCreateProductResponse(product);

    }
}
