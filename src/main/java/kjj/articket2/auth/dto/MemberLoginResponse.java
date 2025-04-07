package kjj.articket2.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberLoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
