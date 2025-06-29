package kjj.articket2.member.repository;

import jakarta.persistence.LockModeType;
import kjj.articket2.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByNameAndEmail(String name, String email);
    Optional<Member> findByUsernameAndEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 사용자 계정 잠금
    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findByUsernameWithLock(@Param("username") String username);
}
