package lazy.demo.auth_service.util;

import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUtil {
    private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth";
    private static final String CLIENT_ID = "1017511821870-n0u5m73hla6pdduv6vna856pubpqcsk8.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2/callback";
    private static final String SCOPE = "email profile";

    public static String getAuthorizationUrl() {
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                AUTHORIZATION_URL, CLIENT_ID, REDIRECT_URI, SCOPE);
    }
}
