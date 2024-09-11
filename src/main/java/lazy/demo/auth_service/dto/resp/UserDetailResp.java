package lazy.demo.auth_service.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lazy.demo.auth_service.enums.UserProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResp {
    private Long userId;
    private String username;
    private String email;
    private boolean isAdmin;
    private LocalDate dateOfBirth;
}
