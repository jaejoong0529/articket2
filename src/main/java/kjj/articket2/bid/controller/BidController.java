package kjj.articket2.bid.controller;

import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
            @CookieValue(value = "accessToken", required = false) String accessToken) {
        bidService.bidProduct(request, accessToken);
        return ResponseEntity.ok("입찰되었습니다");
    }
}
