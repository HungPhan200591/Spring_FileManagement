package lazy.demo.auth_service.cron;

import lazy.demo.auth_service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCronJob {
    private final TokenService tokenService;

    @Scheduled(cron = "0 0 1 * * ?")  // Chạy mỗi ngày vào 0:00
    public void cleanRevokedToken() {
        tokenService.cleanUpRevokedToken();
    }
}
