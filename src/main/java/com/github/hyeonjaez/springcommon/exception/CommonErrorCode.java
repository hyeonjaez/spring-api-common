package com.github.hyeonjaez.springcommon.exception;

import org.springframework.http.HttpStatus;

/**
 * {@code CommonErrorCode} defines a set of global error codes commonly used across the application.
 *
 * <p>This enum implements the {@link ErrorCode} interface and provides
 * the HTTP status code, business-specific error code string, and user-facing error message.</p>
 *
 * <p>Each constant represents a frequently occurring error scenario and is used in conjunction
 * with {@link com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler}.</p>
 *
 * <p><b>Error Code Convention:</b> Follows the format "DOMAIN-CODE" (e.g., COMMON-001)</p>
 *
 * <ul>
 *     <li>{@code INVALID_INPUT_VALUE} - Indicates validation failure</li>
 *     <li>{@code INTERNAL_SERVER_ERROR} - Indicates unexpected server error</li>
 *     <li>{@code NO_ENDPOINT} - API endpoint not found</li>
 *     <li>{@code METHOD_NOT_ALLOWED} - HTTP method not allowed</li>
 *     <li>{@code PARAMETER_NULL} - Required parameter is null</li>
 *     <li>{@code PARAMETER_ID_VALUE} - ID parameter is zero or negative</li>
 * </ul>
 *
 * @author fiat_lux
 * @since 0.0.1
 */
public enum CommonErrorCode implements ErrorCode {
    /**
     * Thrown when input validation fails.
     * Example: failure of @Valid or @Validated annotations
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "Validation failed for the input."),

    /**
     * Thrown when a required parameter is {@code null}.
     */
    PARAMETER_NULL(HttpStatus.BAD_REQUEST, "COMMON-002", "A required parameter is null."),

    /**
     * Thrown when the ID parameter is zero or less.
     */
    PARAMETER_ID_VALUE(HttpStatus.BAD_REQUEST, "COMMON-003", "ID value must be greater than zero."),

    /**
     * Thrown when the requested endpoint does not exist.
     */
    NO_ENDPOINT(HttpStatus.NOT_FOUND, "COMMON-004", "The requested endpoint does not exist."),

    /**
     * Thrown when the HTTP method is not supported for the request.
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-005", "The HTTP method is not allowed."),

    /**
     * Indicates an unexpected internal server error.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-006", "An internal server error has occurred.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
