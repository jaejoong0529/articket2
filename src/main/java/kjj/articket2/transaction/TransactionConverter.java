package kjj.articket2.transaction;

import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.dto.TransactionResponse;

import java.time.LocalDateTime;

public class TransactionConverter {
    public static Transaction createTrade(Member buyer, Member seller, Product product, int price) {
        return Transaction.builder()
                .buyer(buyer)
                .seller(seller)
                .product(product)
                .price(price)
                .tradeTime(LocalDateTime.now())
                .build();
    }
    public static TransactionResponse toResponse(Transaction transaction,boolean isBuyer) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .productName(transaction.getProduct().getProductName())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTradeTime())
                .counterparty(isBuyer
                        ? transaction.getProduct().getMember().getUsername()  // 판매자
                        : transaction.getBuyer().getUsername())              // 구매자
                .build();
    }
}
