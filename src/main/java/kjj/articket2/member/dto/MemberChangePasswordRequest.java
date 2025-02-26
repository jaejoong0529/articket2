package kjj.articket2.member.dto;

import lombok.Getter;

@Getter
public class MemberChangePasswordRequest {
    private String username;
    private String currentPassword;
    private String newPassword;
}
