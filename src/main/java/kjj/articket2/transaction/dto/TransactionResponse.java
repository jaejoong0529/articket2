package kjj.articket2.transaction.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.transaction.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {
    private Long transactionId;
    private String productName;
    private int price;
    private LocalDateTime transactionDate;
    private String counterparty; // 구매자 or 판매자

    public TransactionResponse(Transaction transaction, boolean isBuyer) {
        this.transactionId = transaction.getId();
        this.productName = transaction.getProduct().getProductName();
        this.price = transaction.getPrice();
        this.transactionDate = transaction.getTradeTime();
        this.counterparty = isBuyer ? transaction.getProduct().getMember().getUsername() // 판매자 이름
                : transaction.getBuyer().getUsername(); // 구매자 이름
    }
}

