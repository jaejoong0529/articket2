package kjj.articket2.product.controller;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.product.domain.Category;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductDetailResponse;
import kjj.articket2.product.dto.ProductResponse;
import kjj.articket2.product.dto.ProductUpdateRequest;
import kjj.articket2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000") // React와 연결
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    //상품등록
    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createProduct(
            @RequestPart("request") ProductCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.createProduct(request, image, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 성공적으로 등록되었습니다.");
    }

    //상품 전체 조회
    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    //카테고리별 상품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Category category) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Long id) {
        ProductDetailResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // 상품 삭제 (판매자만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.deleteProduct(id, userDetails);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    // 상품 수정(판매자만 가능)
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @RequestPart("request") ProductUpdateRequest request,
                                                @RequestPart(value = "image", required = false) MultipartFile image,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.updateProduct(id, request, image, userDetails);
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }
}