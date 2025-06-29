package kjj.articket2.bid.controller;

import jakarta.validation.Valid;
import kjj.articket2.bid.dto.*;
import kjj.articket2.bid.service.BidService;
import kjj.articket2.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
public class BidController {
    private final BidService bidService;

    //입찰하기
    @PostMapping("/bidProduct")
    public ResponseEntity<String> bidProduct(
            @RequestBody @Valid BidRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        bidService.bidProduct(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("입찰이 완료되었습니다.");
    }

    //현재 최고 입찰가 조회
    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getHighestBid(@PathVariable Long productId) {
        return ResponseEntity.ok(bidService.getHighestBid(productId));
    }

    //특정 사용자의 입찰 내역 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BidResponse>> getUserBids(@PathVariable Long memberId) {
        List<BidResponse> bidResponses = bidService.getUserBids(memberId);
        return ResponseEntity.ok(bidResponses);
    }

    //즉시 구매
    @PostMapping("/buyProduct")
    public ResponseEntity<String> buyProduct(
            @RequestBody BuyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        bidService.buyProduct(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("즉시 구매 되었습니다.");
    }
}
