package kjj.articket2.member.dto;

import lombok.Getter;

@Getter
public class MemberLoginRequest {
    private String username;
    private String password;
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
