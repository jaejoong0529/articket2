package kjj.articket2.admin.dto;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberResponseDto {
    private Long memberId;
    private String username;
    private String email;
    private String role;
}
