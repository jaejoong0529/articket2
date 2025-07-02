package kjj.articket2.member.repository;

import kjj.articket2.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByNameAndEmail(String name, String email);
    Optional<Member> findByUsernameAndEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    // 이름/닉네임/이메일로 검색
    List<Member> findByUsernameContainingOrNicknameContainingOrEmailContaining(String username, String nickname, String email);

    // 최근 7일 이내 가입자 수
    long countByDateJoinedAfter(LocalDateTime date);
}
