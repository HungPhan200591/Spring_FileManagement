package lazy.demo.auth_service.dto.external.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleOAuthTokenResp {

    private String accessToken;
    private int expiresIn;
    private String tokenType;
    private String idToken;
    private String scope;
}
