package com.github.hyeonjaez.springcommon.handler;

import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import com.github.hyeonjaez.springcommon.exception.CustomException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 클래스입니다.
 *
 * <p>
 * {@link RestControllerAdvice} 어노테이션을 사용하여 컨트롤러 레벨에서 발생한 예외들을 공통된 방식으로 처리합니다.
 * 도메인 예외({@link CustomException}) 및 스프링 내장 예외, 검증 예외, 알 수 없는 예외 등을 핸들링하여
 * 일관된 에러 응답 형식을 제공합니다.
 * </p>
 *
 * @author fiat_lux
 * @see com.github.hyeonjaez.springcommon.handler.ErrorResponse
 * @see com.github.hyeonjaez.springcommon.exception.ErrorCode
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 도메인 비즈니스 예외를 처리합니다.
     *
     * @param exception {@link CustomException}
     * @return 에러 응답 ResponseEntity
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final CustomException exception) {
        return ErrorResponse.toResponseEntity(exception.getErrorCode());
    }

    /**
     * 잘못된 URL 요청으로 인해 리소스를 찾을 수 없는 경우를 처리합니다.
     *
     * @param exception {@link NoResourceFoundException}
     * @return 에러 응답 ResponseEntity
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception) {
        return ErrorResponse.toResponseEntity(
                CommonErrorCode.NO_ENDPOINT,
                String.format("[%s] 에 해당하는 엔드포인트를 찾을 수 없습니다.", exception.getResourcePath())
        );
    }

    /**
     * 허용되지 않은 HTTP 메서드 요청을 처리합니다.
     *
     * @param exception {@link HttpRequestMethodNotSupportedException}
     * @return 에러 응답 ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return ErrorResponse.toResponseEntity(
                CommonErrorCode.METHOD_NOT_ALLOWED,
                String.format("[%s] 는 허용되지 않는 메소드입니다.", exception.getMethod())
        );
    }

    /**
     * 예상치 못한 서버 예외를 처리합니다.
     *
     * @return 500 에러 응답 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        return ErrorResponse.toResponseEntity(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 유효성 검사 관련 예외를 처리합니다.
     *
     * <ul>
     *     <li>{@link MethodArgumentNotValidException} - @Valid에서 바인딩 실패</li>
     *     <li>{@link ConstraintViolationException} - @Validated에서 유효성 실패</li>
     *     <li>{@link HttpMessageNotReadableException} - JSON 파싱 실패 등</li>
     * </ul>
     *
     * @param exception 검증 관련 예외
     * @return 400 에러 응답 ResponseEntity
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
        String message = resolveValidationMessage(exception);
        return ErrorResponse.toResponseEntity(CommonErrorCode.INVALID_INPUT_VALUE, message);
    }

    /**
     * 예외 타입에 따라 검증 메시지를 생성합니다.
     *
     * @param exception 예외 객체
     * @return 사용자에게 전달할 메시지
     */
    private String resolveValidationMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            BindingResult result = methodArgumentNotValidException.getBindingResult();
            return result.getFieldErrors()
                    .stream()
                    .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
                    .reduce((m1, m2) -> m1 + ". " + m2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        }

        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(v -> String.format("[%s] %s", v.getPropertyPath(), v.getMessage()))
                    .reduce((m1, m2) -> m1 + ". " + m2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        }

        if (exception instanceof HttpMessageNotReadableException) {
            return "요청 body를 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        }

        return "알 수 없는 검증 오류가 발생했습니다.";
    }
}