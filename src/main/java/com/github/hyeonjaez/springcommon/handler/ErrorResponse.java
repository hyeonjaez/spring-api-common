package com.github.hyeonjaez.springcommon.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.hyeonjaez.springcommon.exception.BusinessException;
import com.github.hyeonjaez.springcommon.exception.ErrorCode;
import com.github.hyeonjaez.springcommon.response.ApiStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 에러 응답 본문 구조를 정의한 클래스입니다.
 *
 * <p>
 * {@link ErrorCode} 인터페이스를 구현한 클래스에서 전달된 정보를 바탕으로,
 * 에러 상태, HTTP 상태 코드, 에러 코드, 메시지를 포함하는 응답 객체를 생성합니다.
 * </p>
 *
 * <p>
 * {@code @RestControllerAdvice}에서 예외 처리 시 {@link ResponseEntity}로 반환할 수 있도록
 * 다양한 정적 팩토리 메서드를 제공합니다.
 * </p>
 *
 * @author fiat_lux
 * @see BusinessException
 * @see com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * API 응답 상태를 나타냅니다. (예: FAILURE, ERROR)
     */
    private final ApiStatus status;

    /**
     * HTTP 상태 코드 (예: 400, 500 등)
     */
    private final int statusCode;

    /**
     * 비즈니스 로직에 따른 에러 코드 (예: COMMON-001)
     */
    private final String errorCode;

    /**
     * 사용자에게 전달할 에러 메시지
     */
    private final String message;

    /**
     * ErrorResponse 객체를 직접 생성하지 않고, of() 또는 toResponseEntity()를 사용할 것을 권장합니다.
     */
    private ErrorResponse(ApiStatus status, HttpStatus httpStatus, String errorCode, String message) {
        this.status = status;
        this.statusCode = httpStatus.value();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * 커스텀 메시지를 포함한 에러 응답 객체를 생성합니다.
     *
     * @param errorCode     에러 코드 객체
     * @param customMessage 사용자 정의 에러 메시지
     * @return ErrorResponse 객체
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
     * 기본 메시지를 포함한 에러 응답 객체를 생성합니다.
     *
     * @param errorCode 에러 코드 객체
     * @return ErrorResponse 객체
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getMessage());
    }

    /**
     * 기본 메시지를 포함한 {@link ResponseEntity} 객체를 생성합니다.
     *
     * @param errorCode 에러 코드 객체
     * @return 에러 응답 본문을 포함한 ResponseEntity 객체
     */
    public static ResponseEntity<ErrorResponse> toErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(of(errorCode));
    }

    /**
     * 커스텀 메시지를 포함한 {@link ResponseEntity} 객체를 생성합니다.
     *
     * @param errorCode     에러 코드 객체
     * @param customMessage 사용자 정의 에러 메시지
     * @return 에러 응답 본문을 포함한 ResponseEntity 객체
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
     * HTTP 상태 코드에 따라 {@link ApiStatus}를 결정합니다.
     *
     * @param httpStatus HTTP 상태 코드
     * @return {@code 5xx}이면 ERROR, 그 외는 FAILURE
     */
    private static ApiStatus resolveStatus(HttpStatus httpStatus) {
        return httpStatus.is5xxServerError() ? ApiStatus.ERROR : ApiStatus.FAILURE;
    }

}