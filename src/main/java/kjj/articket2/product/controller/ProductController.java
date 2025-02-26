package kjj.articket2.product.controller;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.dto.ProductCreateRequest;
import kjj.articket2.product.dto.ProductCreateResponse;
import kjj.articket2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest request,
                                                               @SessionAttribute("member") Member member) {
        ProductCreateResponse response = productService.createProduct(request,member);
        return ResponseEntity.ok(response);
    }
}
