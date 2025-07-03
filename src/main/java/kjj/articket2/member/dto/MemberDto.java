package kjj.articket2.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String username;
    private String nickname;
    private String email;
    private String name;
    private int money;
    private String role;
}
