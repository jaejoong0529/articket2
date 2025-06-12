package kjj.articket2.member;

import kjj.articket2.auth.dto.MemberLoginResponse;
import kjj.articket2.auth.dto.MemberSignUpRequest;
import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.domain.Role;
import kjj.articket2.member.dto.MemberDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public  class MemberConverter {

    public static Member fromDto(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {//dto->entity
        return Member.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();
    }

    public static MemberLoginResponse toLoginResponse(String accessToken, String refreshToken) {//응답 데이터를 생성
        return MemberLoginResponse.builder()
                .message("로그인 성공")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public MemberDto toDto(CustomUserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth ->auth.getAuthority())
                .orElse("ROLE_USER");
        return MemberDto.builder()
                .username(userDetails.getUsername())
                .role(role)
                .build();
    }
}
