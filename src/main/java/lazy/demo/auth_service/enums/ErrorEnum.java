package lazy.demo.auth_service.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    // Mã lỗi cho module người dùng
    USER_NOT_FOUND("USR_001", "User not found"), // Người dùng không tồn tại
    USER_ALREADY_EXISTS("USR_002", "User already exists"), // Người dùng đã tồn tại

    // Mã lỗi cho module xác thực
    INVALID_CREDENTIALS("AUTH_001", "Invalid credentials"), // Thông tin xác thực không hợp lệ
    INVALID_TOKEN("AUTH_002", "Invalid token"), // Token không hợp lệ

    // Mã lỗi cho module yêu cầu
    INVALID_REQUEST("REQ_001", "Invalid request"), // Yêu cầu không hợp lệ

    // Mã lỗi cho hệ thống
    INTERNAL_SERVER_ERROR("SYS_001", "Internal server error"); // Lỗi hệ thống

    private final String errCode;
    private final String errMessage;

    ErrorEnum(String code, String errMessage) {
        this.errCode = code;
        this.errMessage = errMessage;
    }

}
