package lazy.demo.auth_service.service;

import lazy.demo.auth_service.config.security.jwt.JwtService;
import lazy.demo.auth_service.config.security.jwt.JwtTokenDTO;
import lazy.demo.auth_service.dto.req.DeviceInfoDTO;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lazy.demo.auth_service.model.Token;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.repository.TokenRepository;
import lazy.demo.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public void revokeToken(String tokenRevoke) {
        Token token = tokenRepository.findByTokenAndRevokeFalse(tokenRevoke)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        token.setRevoke(true);
        tokenRepository.save(token);
    }

    public void revokeRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshTokenAndRevokeFalse(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        token.setRevoke(true);
        tokenRepository.save(token);
    }

    public Token saveToken(String authenticatedUsername, DeviceInfoDTO deviceInfo, String ipAddress) {

        String deviceId = deviceInfo.getDeviceId();
        String deviceType = deviceInfo.getDeviceType();
        String operatingSystem = deviceInfo.getOperatingSystem();
        String userAgent = deviceInfo.getUserAgent();

        tokenRepository.updateRevokeByDeviceIdAndDeviceType(deviceId, deviceType);

        User user = userService.findUserByUserNameOrEmail(authenticatedUsername, authenticatedUsername);
        JwtTokenDTO jwtToken = jwtService.generateToken(authenticatedUsername);
        JwtTokenDTO jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUsername);
        Token token = Token.builder()
                .user(user)
                .token(jwtToken.getToken())
                .expirationToken(jwtToken.getExpirationDateTime())
                .refreshToken(jwtRefreshToken.getToken())
                .expirationRefreshToken(jwtRefreshToken.getExpirationDateTime())
                .deviceId(deviceId)
                .deviceType(deviceType)
                .operatingSystem(operatingSystem)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();

        return tokenRepository.save(token);
    }

    // Làm mới access token bằng cách sử dụng refresh token
    public LoginResp refreshAccessToken(String refreshToken) {

        if (!jwtService.validateJwtRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token has expired");
        }

        String username = jwtService.getUserNameFromJwtToken(refreshToken);
        User user = userService.findUserByUserNameOrEmail(username, username);

        Token token = tokenRepository.findByUserAndRefreshTokenAndRevokeFalse(user, refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (token.getExpirationRefreshToken().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        token.setRevoke(true);
        tokenRepository.save(token);

        JwtTokenDTO newJwtToken = jwtService.generateToken(username);
        JwtTokenDTO newJwtRefreshToken = jwtService.generateRefreshToken(username);
        String newAccToken = newJwtToken.getToken();
        String newRefreshToken = newJwtRefreshToken.getToken();

        Token newToken = Token.builder()
                .user(user)
                .token(newAccToken)
                .refreshToken(newRefreshToken)
                .expirationToken(newJwtToken.getExpirationDateTime())
                .expirationRefreshToken(newJwtRefreshToken.getExpirationDateTime())
                .deviceId(token.getDeviceId())
                .deviceType(token.getDeviceType())
                .operatingSystem(token.getOperatingSystem())
                .userAgent(token.getUserAgent())
                .ipAddress(token.getIpAddress())
                .build();

        tokenRepository.save(newToken);

        return new LoginResp(newAccToken, newRefreshToken);
    }

    @Transactional
    public void cleanUpRevokedToken() {
        tokenRepository.deleteByRevokeTrue();
    }
}
