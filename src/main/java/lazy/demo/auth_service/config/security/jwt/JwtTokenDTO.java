package lazy.demo.auth_service.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {
    String token;
    LocalDateTime expirationDateTime;
}
