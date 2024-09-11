package lazy.demo.auth_service.dto.resp;

import lazy.demo.auth_service.dto.resp.api_error.ApiError;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GenericResponse<T> {
    private String status;
    private T data;

    public GenericResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> GenericResponse<T> success(T data) {
        return new GenericResponse<>("success", data);
    }

    public static GenericResponse<ApiError> fail(ApiError apiError) {
        return new GenericResponse<>("fail", apiError);
    }
}