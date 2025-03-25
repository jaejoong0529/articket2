package kjj.articket2.member;

import kjj.articket2.auth.dto.MemberLoginResponse;
import kjj.articket2.auth.dto.MemberSignUpRequest;
import kjj.articket2.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public  class MemberConverter {

    public static Member fromDto(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {//dto->entity
        return Member.builder()
                .id(request.getId())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .money(0) // 기본값 설정
                .dateJoined(LocalDateTime.now()) // 기본값 설정
                .lastLogin(LocalDateTime.now()) // 기본값 설정
                .build();
    }

    public static MemberLoginResponse toLoginResponse(String accessToken, String refreshToken) {//응답 데이터를 생성
        return MemberLoginResponse.builder()
                .message("로그인 성공")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
