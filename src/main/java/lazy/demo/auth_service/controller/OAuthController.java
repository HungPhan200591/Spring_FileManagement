package lazy.demo.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lazy.demo.auth_service.dto.external.google.GoogleOAuthTokenResp;
import lazy.demo.auth_service.dto.req.OAuthGoogleLoginReq;
import lazy.demo.auth_service.dto.resp.GenericResponse;
import lazy.demo.auth_service.dto.resp.LoginResp;
import lazy.demo.auth_service.model.User;
import lazy.demo.auth_service.repository.UserRepository;
import lazy.demo.auth_service.service.OAuthService;
import lazy.demo.auth_service.util.GoogleOAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/google")
    public String getGoogleAuthorizationUrl() {
        return GoogleOAuthUtil.getAuthorizationUrl();
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam("code") String code) {
        return code;
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody OAuthGoogleLoginReq oAuthGoogleLoginReq,
                                             HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        LoginResp loginResp = oAuthService.loginWithGoogle(oAuthGoogleLoginReq, ipAddress);
        return ResponseEntity.ok(GenericResponse.success(loginResp));

    }



}
