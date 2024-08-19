package lazy.demo.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // Đặt số lượng thread trong pool
        scheduler.setThreadNamePrefix("CronJob-");
        scheduler.setAwaitTerminationSeconds(60);  // Thời gian chờ để dừng thread khi ứng dụng kết thúc
        scheduler.setWaitForTasksToCompleteOnShutdown(true);  // Đợi các task hoàn thành khi shutdown
        return scheduler;
    }
}