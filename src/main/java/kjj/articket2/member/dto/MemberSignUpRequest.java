package kjj.articket2.member.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequest {
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
