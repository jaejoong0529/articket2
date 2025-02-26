package kjj.articket2.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFindUsernameResponse {
    private String username;
    private String message;
}
