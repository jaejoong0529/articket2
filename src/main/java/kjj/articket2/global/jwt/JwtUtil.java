package kjj.articket2.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final long accessTokenExpiration = 1000 * 60 * 60; // 1시간
    private final long refreshTokenExpiration = 1000 * 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {//디코딩,여기서 key는 서명 및 검증에 사용
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    //Access Token 생성 (1시간)
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)//주체설정
                .setIssuedAt(new Date())//발행시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))//만료시간
                .signWith(SignatureAlgorithm.HS256, key)//토큰서명
                .compact();//문자열로 압축

    }


    //Refresh Token 생성 (7일)
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    //JWT에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {//키값을 사용해서 추출
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    //유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}

