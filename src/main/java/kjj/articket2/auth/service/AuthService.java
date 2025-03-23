package kjj.articket2.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kjj.articket2.auth.dto.MemberLoginRequest;
import kjj.articket2.auth.dto.MemberLoginResponse;
import kjj.articket2.auth.dto.MemberSignUpRequest;
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
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new InvalidUsernameException("아이디가 중복입니다.");
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new InvalidNicknameException("닉네임이 중복입니다.");
        }
        Member member = MemberConverter.fromDto(request).toBuilder()
                .password(passwordEncoder.encode(request.getPassword()))
                .dateJoined(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .money(0) // money 값을 0으로 설정
                .build();
        memberRepository.save(member);
    }

    //로그인
    public MemberLoginResponse login(MemberLoginRequest request, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        passwordMatches(request.getPassword(), member.getPassword());
        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
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
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            refreshTokenRepository.deleteById(username);
            SecurityContextHolder.clearContext();
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    //리프레시토큰
    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("유효하지않은 토큰입니다");
        }
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 토큰입니다"));

        if (!jwtUtil.validateToken(storedToken.getToken())) {
            throw new IllegalArgumentException("유효하지않은 토큰입니다");
        }
        String username = storedToken.getUsername();
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return Map.of("accessToken", newAccessToken);
    }
}
