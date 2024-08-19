package lazy.demo.auth_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import lazy.demo.auth_service.dto.external.google.GoogleOAuthTokenResp;
import lazy.demo.auth_service.dto.req.OAuthGoogleLoginReq;
import lazy.demo.auth_service.dto.resp.GenericResponse;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthServiceRestTemplateImpl implements OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Override
    public LoginResp loginWithGoogle(OAuthGoogleLoginReq oAuthGoogleLoginReq, String ipAddress) {
        return null;
    }

    @Override
    public GoogleOAuthTokenResp getGoogleOAuthTokenResp(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", "1017511821870-n0u5m73hla6pdduv6vna856pubpqcsk8.apps.googleusercontent.com");
        map.add("client_secret", "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");
        map.add("redirect_uri", "http://localhost:8080/oauth2/callback");
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(tokenUri, request, GoogleOAuthTokenResp.class).getBody();
    }

    @Override
    public Claims decodeAndVerifyToken(String idToken) {
        return null;
    }
}
