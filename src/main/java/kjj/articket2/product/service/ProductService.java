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
import kjj.articket2.product.exception.FileStorageException;
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
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    //상품등록
    @Transactional
    public void createProduct(ProductCreateRequest request, MultipartFile image, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        String imageUrl = saveFileOrNull(image);
        Product product = ProductConverter.fromDto(request, member, imageUrl);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String search) {
        List<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository.findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(ProductConverter::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Category category, String search) {
        List<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository.findByCategoryAndProductNameContainingIgnoreCaseOrCategoryAndDescriptionContainingIgnoreCase(
                    category, search, category, search);
        } else {
            products = productRepository.findByCategory(category);
        }

        return products.stream()
                .map(ProductConverter::fromEntity)
                .collect(Collectors.toList());
    }

    //상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(Long productId) {
        Product product = findProductById(productId);
        return ProductConverter.fromDetailEntity(product);
    }

    //상품 삭제 (판매자만 가능)
    @Transactional
    public void deleteProduct(Long productId, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        Product product = findProductById(productId);
        validateProductOwnership(product, member);
        productRepository.delete(product);
    }

    //상품 수정 (판매자만 가능)
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request,MultipartFile image, CustomUserDetails userDetails) {
        Member member = getAuthenticatedMember(userDetails);
        Product product = findProductById(productId);
        validateProductOwnership(product, member);
        String imageUrl = product.getImage();
        String newImageUrl = saveFileOrNull(image);
        if (newImageUrl != null) {
            imageUrl = newImageUrl;
        }
        ProductConverter.updateEntity(product, request, imageUrl);
    }

    //파일이 없으면 null, 있으면 저장 후 URL 반환
    private String saveFileOrNull(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return fileService.saveFile(file);
        } catch (FileStorageException ex) {
            throw ex;
        }
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
    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    //판매자 권환
    private void validateProductOwnership(Product product, Member member) {
        if (!product.getMember().getId().equals(member.getId())) {
            throw new AuthenticationException("권한이 없습니다.");
        }
    }
}
