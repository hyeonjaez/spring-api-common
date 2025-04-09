package com.github.hyeonjaez.springcommon.response;

import com.github.hyeonjaez.springcommon.util.ObjectsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating {@link ApiResponse} objects in a standardized and simplified way.
 *
 * <p>
 * This class helps construct consistent API responses by wrapping them in a {@link ResponseEntity}
 * along with appropriate HTTP status codes and user-friendly messages.
 * </p>
 *
 * <p>
 * It provides static helper methods for common response patterns such as:
 * <ul>
 *     <li>{@code 200 OK}</li>
 *     <li>{@code 201 Created}</li>
 *     <li>{@code 204 No Content}</li>
 * </ul>
 * </p>
 *
 * <pre>{@code
 * return ApiResponseUtil.ok("User retrieved successfully", userDto);
 * return ApiResponseUtil.created(newUserDto);
 * return ApiResponseUtil.noContent();
 * }</pre>
 *
 * @author fiat_lux
 * @see ApiResponse
 * @see ApiStatus
 * @since 0.0.1
 */
public class ApiResponseUtil {

    /**
     * Default fallback message.
     */
    public static final String DEFAULT_MESSAGE = "요청이 처리되었습니다.";

    /**
     * Default success message for 200 OK.
     */
    public static final String DEFAULT_OK_MESSAGE = "요청이 성공적으로 처리되었습니다.";

    /**
     * Default message for 201 Created.
     */
    public static final String DEFAULT_CREATED_MESSAGE = "리소스가 성공적으로 생성되었습니다.";

    /**
     * Default message for 204 No Content.
     */
    public static final String DEFAULT_NO_CONTENT_MESSAGE = "처리가 완료되었지만 반환할 데이터가 없습니다.";

    /**
     * Returns a {@code 200 OK} response with a custom message and response data.
     *
     * @param message the message to include in the response
     * @param data    the payload to return
     * @param <T>     the type of response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return build(ApiStatus.SUCCESS, HttpStatus.OK, message, data);
    }

    /**
     * Returns a {@code 200 OK} response with a default success message.
     *
     * @param data the payload to return
     * @param <T>  the type of response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ok(DEFAULT_OK_MESSAGE, data);
    }

    /**
     * Returns a {@code 201 Created} response with a custom message and response data.
     *
     * @param message the message to include
     * @param data    the payload to return
     * @param <T>     the type of response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return build(ApiStatus.SUCCESS, HttpStatus.CREATED, message, data);
    }

    /**
     * Returns a {@code 201 Created} response with a default message.
     *
     * @param data the payload to return
     * @param <T>  the type of response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(DEFAULT_CREATED_MESSAGE, data);
    }

    /**
     * Returns a {@code 204 No Content} response with a custom message.
     *
     * @param message the message to include
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static ResponseEntity<ApiResponse<EmptyResponse>> noContent(String message) {
        return build(ApiStatus.SUCCESS, HttpStatus.NO_CONTENT, message, EmptyResponse.getInstance());
    }

    /**
     * Returns a {@code 204 No Content} response with a default message.
     *
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static ResponseEntity<ApiResponse<EmptyResponse>> noContent() {
        return noContent(DEFAULT_NO_CONTENT_MESSAGE);
    }

    /**
     * Builds a response with the provided status, HTTP code, message, and data.
     *
     * <p>Defaults are applied for any {@code null} input:
     * <ul>
     *     <li>{@code status} defaults to {@link ApiStatus#SUCCESS}</li>
     *     <li>{@code httpStatus} defaults to {@link HttpStatus#OK}</li>
     *     <li>{@code message} defaults to a standard message based on the HTTP status</li>
     *     <li>{@code data} defaults to an {@link EmptyResponse} instance</li>
     * </ul>
     * </p>
     *
     * @param status     API status
     * @param httpStatus HTTP status code
     * @param message    message to include
     * @param data       response payload
     * @param <T>        type of the response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> build(ApiStatus status, HttpStatus httpStatus, String message, T data) {
        ApiStatus notNullStatus = ObjectsUtil.isNotNull(status) ? status : ApiStatus.SUCCESS;
        HttpStatus notNullHttpStatus = ObjectsUtil.isNotNull(httpStatus) ? httpStatus : HttpStatus.OK;
        String notNullMessage = ObjectsUtil.isNotNull(message) ? message : resolveDefaultMessage(notNullHttpStatus);
        T notNullData = ObjectsUtil.isNotNull(data) ? data : (T) EmptyResponse.getInstance();

        return ResponseEntity
                .status(notNullHttpStatus)
                .body(new ApiResponse<>(notNullStatus, notNullMessage, notNullData));
    }

    /**
     * Builds a response assuming {@link ApiStatus#SUCCESS} as the default status.
     *
     * @param httpStatus HTTP status
     * @param message    message to include
     * @param data       response payload
     * @param <T>        type of the response body
     * @return ResponseEntity containing {@link ApiResponse}
     */
    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus httpStatus, String message, T data) {
        return build(ApiStatus.SUCCESS, httpStatus, message, data);
    }

    /**
     * Returns a default message depending on the given HTTP status.
     *
     * @param httpStatus HTTP status
     * @return the default message string
     */
    private static String resolveDefaultMessage(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case OK -> DEFAULT_OK_MESSAGE;
            case CREATED -> DEFAULT_CREATED_MESSAGE;
            case NO_CONTENT -> DEFAULT_NO_CONTENT_MESSAGE;
            default -> DEFAULT_MESSAGE;
        };
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ApiResponseUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }
}