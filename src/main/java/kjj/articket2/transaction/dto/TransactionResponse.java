package kjj.articket2.transaction.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private Long transactionId;
    private String productName;
    private int price;
    private LocalDateTime transactionDate;
    private String counterparty; // 구매자 or 판매자
}
