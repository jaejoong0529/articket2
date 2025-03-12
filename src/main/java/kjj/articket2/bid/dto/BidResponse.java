package kjj.articket2.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    private Long bidId;
    private Long productId;
    private Long memberId;
    private Integer bidAmount;
    private LocalDateTime bidTime;
}
