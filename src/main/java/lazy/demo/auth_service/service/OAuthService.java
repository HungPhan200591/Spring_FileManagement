package lazy.demo.auth_service.service;

public interface OAuthService {
    String exchangeCodeForToken(String code);
}
