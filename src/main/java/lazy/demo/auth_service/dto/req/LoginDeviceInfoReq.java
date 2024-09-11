package lazy.demo.auth_service.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDeviceInfoReq {
    private String deviceId;
    private String deviceType;
    private String operatingSystem;
    private String userAgent;
}
