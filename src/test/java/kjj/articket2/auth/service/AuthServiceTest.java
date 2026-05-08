package kjj.articket2.auth.service;

import kjj.articket2.auth.dto.TokenRequest;
import kjj.articket2.auth.dto.TokenResponse;
import kjj.articket2.global.jwt.BlacklistedTokenRepository;
import kjj.articket2.global.jwt.JwtUtil;
import kjj.articket2.global.jwt.RefreshToken;
import kjj.articket2.global.jwt.RefreshTokenRepository;
import kjj.articket2.global.jwt.TokenValidator;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.domain.Role;
import kjj.articket2.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenValidator tokenValidator;

    @InjectMocks
    private AuthService authService;

    @Test
    void refreshToken_usesCurrentMemberRoleForNewAccessToken() {
        String oldRefreshToken = "old-refresh";
        Member admin = Member.builder()
                .id(1L)
                .username("admin")
                .password("encoded")
                .role(Role.ROLE_ADMIN)
                .build();

        when(jwtUtil.getUsernameFromToken(oldRefreshToken)).thenReturn("admin");
        when(refreshTokenRepository.findById("admin")).thenReturn(Optional.of(new RefreshToken("admin", oldRefreshToken)));
        when(memberRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(jwtUtil.generateAccessToken("admin", "ROLE_ADMIN")).thenReturn("new-access");
        when(jwtUtil.generateRefreshToken("admin")).thenReturn("new-refresh");

        TokenResponse response = authService.refreshToken(
                TokenRequest.builder().refreshToken(oldRefreshToken).build()
        );

        assertThat(response.getAccessToken()).isEqualTo("new-access");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh");
        verify(jwtUtil).generateAccessToken("admin", "ROLE_ADMIN");

        ArgumentCaptor<RefreshToken> refreshTokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture());
        assertThat(refreshTokenCaptor.getValue().getUsername()).isEqualTo("admin");
        assertThat(refreshTokenCaptor.getValue().getToken()).isEqualTo("new-refresh");
    }
}
