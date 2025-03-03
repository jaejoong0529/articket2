package kjj.articket2.product.controller;

import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductCreateResponse;
import kjj.articket2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductCreateRequest request,
                                                               @CookieValue(value = "accessToken", required = false)
                                                               String accessToken) {
        productService.createProduct(request, accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 성공적으로 등록되었습니다.");

    }
//    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest request,
//                                                               @SessionAttribute("member") Member member) {
//        ProductCreateResponse response = productService.createProduct(request,member);
//        return ResponseEntity.ok(response);
//    }
}