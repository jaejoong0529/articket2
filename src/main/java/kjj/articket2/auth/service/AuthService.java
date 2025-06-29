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
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenValidator tokenValidator;

    //회원가입
    @Transactional
    public void signup(MemberSignUpRequest request) {
        validateDuplicateUser(request);
        Member member = MemberConverter.fromDto(request, passwordEncoder);
        memberRepository.save(member);
    }

    //로그인
    @Transactional
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        passwordMatches(request.getPassword(), member.getPassword());
        member.updateLastLogin(LocalDateTime.now());
        String role = member.getRole().name();
        String accessToken = jwtUtil.generateAccessToken(request.getUsername(),role);
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        refreshTokenRepository.save(new RefreshToken(request.getUsername(), refreshToken));
        return MemberConverter.toLoginResponse(accessToken, refreshToken);
    }

    @Transactional
    //로그아웃
    public void logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null) return;
        String username = jwtUtil.getUsernameFromToken(token);
        refreshTokenRepository.deleteById(username);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public TokenResponse refreshToken(TokenRequest request) {
        String oldRefreshToken = request.getRefreshToken();
        validateRefreshToken(oldRefreshToken);

        String username = jwtUtil.getUsernameFromToken(oldRefreshToken);
        RefreshToken storedToken = getStoredToken(username);
        validateStoredToken(oldRefreshToken, storedToken);

        String role = jwtUtil.getRoleFromToken(oldRefreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        refreshTokenRepository.save(new RefreshToken(username, newRefreshToken));

        return new TokenResponse(newAccessToken, newRefreshToken);
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
