package lazy.demo.auth_service.config.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lazy.demo.auth_service.model.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    public JwtTokenDTO generateToken(String username) {
        return generateTokenWithUsername(username, jwtExpiration);
    }

    // Tạo refresh token
    public JwtTokenDTO generateRefreshToken(String username) {
        return generateTokenWithUsername(username, jwtRefreshExpiration);
    }

    // Tạo JWT với username, được dùng cho cả access token và refresh token
    public JwtTokenDTO generateTokenWithUsername(String username, int expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusSeconds(expiration);

        // Tạo claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("createdDate", now.toString());
        claims.put("expirationDate", expirationTime.toString());
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(getSignKey(jwtSecret))
                .compact();

        return new JwtTokenDTO(token, expirationTime);
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



    private Key getSignKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }


}