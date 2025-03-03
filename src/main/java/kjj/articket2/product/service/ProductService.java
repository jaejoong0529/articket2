package kjj.articket2.product.service;

import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.MemberNotFoundException;
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
    private final ProductRepository productRepository;

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public void createProduct(ProductCreateRequest request, String accessToken) {
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            throw new RuntimeException("허용되지 않았습니다");
        }

        String username = jwtUtil.getUsernameFromToken(accessToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Product product = ProductConverter.fromDto(request, member).toBuilder()
                .createdAt(LocalDateTime.now())
                .build();
        productRepository.save(product);
    }
}

