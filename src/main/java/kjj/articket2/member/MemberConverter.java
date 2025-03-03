package kjj.articket2.member;

import kjj.articket2.auth.dto.MemberLoginResponse;
import kjj.articket2.auth.dto.MemberSignUpRequest;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.dto.*;
import org.springframework.stereotype.Component;

@Component
public  class MemberConverter {

    public static Member fromDto(MemberSignUpRequest memberDto) {
        return Member.builder()
                .id(memberDto.getId())
                .name(memberDto.getName())
                .username(memberDto.getUsername())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .build();
    }

    public static MemberLoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return MemberLoginResponse.builder()
                .message("로그인 성공")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
