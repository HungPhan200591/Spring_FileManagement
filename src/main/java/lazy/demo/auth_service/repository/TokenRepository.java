package lazy.demo.auth_service.repository;

import jakarta.transaction.Transactional;
import lazy.demo.auth_service.model.Token;
import lazy.demo.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenAndRevokeFalse(String token);
    Optional<Token> findByRefreshTokenAndRevokeFalse(String refreshToken);
    void deleteByToken(String token);

    Optional<Token> findByUserAndRefreshTokenAndRevokeFalse(User user, String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoke = true WHERE t.deviceId = :deviceId AND t.deviceType = :deviceType AND t.revoke = false")
    void updateRevokeByDeviceIdAndDeviceType(String deviceId, String deviceType);

    void deleteByRevokeTrue();
}
