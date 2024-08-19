package lazy.demo.auth_service.controller;

import lazy.demo.auth_service.service.OAuthService;
import lazy.demo.auth_service.util.GoogleOAuthUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(@Qualifier("OAuthServiceWebClientImpl") OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/google")
    public String getGoogleAuthorizationUrl() {
        return GoogleOAuthUtil.getAuthorizationUrl();
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam("code") String code) {
        // Exchange the authorization code for an access token
        return oAuthService.exchangeCodeForToken(code);
    }



}
