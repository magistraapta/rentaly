package main.app.rental_app.exc;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import main.app.rental_app.shared.BaseResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Handle exception of type of type
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    /**
     * Handle resource not found exception
     * @param ex ResourceNotFoundException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status NOT_FOUND
     */
    public ResponseEntity<BaseResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Not found at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        BaseResponse<Void> errorResponse = BaseResponse.error(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    /**
     * Handle bad request exception
     * @param ex BadRequestException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status BAD_REQUEST
     */
    public ResponseEntity<BaseResponse<Void>> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.error("Bad request at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        BaseResponse<Void> errorResponse = BaseResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    /**
     * Handle unauthorized exception
     * @param ex UnauthorizedException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status UNAUTHORIZED
     */
    public ResponseEntity<BaseResponse<Void>> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.error("Unauthorized at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        BaseResponse<Void> errorResponse = BaseResponse.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    /**
     * Handle forbidden exception
     * @param ex ForbiddenException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status FORBIDDEN
     */
    public ResponseEntity<BaseResponse<Void>> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        log.error("Forbidden at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        BaseResponse<Void> errorResponse = BaseResponse.error(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    /**
     * Handle generic exceptions
     * @param ex Exception: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status INTERNAL_SERVER_ERROR
     */
    public ResponseEntity<BaseResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal server error at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        BaseResponse<Void> errorResponse = BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
