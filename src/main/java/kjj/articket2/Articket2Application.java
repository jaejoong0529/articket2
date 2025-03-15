package kjj.articket2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@EnableScheduling
@SpringBootApplication
//        (exclude = SecurityAutoConfiguration.class)
public class Articket2Application {

    public static void main(String[] args) {
        SpringApplication.run(Articket2Application.class, args);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // 비밀번호 암호화에 BCrypt 사용
//    }
}
