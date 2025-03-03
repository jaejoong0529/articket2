package kjj.articket2.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kjj.articket2.member.MemberConverter;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter @Setter
@Builder(toBuilder = true)
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드 포함 생성자
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;

    /**
     *회원가입 날짜
     */
    public String dateJoinedFormatted() {
        return formatted(dateJoined);
    }

    /**
     *마지막 로그인
     */
    public String lastLoginFormatted() {
        return formatted(lastLogin);
    }


    private String formatted(LocalDateTime dateTime) {//LocalDateTime 객체를 받아서 지정된 형식으로 문자열을 반환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
