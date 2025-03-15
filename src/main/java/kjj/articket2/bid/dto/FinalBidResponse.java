package kjj.articket2.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalBidResponse {
    private Long productId;
    private Long winningBidId;
    private Long winnerId;
    private Integer finalPrice;
    private boolean isSold;
}
