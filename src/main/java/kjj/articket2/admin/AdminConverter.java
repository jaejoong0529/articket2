package kjj.articket2.admin;

import kjj.articket2.admin.dto.AdminMemberResponseDto;
import kjj.articket2.admin.dto.AdminProductResponseDto;
import kjj.articket2.admin.dto.AdminTransactionResponseDto;
import kjj.articket2.bid.domain.Bid;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.transaction.domain.Transaction;

public class AdminConverter {
    public static AdminMemberResponseDto toMemberDto(Member member) {
        return AdminMemberResponseDto.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .role(String.valueOf(member.getRole()))
                .build();
    }
    public static AdminTransactionResponseDto toTransactionDto(Transaction tx) {
        return AdminTransactionResponseDto.builder()
                .transactionId(tx.getId())
                .buyer(tx.getBuyer().getUsername())
                .seller(tx.getSeller().getUsername())
                .price(tx.getPrice())
                .date(tx.getTradeTime().toString())
                .build();
    }

    public static AdminProductResponseDto toProductDto(Product product) {
        return AdminProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .buyNowPrice(product.getBuyNowPrice())
                .image(product.getImage()) // 이미지 URL 설정
                .build();
    }
}
