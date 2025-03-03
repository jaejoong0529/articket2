package kjj.articket2.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberSignUpResponse {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String dateJoined;
    private String lastLogin;
}
