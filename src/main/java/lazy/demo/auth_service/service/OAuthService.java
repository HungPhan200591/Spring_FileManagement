package lazy.demo.auth_service.service;

import io.jsonwebtoken.Claims;
import lazy.demo.auth_service.dto.external.google.GoogleOAuthTokenResp;
import lazy.demo.auth_service.dto.req.OAuthGoogleLoginReq;
import lazy.demo.auth_service.dto.resp.LoginResp;

public interface OAuthService {
    LoginResp loginWithGoogle(OAuthGoogleLoginReq oAuthGoogleLoginReq, String ipAddress);
    GoogleOAuthTokenResp getGoogleOAuthTokenResp(String code);
    Claims decodeAndVerifyToken(String idToken);

}
