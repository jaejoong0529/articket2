package kjj.articket2.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kjj.articket2.auth.dto.MemberLoginRequest;
import kjj.articket2.auth.dto.MemberLoginResponse;
import kjj.articket2.auth.dto.MemberSignUpRequest;
import kjj.articket2.global.exception.InvalidTokenException;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.global.jwt.RefreshToken;
import kjj.articket2.global.jwt.RefreshTokenRepository;
import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.InvalidNicknameException;
import kjj.articket2.member.exception.InvalidPasswordException;
import kjj.articket2.member.exception.InvalidUsernameException;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    //회원가입
    public void signup(MemberSignUpRequest request) {
        validateDuplicateUser(request);
        Member member = MemberConverter.fromDto(request, passwordEncoder);
        memberRepository.save(member);
    }

    //중복확인
    private void validateDuplicateUser(MemberSignUpRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new InvalidUsernameException("아이디가 중복입니다.");
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new InvalidNicknameException("닉네임이 중복입니다.");
        }
    }

    //로그인
    public MemberLoginResponse login(MemberLoginRequest request, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        passwordMatches(request.getPassword(), member.getPassword());
        member.setLastLogin(LocalDateTime.now());
        memberRepository.save(member);
        String role = member.getRole().name();
        String accessToken = jwtUtil.generateAccessToken(request.getUsername(), role);
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        refreshTokenRepository.save(new RefreshToken(request.getUsername(), refreshToken));
        response.setHeader("Authorization", "Bearer " + accessToken);
        return MemberConverter.toLoginResponse(accessToken, refreshToken);
    }

    //비밀번호 일치
    private void passwordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    //로그아웃
    public void logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) return;
        String username = jwtUtil.getUsernameFromToken(token);
        refreshTokenRepository.deleteById(username);
        SecurityContextHolder.clearContext();
    }

    //토큰추출
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    // 리프레시 토큰 재발급
    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidTokenException("유효하지않은 토큰입니다1");
        }

        // 1. 토큰에서 username 추출
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        if (username == null) {
            throw new InvalidTokenException("유효하지않은 토큰입니다2");
        }

        // 2. username 기반으로 저장된 토큰 조회
        RefreshToken storedToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new InvalidTokenException("유효하지않은 토큰입니다3"));

        // 3. 저장된 토큰과 비교
        if (!storedToken.getToken().equals(refreshToken)) {
            throw new InvalidTokenException("유효하지않은 토큰입니다4");
        }

        // 4. 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException("유효하지않은 토큰입니다5");
        }

        // 5. 역할 가져오고, 새 accessToken 생성
        String role = jwtUtil.getRoleFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, role);

        return Map.of("accessToken", newAccessToken);
    }
}
