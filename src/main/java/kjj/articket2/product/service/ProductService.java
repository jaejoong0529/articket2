package kjj.articket2.product.service;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.AuthenticationException;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.ProductConverter;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.domain.Category;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductDetailResponse;
import kjj.articket2.product.dto.ProductResponse;
import kjj.articket2.product.dto.ProductUpdateRequest;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    //상품등록
    public void createProduct(ProductCreateRequest request, MultipartFile image, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        String imageUrl = (image != null && !image.isEmpty()) ? fileService.saveFile(image) : null;
        Product product = ProductConverter.fromDto(request, member, imageUrl);
        productRepository.save(product);
    }

    //상품 목록 조회
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductConverter::fromEntity)
                .collect(Collectors.toList());
    }

    //카테고리별 상품 목록 조회
    public List<ProductResponse> getProductsByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(ProductConverter::fromEntity)
                .collect(Collectors.toList());
    }

    //상품 상세 조회
    public ProductDetailResponse getProductById(Long id) {
        Product product = findProductById(id);
        return ProductConverter.fromDetailEntity(product);
    }

    //상품 삭제 (판매자만 가능)
    public void deleteProduct(Long id, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        Product product = findProductById(id);
        validateProductOwnership(product, member);
        productRepository.delete(product);
    }

    //상품 수정 (판매자만 가능)
    public void updateProduct(Long id, ProductUpdateRequest request,MultipartFile image, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        Product product = findProductById(id);
        validateProductOwnership(product, member);
        String imageUrl = (image != null && !image.isEmpty()) ? fileService.saveFile(image) : product.getImage();
        Product updatedProduct = ProductConverter.fromUpdateDto(request, product, imageUrl);
        productRepository.save(updatedProduct);
    }

    //권환 확인
    private Member getAuthenticatedMember(CustomUserDetails userDetails){
        if (userDetails == null) {
            throw new AuthenticationException("허용되지 않은 접근입니다");
        }
        return memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
    }

    //상품찾기
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    //판매자 권환
    private void validateProductOwnership(Product product, Member member) {
        if (!product.getMember().equals(member)) {
            throw new AuthenticationException("권한이 없습니다.");
        }
    }
}

