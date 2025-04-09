package com.github.hyeonjaez.springcommon.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * {@code ErrorCode} defines a common contract for custom error codes used in exception handling.
 *
 * <p>Implementations must provide an HTTP status, an error code string, and a user-friendly message.
 * This interface is typically used with {@link com.github.hyeonjaez.springcommon.handler.ErrorResponse}
 * and {@link BusinessException} to produce consistent API error responses.
 * </p>
 *
 * <p>You can define various {@code ErrorCode} implementations by domain or library.</p>
 *
 * <pre>{@code
 * public enum UserErrorCode implements ErrorCode {
 *     USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "User not found.");
 *     ...
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @since 0.0.1
 */
public interface ErrorCode extends Serializable {

    /**
     * Returns the corresponding HTTP status code for the error.
     *
     * @return the {@link HttpStatus} associated with this error
     */
    HttpStatus getHttpStatus();

    /**
     * Returns the unique code representing this error.
     *
     * <p>Example: "USER-001", "COMMON-002"</p>
     *
     * @return the error code string
     */
    String getCode();

    /**
     * Returns a human-readable error message for the client.
     *
     * @return the error message
     */
    String getMessage();
}