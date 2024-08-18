package lazy.demo.auth_service.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private int jwtRefreshExpiration;


    // Tạo JWT từ thông tin người dùng (authentication)
    public String generateToken(String username) {
        return generateTokenWithUsername(username, jwtExpiration);
    }

    // Tạo refresh token
    public String generateRefreshToken(String username) {
        return generateTokenWithUsername(username, jwtRefreshExpiration);
    }

    // Tạo JWT với username, được dùng cho cả access token và refresh token
    public String generateTokenWithUsername(String username, int expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusSeconds(expiration);

        // Tạo claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("createdDate", now.toString());
        claims.put("expirationDate", expirationTime.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(getSignKey(jwtSecret))
                .compact();
    }

    public boolean validateJwtToken(String token, String authenticatedUsername) {
        String usernameFromToken = getUserNameFromJwtToken(token);

        boolean equalsUsername = usernameFromToken.equals(authenticatedUsername);
        boolean tokenExpired = isTokenExpired(token);

        return equalsUsername && !tokenExpired;
    }

    public boolean validateJwtRefreshToken(String token) {
        boolean tokenExpired = isTokenExpired(token);
        return !tokenExpired;
    }

    private boolean isTokenExpired(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey(jwtSecret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String expirationDateStr = (String) claims.get("expirationDate");
            LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr);

            return LocalDateTime.now().isAfter(expirationDate);
        } catch (Exception e) {
            return true;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Làm mới access token bằng cách sử dụng refresh token
    public String refreshAccessToken(String refreshToken) {

        if (validateJwtRefreshToken(refreshToken)) {
            String username = getUserNameFromJwtToken(refreshToken);
            return generateTokenWithUsername(username, jwtExpiration);
        }
        throw new RuntimeException("Refresh token không hợp lệ.");
    }

    private Key getSignKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

}