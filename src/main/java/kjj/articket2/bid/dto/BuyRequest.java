package kjj.articket2.bid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuyRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;
}
