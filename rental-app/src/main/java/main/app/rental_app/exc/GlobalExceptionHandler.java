package main.app.rental_app.exc;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Not found at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    /**
     * Handle bad request exception
     * @param ex BadRequestException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status BAD_REQUEST
     */
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.error("Bad request at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    /**
     * Handle unauthorized exception
     * @param ex UnauthorizedException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status UNAUTHORIZED
     */
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.error("Unauthorized at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    /**
     * Handle forbidden exception
     * @param ex ForbiddenException: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status FORBIDDEN
     */
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        log.error("Forbidden at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    /**
     * Handle generic exceptions
     * @param ex Exception: Get the exception
     * @param request HttpServletRequest: Get the request URI
     * @return ResponseEntity<ErrorResponse>: Return the exception message with status INTERNAL_SERVER_ERROR
     */
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal server error at endpoint {} - Message: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
