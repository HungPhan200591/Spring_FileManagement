package lazy.demo.auth_service.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OAuthGoogleLoginReq {
    private String code;

    @JsonProperty("device_info")
    private LoginDeviceInfoReq deviceInfo;
}
