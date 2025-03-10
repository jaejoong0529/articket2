package kjj.articket2.bid.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BidRequest {
    private Long productId;
    private Integer bidAmount;
}
