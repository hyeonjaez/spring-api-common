package com.github.hyeonjaez.springcommon.handler;

import com.github.hyeonjaez.springcommon.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 공통 예외 처리의 기본 구현을 제공하는 추상 클래스입니다.
 * <p>
 * 사용자는 이 클래스를 상속받아 필요한 예외 핸들러만 추가하면 됩니다.
 * </p>
 *
 * <pre>{@code
 * @RestControllerAdvice
 * public class MyExceptionHandler extends GlobalExceptionHandler {
 *     // 필요한 예외만 추가로 핸들링
 * }
 * }</pre>
 *
 * @author fiat_lux
 * @see com.github.hyeonjaez.springcommon.handler.ErrorResponse
 * @see com.github.hyeonjaez.springcommon.exception.ErrorCode
 * @since 0.0.1
 */
public abstract class GlobalExceptionHandler {

    /**
     * 도메인 비즈니스 예외를 처리합니다.
     *
     * @param exception {@link BusinessException}
     * @return 에러 응답 ResponseEntity
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
        return ErrorResponse.toErrorResponseEntity(exception.getErrorCode());
    }
}