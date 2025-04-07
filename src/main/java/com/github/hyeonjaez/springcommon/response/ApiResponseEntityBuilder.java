package com.github.hyeonjaez.springcommon.response;

import com.github.hyeonjaez.springcommon.util.ObjectsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * {@link ApiResponse} 객체를 손쉽게 생성할 수 있도록 도와주는 유틸리티 클래스입니다.
 *
 * <p>
 * 이 클래스는 HTTP 응답 상태와 함께 API 응답 본문을 구성하는 데 사용됩니다.
 * 각 메서드는 {@link ResponseEntity} 객체로 래핑된 {@link ApiResponse}를 반환합니다.
 * </p>
 *
 * <p>
 * 제공되는 정적 메서드를 통해 공통적인 HTTP 응답 (200 OK, 201 Created, 204 No Content)을
 * 표준화된 형태로 쉽게 반환할 수 있으며, 메시지를 생략하면 기본 메시지가 사용됩니다.
 * </p>
 *
 * <pre>{@code
 * // 사용 예시
 * return ApiResponseEntityBuilder.ok("회원 조회 성공", userDto);
 * return ApiResponseEntityBuilder.created(newUserDto);
 * return ApiResponseEntityBuilder.noContent();
 * }</pre>
 *
 * @author fiat_lux
 * @see ApiResponse
 * @see ApiStatus
 * @since 1.0.0
 */
public final class ApiResponseEntityBuilder {
    public static final String DEFAULT_MESSAGE = "요청이 처리되었습니다.";
    public static final String DEFAULT_OK_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    public static final String DEFAULT_CREATED_MESSAGE = "리소스가 성공적으로 생성되었습니다.";
    public static final String DEFAULT_NO_CONTENT_MESSAGE = "처리가 완료되었지만 반환할 데이터가 없습니다.";

    /**
     * 200 OK 응답을 반환합니다.
     *
     * @param message 사용자에게 전달할 메시지
     * @param data    응답 데이터
     * @param <T>     응답 데이터 타입
     * @return 200 OK 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return build(ApiStatus.SUCCESS, HttpStatus.OK, message, data);
    }

    /**
     * 200 OK 응답을 기본 메시지와 함께 반환합니다.
     *
     * @param data 응답 데이터
     * @param <T>  응답 데이터 타입
     * @return 200 OK 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ok(DEFAULT_OK_MESSAGE, data);
    }

    /**
     * 201 Created 응답을 반환합니다.
     *
     * @param message 사용자에게 전달할 메시지
     * @param data    응답 데이터
     * @param <T>     응답 데이터 타입
     * @return 201 Created 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return build(ApiStatus.SUCCESS, HttpStatus.CREATED, message, data);
    }

    /**
     * 201 Created 응답을 기본 메시지와 함께 반환합니다.
     *
     * @param data 응답 데이터
     * @param <T>  응답 데이터 타입
     * @return 201 Created 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(DEFAULT_CREATED_MESSAGE, data);
    }

    /**
     * 204 No Content 응답을 반환합니다.
     *
     * @param message 사용자에게 전달할 메시지
     * @return 204 No Content 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static ResponseEntity<ApiResponse<EmptyResponse>> noContent(String message) {
        return build(ApiStatus.SUCCESS, HttpStatus.NO_CONTENT, message, EmptyResponse.getInstance());
    }

    /**
     * 204 No Content 응답을 기본 메시지와 함께 반환합니다.
     *
     * @return 204 No Content 상태의 {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static ResponseEntity<ApiResponse<EmptyResponse>> noContent() {
        return noContent(DEFAULT_NO_CONTENT_MESSAGE);
    }

    /**
     * 커스텀 상태, HTTP 상태, 메시지, 데이터로 응답을 생성합니다.
     *
     * <p>
     * 전달된 값이 {@code null}인 경우 기본값이 적용됩니다:
     * <ul>
     *     <li>{@code status}가 null이면 {@link ApiStatus#SUCCESS}</li>
     *     <li>{@code httpStatus}가 null이면 {@link HttpStatus#OK}</li>
     *     <li>{@code message}가 null이면 상태에 따른 기본 메시지</li>
     *     <li>{@code data}가 null이면 {@link EmptyResponse} 인스턴스</li>
     * </ul>
     * </p>
     *
     * @param status     응답 상태
     * @param httpStatus HTTP 상태 코드
     * @param message    사용자 메시지
     * @param data       응답 데이터
     * @param <T>        응답 데이터 타입
     * @return {@link ApiResponse} 래핑 {@link ResponseEntity}
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
     * 응답 상태를 {@link ApiStatus#SUCCESS}로 고정하여 응답을 생성합니다.
     *
     * @param httpStatus HTTP 상태 코드
     * @param message    사용자 메시지
     * @param data       응답 데이터
     * @param <T>        응답 데이터 타입
     * @return {@link ApiResponse} 래핑 {@link ResponseEntity}
     */
    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus httpStatus, String message, T data) {
        return build(ApiStatus.SUCCESS, httpStatus, message, data);
    }

    /**
     * HTTP 상태 코드에 따라 기본 메시지를 반환합니다.
     *
     * @param httpStatus HTTP 상태 코드
     * @return 기본 메시지 문자열
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
     * 유틸리티 클래스이므로 인스턴스화를 방지합니다.
     */
    private ApiResponseEntityBuilder() {
        throw new UnsupportedOperationException("Utility Class");
    }
}