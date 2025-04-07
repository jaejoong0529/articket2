package kjj.articket2.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import kjj.articket2.auth.dto.*;
import kjj.articket2.global.exception.InvalidTokenException;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.global.jwt.RefreshToken;
import kjj.articket2.global.jwt.RefreshTokenRepository;
import kjj.articket2.global.jwt.TokenValidator;
import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.InvalidNicknameException;
import kjj.articket2.member.exception.InvalidPasswordException;
import kjj.articket2.member.exception.InvalidUsernameException;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kjj.articket2.global.jwt.TokenValidator.INVALID_TOKEN;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenValidator tokenValidator;


    //회원가입
    public void signup(MemberSignUpRequest request) {
        validateDuplicateUser(request);
        Member member = MemberConverter.fromDto(request, passwordEncoder);
        memberRepository.save(member);
    }

    //로그인
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        passwordMatches(request.getPassword(), member.getPassword());
        member.setLastLogin(LocalDateTime.now());
        memberRepository.save(member);
        String role = member.getRole().name();
        String accessToken = jwtUtil.generateAccessToken(request.getUsername(),role);
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        refreshTokenRepository.save(new RefreshToken(request.getUsername(), refreshToken));
        return MemberConverter.toLoginResponse(accessToken, refreshToken);
    }

    //로그아웃
    public void logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null) return;
        String username = jwtUtil.getUsernameFromToken(token);
        refreshTokenRepository.deleteById(username);
        SecurityContextHolder.clearContext();
    }

    //리프레시
    public TokenResponse refreshToken(TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        // 토큰 유효성 검사
        validateRefreshToken(refreshToken);

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        RefreshToken storedToken = getStoredToken(username);
        // 요청으로 온 토큰과 DB에 저장된 토큰이 일치하는지 확인
        validateStoredToken(refreshToken, storedToken);

        String role = jwtUtil.getRoleFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, role);

        return new TokenResponse(newAccessToken);
    }

    private void validateRefreshToken(String refreshToken) {
        tokenValidator.validateTokenExists(refreshToken);
        tokenValidator.validateUsernameExtracted(jwtUtil.getUsernameFromToken(refreshToken));
        tokenValidator.validateTokenValidity(refreshToken);
    }

    private RefreshToken getStoredToken(String username) {
        return refreshTokenRepository.findById(username)
                .orElseThrow(() -> new InvalidTokenException(INVALID_TOKEN));
    }

    private void validateStoredToken(String requestToken, RefreshToken storedToken) {
        tokenValidator.validateTokenMatches(requestToken, storedToken.getToken());
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

    //비밀번호 일치
    private void passwordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }
}