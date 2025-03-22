package kjj.articket2.global.jwt;

import kjj.articket2.member.domain.Member;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override//사용자 정보를 로드.이름에 해당하는 멤버 엔티티를 조회하고 객체생성
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다 " + username));
        return new CustomUserDetails(member);
    }
}