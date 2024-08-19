package lazy.demo.auth_service.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceInfoDTO {
    private String deviceId;
    private String deviceType;
    private String operatingSystem;
    private String userAgent;
}
