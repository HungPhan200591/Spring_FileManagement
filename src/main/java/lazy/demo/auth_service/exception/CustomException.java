package lazy.demo.auth_service.exception;

import lazy.demo.auth_service.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorEnum errorEnum;

    public CustomException(ErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }

}
