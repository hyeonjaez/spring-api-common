package com.github.hyeonjaez.springcommon.exception;

import org.springframework.http.HttpStatus;

/**
 * {@code ErrorCode}는 커스텀 예외 처리 시 사용할 공통 에러 코드 규약을 정의하는 인터페이스입니다.
 *
 * <p>구현체는 HTTP 상태 코드, 에러 코드 문자열, 사용자에게 전달할 메시지를 포함해야 하며,
 * {@link com.github.hyeonjaez.springcommon.handler.ErrorResponse} 또는
 * {@link BusinessException}과 함께 사용되어
 * 일관된 예외 응답 구조를 구성합니다.</p>
 *
 * <p>라이브러리 또는 도메인 별로 다양한 {@code ErrorCode} 구현체를 정의하여 사용할 수 있습니다.</p>
 *
 * <pre>{@code
 * public enum UserErrorCode implements ErrorCode {
 *     USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다.");
 *     ...
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @since 1.0.0
 */
public interface ErrorCode {

    /**
     * 에러에 해당하는 HTTP 상태 코드를 반환합니다.
     *
     * @return {@link HttpStatus} HTTP 상태 코드
     */
    HttpStatus getHttpStatus();

    /**
     * 에러에 해당하는 코드 문자열을 반환합니다.
     *
     * <p>예: "USER-001", "COMMON-002" 등</p>
     *
     * @return 에러 코드 문자열
     */
    String getCode();

    /**
     * 사용자에게 전달할 에러 메시지를 반환합니다.
     *
     * @return 에러 메시지
     */
    String getMessage();
}
