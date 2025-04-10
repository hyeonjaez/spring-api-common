package com.github.hyeonjaez.springcommon.handler;

import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Abstract base class that provides default global exception handling.
 *
 * <p>
 * Use this class in combination with {@code @RestControllerAdvice} to handle
 * common exceptions consistently across your Spring application.
 * </p>
 *
 * <p>
 * The following exception types are handled by default:
 * <ul>
 *     <li>{@link NoResourceFoundException} - when the requested endpoint does not exist</li>
 *     <li>{@link HttpRequestMethodNotSupportedException} - when the HTTP method is not allowed</li>
 *     <li>{@link MethodArgumentNotValidException}, {@link ConstraintViolationException}, {@link HttpMessageNotReadableException} - for validation failures</li>
 *     <li>{@link Exception} - for unexpected internal server errors</li>
 * </ul>
 * </p>
 *
 * <p>
 * Extend this class and annotate your subclass with {@code @RestControllerAdvice}
 * to enable the default global exception handling. You may override or add new
 * exception handler methods as needed.
 * </p>
 *
 * <pre>{@code
 * @RestControllerAdvice
 * public class MyExceptionHandler extends AbstractGlobalExceptionHandler {
 *     // Override specific handlers or add new ones if needed
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @see com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler
 * @see com.github.hyeonjaez.springcommon.handler.ErrorResponse
 * @see com.github.hyeonjaez.springcommon.exception.BusinessException
 * @since 0.0.1
 */
public abstract class AbstractGlobalExceptionHandler extends GlobalExceptionHandler {
    /**
     * Handles requests for non-existent endpoints (404 Not Found).
     *
     * @param exception the exception thrown when no matching resource is found
     * @return a standardized error response with HTTP 404 status
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception) {
        return ErrorResponse.toErrorResponseEntity(
                CommonErrorCode.NO_ENDPOINT,
                String.format("[%s] 에 해당하는 엔드포인트를 찾을 수 없습니다.", exception.getResourcePath())
        );
    }

    /**
     * Handles HTTP requests using unsupported methods (405 Method Not Allowed).
     *
     * @param exception the exception thrown when a method is not supported
     * @return a standardized error response with HTTP 405 status
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return ErrorResponse.toErrorResponseEntity(
                CommonErrorCode.METHOD_NOT_ALLOWED,
                String.format("[%s] 는 허용되지 않는 메소드입니다.", exception.getMethod())
        );
    }

    /**
     * Handles unexpected exceptions that are not explicitly caught elsewhere (500 Internal Server Error).
     *
     * @return a standardized error response with HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        return ErrorResponse.toErrorResponseEntity(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles validation-related exceptions (400 Bad Request).
     *
     * <ul>
     *     <li>{@link MethodArgumentNotValidException} - thrown when {@code @Valid} fails</li>
     *     <li>{@link ConstraintViolationException} - thrown when {@code @Validated} fails</li>
     *     <li>{@link HttpMessageNotReadableException} - thrown when JSON parsing fails</li>
     * </ul>
     *
     * @param exception the validation exception
     * @return a standardized error response with HTTP 400 status
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
        String message = resolveValidationMessage(exception);
        return ErrorResponse.toErrorResponseEntity(CommonErrorCode.INVALID_INPUT_VALUE, message);
    }

    /**
     * Resolves a human-readable error message based on the exception type.
     *
     * @param exception the validation exception
     * @return a formatted error message to include in the response
     */
    protected String resolveValidationMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            BindingResult result = methodArgumentNotValidException.getBindingResult();
            return result.getFieldErrors()
                    .stream()
                    .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
                    .reduce((m1, m2) -> m1 + ". " + m2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        }

        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(v -> String.format("[%s] %s", v.getPropertyPath(), v.getMessage()))
                    .reduce((m1, m2) -> m1 + ". " + m2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        }

        if (exception instanceof HttpMessageNotReadableException) {
            return "요청 body를 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        }

        return "알 수 없는 검증 오류가 발생했습니다.";
    }
}

