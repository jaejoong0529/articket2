package kjj.articket2.bid;

import kjj.articket2.bid.domain.Bid;
import kjj.articket2.bid.dto.BidRequest;
import kjj.articket2.bid.dto.BidResponse;
import kjj.articket2.member.domain.Member;
import kjj.articket2.product.domain.Product;
import kjj.articket2.product.dto.ProductCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BidConverter {
    public static Bid fromDto(BidRequest request, Member member, Product product) {
        return Bid.builder()
                .product(product)
                .member(member)
                .bidAmount(request.getBidAmount())
                .bidTime(LocalDateTime.now())
                .build();
    }
    public static BidResponse fromEntity(Bid bid) {
        return BidResponse.builder()
                .bidId(bid.getId())
                .productId(bid.getProduct().getId())
                .memberId(bid.getMember().getId())
                .bidAmount(bid.getBidAmount())
                .bidTime(bid.getBidTime())
                .build();
    }
}
