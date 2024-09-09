package lazy.demo.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ImageMngtAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageMngtAuthServiceApplication.class, args);
    }

}
