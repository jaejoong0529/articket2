package kjj.articket2.member.domain;

import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Builder.Default
    private int money = 0;
    @Builder.Default
    private LocalDateTime dateJoined = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime lastLogin = LocalDateTime.now();


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

    public void deductMoney(Integer amount) {
        if (this.money < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.money -= amount;
    }

    public void addMoney(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.money += amount;
    }
}
