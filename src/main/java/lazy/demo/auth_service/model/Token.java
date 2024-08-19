package lazy.demo.auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tokens", uniqueConstraints = {
        @UniqueConstraint(name = "tokens_uq_token", columnNames = "token"),
        @UniqueConstraint(name = "tokens_uq_refresh_token", columnNames = "refresh_token")
})
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 4000)
    private String token;
    @Column(length = 4000)
    private String refreshToken;
    private LocalDateTime expirationToken;
    private LocalDateTime expirationRefreshToken;
    private String deviceId;
    private String deviceType;
    private String operatingSystem;
    private String userAgent;
    private String ipAddress;
    private boolean revoke = false;
}
