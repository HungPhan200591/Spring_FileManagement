package lazy.demo.auth_service.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginReq {
    private String username;
    private String password;

    @JsonProperty("device_info")
    private LoginDeviceInfoReq deviceInfo;
}
