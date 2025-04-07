package kjj.articket2.auth.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequest {
    private String name;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
}
