package kjj.articket2.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.global.jwt.RefreshToken;
import kjj.articket2.global.jwt.RefreshTokenRepository;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.dto.MemberLoginRequest;
import kjj.articket2.member.dto.MemberLoginResponse;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberLoginRequest request, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

        refreshTokenRepository.save(new RefreshToken(request.getUsername(), refreshToken));

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("로그인 성공");
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 AccessToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    String username = jwtUtil.extractUsername(token);

                    // RefreshToken 삭제
                    refreshTokenRepository.deleteById(username);

                    // AccessToken 쿠키 삭제
                    Cookie emptyCookie = new Cookie("accessToken", null);
                    emptyCookie.setHttpOnly(true);
                    emptyCookie.setSecure(true);
                    emptyCookie.setPath("/");
                    emptyCookie.setMaxAge(0); // 즉시 만료
                    response.addCookie(emptyCookie);

                    return ResponseEntity.ok("로그아웃 성공");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃 실패: 토큰 없음");
    }
}

