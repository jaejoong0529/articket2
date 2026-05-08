package kjj.articket2.global.jwt;

import org.springframework.data.repository.CrudRepository;

public interface BlacklistedTokenRepository extends CrudRepository<BlacklistedToken, String> {
}
