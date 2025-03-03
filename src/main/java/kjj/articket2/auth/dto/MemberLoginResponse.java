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

// record 공부하기 (왜 쓰는지?)
//public record MemberLoginRequest2(
//        String username,
//        String password
//) {
//    // memberService
//    // request.getUsername (x)
//    // request.username (0)
//}