package com.github.hyeonjaez.springcommon.exception;

/**
 * {@code BusinessException}은 애플리케이션 내에서 발생하는 비즈니스 예외를 나타냅니다.
 *
 * <p>이 클래스는 {@link ErrorCode} 인터페이스를 통해 에러 상태 코드, 에러 코드 문자열, 에러 메시지를 캡슐화하여
 * 예외 처리 시 일관된 응답을 생성할 수 있도록 도와줍니다.</p>
 *
 * <p>주로 서비스 또는 도메인 계층에서 유효성 검사, 상태 불일치 등 비즈니스 로직에 따라 예외를 발생시킬 때 사용합니다.</p>
 *
 * <pre>{@code
 * // 예시: 사용자 ID가 존재하지 않는 경우
 * if (!userExists) {
 *     throw new CustomException(UserErrorCode.USER_NOT_FOUND);
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @since 1.0.0
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
