package kjj.articket2.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("blacklistedToken")
public class BlacklistedToken {
    @Id
    private String token;

    @TimeToLive
    private long ttlSeconds;
}
