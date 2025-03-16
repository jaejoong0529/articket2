package kjj.articket2.transaction.controller;

import kjj.articket2.bid.service.BidService;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.dto.TransactionResponse;
import kjj.articket2.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // 현재 최고 입찰가 조회
    @GetMapping("/bought")
    public ResponseEntity<List<TransactionResponse>> getBoughtProducts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getBoughtProducts(userDetails));
    }

    @GetMapping("/sold")
    public ResponseEntity<List<TransactionResponse>> getSoldProducts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getSoldProducts(userDetails));
    }

}
