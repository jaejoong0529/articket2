package kjj.articket2.global.jwt;

import kjj.articket2.global.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";

    private final JwtUtil jwtUtil;

    public TokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void validateTokenExists(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }

    public void validateUsernameExtracted(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }

    public void validateTokenMatches(String requestToken, String storedToken) {
        if (!requestToken.equals(storedToken)) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }

    public void validateTokenValidity(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }
}

