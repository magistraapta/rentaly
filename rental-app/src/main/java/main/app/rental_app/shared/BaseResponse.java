package main.app.rental_app.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private String timestamp;

    public static <T> BaseResponse<T> of(HttpStatusCode statusCode, String message, T data) {
        return BaseResponse.<T>builder()
            .statusCode(statusCode.value())
            .message(message)
            .data(data)
            .timestamp(Instant.now().toString())
            .build();
    }

    public static <T> BaseResponse<T> of(HttpStatusCode statusCode, String message) {
        return BaseResponse.<T>builder()
            .statusCode(statusCode.value())
            .message(message)
            .timestamp(Instant.now().toString())
            .build();
    }

    public static <T> BaseResponse<T> success(HttpStatusCode status, String message, T data) {
        return BaseResponse.of(status, message, data);
    }

    public static <T> BaseResponse<T> success(HttpStatusCode status, String message) {
        return BaseResponse.of(status, message);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return BaseResponse.of(HttpStatus.OK, message, data);
    }

    public static <T> BaseResponse<T> success(HttpStatusCode status) {
        return BaseResponse.of(status, "Success");
    }

    public static <T> BaseResponse<T> error(HttpStatusCode status, String message) {
        return BaseResponse.of(status, message);
    }

}
