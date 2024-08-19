package lazy.demo.auth_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lazy.demo.auth_service.dto.external.google.GoogleOAuthTokenResp;
import lazy.demo.auth_service.dto.req.OAuthGoogleLoginReq;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lazy.demo.auth_service.model.Token;
import lazy.demo.auth_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
@Primary
@Transactional
public class OAuthServiceWebClientImpl implements OAuthService {

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

    @Value("${spring.security.oauth2.client.provider.google.jwk-set-uri}")
    private String jwkSetUri;

    private final WebClient webClient;
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public OAuthServiceWebClientImpl(WebClient.Builder webClientBuilder, UserService userService, TokenService tokenService) {
        this.webClient = webClientBuilder.build();
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResp loginWithGoogle(OAuthGoogleLoginReq request, String ipAddress) {
        GoogleOAuthTokenResp googleOAuthTokenResp = getGoogleOAuthTokenResp(request.getCode());
        Claims claims = decodeAndVerifyToken(googleOAuthTokenResp.getIdToken());
        User user = userService.saveOrUpdateUserOAuth(claims);
        Token token = tokenService.saveToken(user.getEmail(), request.getDeviceInfo(), ipAddress);
        return LoginResp.builder()
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
                .build();

    }

    /**
     * Tạo request body dưới dạng MultiValueMap (giống như form data)
     * BodyInserters.fromFormData() được thiết kế để làm việc với MultiValueMap vì nó biết cách chuyển đổi nó thành dữ liệu application/x-www-form-urlencoded.
     * Nếu bạn chỉ đơn thuần sử dụng Map với bodyValue(), Spring sẽ không biết phải chuyển đổi nó thành dạng form data, dẫn đến lỗi.
     * Với Map thông thường, Spring WebFlux không thể tự động xác định rằng dữ liệu cần được chuyển đổi
     * sang định dạng application/x-www-form-urlencoded. Thay vào đó, Map được xử lý như một đối tượng JSON hoặc một loại khác, dẫn đến lỗi
     */
    @Override
    public GoogleOAuthTokenResp getGoogleOAuthTokenResp(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", grantType);

        // Gửi yêu cầu và nhận phản hồi
        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GoogleOAuthTokenResp.class)
                .block(); // Sử dụng block() để nhận phản hồi đồng bộ (bạn có thể sử dụng subscribe() cho bất đồng bộ)
    }

    /**
     * Lấy danh sách các public key từ Google
     */
    public Mono<String> fetchJwks() {
        return webClient.get()
                .uri(jwkSetUri)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * Decode và verify token
     */
    @Override
    public Claims decodeAndVerifyToken(String idToken) {
        String jwks = fetchJwks().block(); // Fetch JWKS synchronously
        try {
            // Parse the JWKS
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jwksNode = objectMapper.readTree(jwks);
            JsonNode keys = jwksNode.get("keys");

            // Extract the public key
            JsonNode key = keys.get(0); // Assuming the first key is the one we need
            String n = key.get("n").asText();
            String e = key.get("e").asText();

            byte[] nBytes = Base64.getUrlDecoder().decode(n);
            byte[] eBytes = Base64.getUrlDecoder().decode(e);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                    new java.math.BigInteger(1, nBytes),
                    new java.math.BigInteger(1, eBytes)
            );

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Decode and verify the token
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode and verify token", e);
        }
    }

}
