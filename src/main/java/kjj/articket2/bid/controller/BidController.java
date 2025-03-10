package kjj.articket2.bid.controller;

import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.service.BidService;
import kjj.articket2.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;
    @PostMapping("/bidProduct")
    public ResponseEntity<String> bidProduct(
            @RequestBody BidRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        bidService.bidProduct(request, userDetails);
        return ResponseEntity.ok("입찰되었습니다");
    }
}
