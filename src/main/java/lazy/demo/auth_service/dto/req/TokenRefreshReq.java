package lazy.demo.auth_service.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenRefreshReq {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
