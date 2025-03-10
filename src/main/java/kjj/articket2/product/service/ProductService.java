package kjj.articket2.product.service;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.ProductConverter;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductResponse;
import kjj.articket2.product.dto.ProductUpdateRequest;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public void createProduct(ProductCreateRequest request, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다");
        }

        String username = userDetails.getUsername(); // 사용자 이름 가져오기
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Product product = ProductConverter.fromDto(request, member).toBuilder()
                .createdAt(LocalDateTime.now())
                .build();

        productRepository.save(product);
    }

    // ✅ 상품 목록 조회
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductConverter::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 상품 상세 조회
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        return ProductConverter.fromEntity(product);
    }

    // ✅ 상품 삭제 (판매자만 가능)
    public void deleteProduct(Long id, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다.");
        }

        String username = userDetails.getUsername();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));

        if (!product.getMember().equals(member)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        productRepository.delete(product);
    }

    // ✅ 상품 수정 (판매자만 가능)
    public void updateProduct(Long id, ProductUpdateRequest request, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다.");
        }

        String username = userDetails.getUsername();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));

        if (!product.getMember().equals(member)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 상품 정보 업데이트
        Product updatedProduct = product.toBuilder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        productRepository.save(updatedProduct);
    }
}

