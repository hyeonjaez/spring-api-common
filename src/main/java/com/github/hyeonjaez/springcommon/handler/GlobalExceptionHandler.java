package com.github.hyeonjaez.springcommon.handler;

import com.github.hyeonjaez.springcommon.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Provides a base implementation for handling domain-specific business exceptions.
 *
 * <p>
 * This abstract class defines a default handler for {@link BusinessException}.
 * Users can extend this class to add additional exception handlers
 * specific to their application needs.
 * </p>
 *
 * <p>
 * When used with {@code @RestControllerAdvice}, it enables global exception handling
 * across the application.
 * </p>
 *
 * <pre>{@code
 * @RestControllerAdvice
 * public class MyExceptionHandler extends GlobalExceptionHandler {
 *     // Add custom exception handlers if necessary
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @see ErrorResponse
 * @see BusinessException
 * @since 0.0.1
 */
public abstract class GlobalExceptionHandler {

    /**
     * Handles domain-specific business exceptions.
     *
     * @param exception the {@link BusinessException} instance
     * @return a {@link ResponseEntity} containing the standardized error response
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
        return ErrorResponse.toErrorResponseEntity(exception.getErrorCode());
    }
}