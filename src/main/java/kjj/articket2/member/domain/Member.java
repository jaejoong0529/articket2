package kjj.articket2.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private int money;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;

    @PrePersist
    public void initDates() {
        this.dateJoined = this.dateJoined == null ? LocalDateTime.now() : this.dateJoined;
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

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateLastLogin(LocalDateTime now) {
        this.lastLogin = now;
    }
}
