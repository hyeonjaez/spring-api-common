package com.github.hyeonjaez.springcommon.handler;

import com.github.hyeonjaez.springcommon.exception.BusinessException;
import com.github.hyeonjaez.springcommon.exception.ErrorCode;
import com.github.hyeonjaez.springcommon.response.ApiStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Represents the standardized structure for error responses.
 *
 * <p>
 * This class is used to build error responses containing API status,
 * HTTP status code, error code, and a message. It is typically constructed
 * using implementations of the {@link ErrorCode} interface.
 * </p>
 *
 * <p>
 * The static factory methods {@code of()} and {@code toErrorResponseEntity()}
 * allow easy construction and wrapping of the error response in a {@link ResponseEntity}.
 * </p>
 *
 * <p>
 * This class is commonly used in {@code @RestControllerAdvice}-annotated exception handlers
 * to produce consistent API error responses.
 * </p>
 *
 * <p>
 * This class does not depend on any JSON library.
 * If you want to exclude {@code null} fields in the response,
 * configure your serializer (e.g., Jackson) to do so.
 * </p>
 *
 * @author fiat_lux
 * @see BusinessException
 * @see com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler
 * @since 0.0.1
 */
public class ErrorResponse {

    /**
     * The status of the API response (e.g., FAILURE or ERROR).
     */
    private final ApiStatus status;

    /**
     * The HTTP status code (e.g., 400, 500).
     */
    private final int statusCode;

    /**
     * A domain-specific error code (e.g., COMMON-001).
     */
    private final String errorCode;

    /**
     * A human-readable error message for the client.
     */
    private final String message;

    /**
     * Private constructor to enforce the use of factory methods.
     */
    private ErrorResponse(ApiStatus status, HttpStatus httpStatus, String errorCode, String message) {
        this.status = status;
        this.statusCode = httpStatus.value();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Creates an {@link ErrorResponse} with a custom message.
     *
     * @param errorCode     the {@link ErrorCode} instance
     * @param customMessage a custom error message
     * @return a new {@link ErrorResponse}
     */
    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return new ErrorResponse(
                resolveStatus(errorCode.getHttpStatus()),
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                customMessage
        );
    }

    /**
     * Creates an {@link ErrorResponse} using the default message from the error code.
     *
     * @param errorCode the {@link ErrorCode} instance
     * @return a new {@link ErrorResponse}
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getMessage());
    }

    /**
     * Wraps an {@link ErrorResponse} in a {@link ResponseEntity} using the default message.
     *
     * @param errorCode the {@link ErrorCode} instance
     * @return a {@link ResponseEntity} containing the error response
     */
    public static ResponseEntity<ErrorResponse> toErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(of(errorCode));
    }

    /**
     * Wraps an {@link ErrorResponse} in a {@link ResponseEntity} with a custom message.
     *
     * @param errorCode     the {@link ErrorCode} instance
     * @param customMessage a custom error message
     * @return a {@link ResponseEntity} containing the error response
     */
    public static ResponseEntity<ErrorResponse> toErrorResponseEntity(ErrorCode errorCode, String customMessage) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(of(errorCode, customMessage));
    }

    public ApiStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Resolves the {@link ApiStatus} based on the given HTTP status.
     *
     * @param httpStatus the HTTP status code
     * @return {@code ERROR} if status is 5xx, otherwise {@code FAILURE}
     */
    private static ApiStatus resolveStatus(HttpStatus httpStatus) {
        return httpStatus.is5xxServerError() ? ApiStatus.ERROR : ApiStatus.FAILURE;
    }
}