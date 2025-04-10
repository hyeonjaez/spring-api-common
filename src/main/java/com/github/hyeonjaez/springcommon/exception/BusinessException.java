package com.github.hyeonjaez.springcommon.exception;

/**
 * {@code BusinessException} represents a business-level exception that occurs within the application.
 *
 * <p>This class encapsulates the error status, error code string, and error message through
 * the {@link ErrorCode} interface to support consistent error responses during exception handling.</p>
 *
 * <p>Typically used in the service or domain layer to indicate validation failures,
 * invalid states, or other business rule violations.</p>
 *
 * <pre>{@code
 * // Example: Throwing an exception when a user ID does not exist
 * if (!userExists) {
 *     throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @since 0.0.1
 */
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
