package com.github.hyeonjaez.springcommon.exception;

import org.springframework.http.HttpStatus;

/**
 * 공통적으로 사용되는 전역 에러 코드 Enum 클래스입니다.
 *
 * <p>
 * 이 열거형은 {@link ErrorCode} 인터페이스를 구현하며,
 * HTTP 상태 코드, 비즈니스 에러 코드 문자열, 에러 메시지를 포함합니다.
 * </p>
 *
 * <p>
 * 각 상수는 애플리케이션 전반에서 자주 발생할 수 있는 예외 상황을 표현하며,
 * {@link com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler}와 함께 사용됩니다.
 * </p>
 *
 * <p><b>에러 코드 규칙</b>: "도메인코드-번호" 형식 사용 예: COMMON-001</p>
 *
 * <ul>
 *     <li>{@code INVALID_INPUT_VALUE} - 유효성 검증 실패</li>
 *     <li>{@code INTERNAL_SERVER_ERROR} - 서버 내부 오류</li>
 *     <li>{@code NO_ENDPOINT} - 존재하지 않는 API</li>
 *     <li>{@code METHOD_NOT_ALLOWED} - 허용되지 않는 HTTP 메서드</li>
 *     <li>{@code PARAMETER_NULL} - 필수 파라미터 누락</li>
 *     <li>{@code PARAMETER_ID_VALUE} - ID 파라미터가 0 이하</li>
 * </ul>
 *
 * @author fiat_lux
 * @since 1.0.0
 */
public enum CommonErrorCode implements ErrorCode {
    /**
     * 요청값이 유효하지 않을 때 발생합니다.
     * 예: @Valid 또는 @Validated 어노테이션 실패
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "유효성 검증에 실패했습니다."),

    /**
     * 필수 파라미터가 null 일 경우 발생합니다.
     */
    PARAMETER_NULL(HttpStatus.BAD_REQUEST, "COMMON-002", "필수 파라미터가 null 입니다."),

    /**
     * ID 파라미터가 0 이하일 경우 발생합니다.
     */
    PARAMETER_ID_VALUE(HttpStatus.BAD_REQUEST, "COMMON-003", "ID 값은 0 이상이어야 합니다."),

    /**
     * 요청한 엔드포인트가 존재하지 않을 때 발생합니다.
     */
    NO_ENDPOINT(HttpStatus.NOT_FOUND, "COMMON-004", "존재하지 않는 엔드포인트입니다."),

    /**
     * 허용되지 않은 HTTP 메서드로 요청할 경우 발생합니다.
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-005", "허용되지 않는 메소드입니다."),

    /**
     * 서버 내부 처리 중 예기치 못한 예외가 발생했을 때 사용됩니다.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-006", "서버 내부 오류가 발생했습니다.");

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
