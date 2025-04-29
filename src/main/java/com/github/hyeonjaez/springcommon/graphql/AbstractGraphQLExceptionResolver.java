package com.github.hyeonjaez.springcommon.graphql;

import com.github.hyeonjaez.springcommon.exception.BusinessException;
import com.github.hyeonjaez.springcommon.response.ApiStatus;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * An abstract base class for resolving exceptions that occur during GraphQL data fetching.
 *
 * <p>This class implements the {@link DataFetcherExceptionResolver} interface and provides
 * a mechanism to handle various types of exceptions commonly encountered in GraphQL data fetching
 * by mapping them to corresponding {@link GraphQLError} objects.
 *
 * <p>Subclasses can extend this class to inherit the default exception handling logic or
 * override specific methods to customize behavior for handling exceptions.
 *
 * <p>The {@code resolveException} method processes the exception, identifies its type, and
 * delegates the handling to the respective handler method. This ensures consistent error
 * responses for different types of application and server-level exceptions.
 *
 * @author hansnam1105
 * @see com.github.hyeonjaez.springcommon.handler.GlobalExceptionHandler
 * @see com.github.hyeonjaez.springcommon.handler.ErrorResponse
 * @see com.github.hyeonjaez.springcommon.exception.BusinessException
 * @since 0.0.1
 */
// AbstractGraphQLExceptionResolver.java
public abstract class AbstractGraphQLExceptionResolver
        implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable ex, DataFetchingEnvironment env) {
        // 1. 비즈니스 예외
        if (ex instanceof BusinessException) {
            return Mono.just(List.of(handleBusinessException((BusinessException) ex, env)));
        }
        // 2. 리소스 미발견
        if (ex instanceof NoResourceFoundException noResourceFoundException) {
            return Mono.just(List.of(handleNoResourceFound(noResourceFoundException, env)));
        }
        // 3. HTTP 메서드 지원 안 함
        if (ex instanceof HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
            return Mono.just(List.of(handleMethodNotSupported(httpRequestMethodNotSupportedException, env)));
        }
        // 4. 검증 실패
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return Mono.just(List.of(handleValidationException(methodArgumentNotValidException, env)));
        }
        // 5. 메시지 컨버팅 실패 (JSON 파싱 오류 등)
        if (ex instanceof HttpMessageNotReadableException httpMessageNotReadableException) {
            return Mono.just(List.of(handleMessageNotReadable(httpMessageNotReadableException, env)));
        }
        // 6. 그 외 예외 (Internal Server Error)
        return Mono.just(List.of(handleGenericException(ex, env)));
    }

    // --- Handlers for each exception type ---
    private static final String STATUS = "status";
    private static final String STATUS_CODE = "statusCode";
    private static final String ERROR_CODE = "errorCode";

    protected GraphQLError handleBusinessException(BusinessException ex, DataFetchingEnvironment env) {
        var code = ex.getErrorCode();
        return GraphqlErrorBuilder.newError(env)
                .message(code.getMessage())
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, code.getHttpStatus().value(),
                        ERROR_CODE, code.getCode()
                ))
                .build();
    }

    protected GraphQLError handleNoResourceFound(NoResourceFoundException ex, DataFetchingEnvironment env) {
        // 예: 404 Not Found
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 404,
                        ERROR_CODE, "RESOURCE_NOT_FOUND"
                ))
                .build();
    }

    protected GraphQLError handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("지원하지 않는 HTTP 메서드입니다: " + ex.getMethod())
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 405,
                        ERROR_CODE, "METHOD_NOT_ALLOWED"
                ))
                .build();
    }

    protected GraphQLError handleValidationException(MethodArgumentNotValidException ex, DataFetchingEnvironment env) {
        // 첫 번째 필드 오류 메시지만 예시로 사용
        var fieldError = ex.getBindingResult().getFieldErrors().get(0);
        return GraphqlErrorBuilder.newError(env)
                .message(fieldError.getDefaultMessage())
                .errorType(ErrorType.ValidationError)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 400,
                        ERROR_CODE, "INVALID_ARGUMENT",
                        "field", fieldError.getField()
                ))
                .build();
    }

    @SuppressWarnings("unused")
    protected GraphQLError handleMessageNotReadable(HttpMessageNotReadableException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("잘못된 요청 메시지 형식입니다.")
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 400,
                        ERROR_CODE, "MESSAGE_NOT_READABLE"
                ))
                .build();
    }

    @SuppressWarnings("unused")
    protected GraphQLError handleGenericException(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("Internal server error")
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 500,
                        ERROR_CODE, "INTERNAL_ERROR"
                ))
                .build();
    }
}
