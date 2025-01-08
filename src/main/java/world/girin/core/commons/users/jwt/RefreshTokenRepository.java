package world.girin.core.commons.users.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Boolean existsByUsername(String username);

    @Transactional
    void deleteAllByusername(String username);

    Boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);

}
