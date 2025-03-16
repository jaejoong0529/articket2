package kjj.articket2.product.controller;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductDetailResponse;
import kjj.articket2.product.dto.ProductResponse;
import kjj.articket2.product.dto.ProductUpdateRequest;
import kjj.articket2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductCreateRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails)
                                                               {
        productService.createProduct(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 성공적으로 등록되었습니다.");
    }
    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
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
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @RequestBody ProductUpdateRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.updateProduct(id, request, userDetails);
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }
}