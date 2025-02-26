package kjj.articket2.member;

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
    public static MemberLoginResponse toLoginResponse() {
        return MemberLoginResponse.builder()
                .message("로그인 성공")
                .build();
    }
    public static MemberFindUsernameResponse toFindUsernameResponse(Member member) {
        return MemberFindUsernameResponse.builder()
                .username(member.getUsername())
                .message("아이디가 이메일로 전송되었습니다.")
                .build();
    }
    public static MemberFindPasswordResponse toFindPasswordResponse() {
        return MemberFindPasswordResponse.builder()
                .message("임시 비밀번호가 이메일로 전송되었습니다.")
                .build();
    }

    public static MemberChangePasswordResponse toChangePasswordResponse() {
        return MemberChangePasswordResponse.builder()
                .message("비밀번호가 변경되었습니다.")
                .build();
    }

}
