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
 * @see com.github.hyeonjaez.springcommon.graphql.GraphQLExceptionResolver
 * @since 0.0.2
 */
public abstract class AbstractGraphQLExceptionResolver implements DataFetcherExceptionResolver {

    private static final String STATUS = "status";
    private static final String STATUS_CODE = "statusCode";
    private static final String ERROR_CODE = "errorCode";

    /**
     * Resolves exceptions occurring during the GraphQL data fetching process and maps them to a list of GraphQLErrors.
     *
     * Returns Mono: To comply with Spring for GraphQL's asynchronous and non-blocking interface.
     *
     * Based on the type of the input exception, it delegates to specific methods to generate
     * appropriate GraphQLError instances. Supported exception types include:
     * - BusinessException
     * - NoResourceFoundException
     * - HttpRequestMethodNotSupportedException
     * - MethodArgumentNotValidException
     * - HttpMessageNotReadableException
     * - Any other unhandled exceptions
     *
     * Each exception type is mapped to a corresponding GraphQL error with relevant details like
     * error message, error type, and custom extensions containing error information.
     *
     * @param ex the exception thrown during data fetching
     * @param env the GraphQL data fetching environment associated with the operation
     * @return a Mono containing a list of GraphQLErrors representing the resolved exception
     */

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof BusinessException) {
            return Mono.just(List.of(handleBusinessException((BusinessException) ex, env)));
        }
        if (ex instanceof NoResourceFoundException noResourceFoundException) {
            return Mono.just(List.of(handleNoResourceFound(noResourceFoundException, env)));
        }
        if (ex instanceof HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
            return Mono.just(List.of(handleMethodNotSupported(httpRequestMethodNotSupportedException, env)));
        }
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return Mono.just(List.of(handleValidationException(methodArgumentNotValidException, env)));
        }
        if (ex instanceof HttpMessageNotReadableException httpMessageNotReadableException) {
            return Mono.just(List.of(handleMessageNotReadable(httpMessageNotReadableException, env)));
        }
        return Mono.just(List.of(handleGenericException(ex, env)));
    }

    /**
     * Handles a {@code BusinessException} and transforms it into a {@code GraphQLError}.
     *
     * This method retrieves the error code and associated details from the {@code BusinessException},
     * then constructs a {@code GraphQLError} with an appropriate error message, error type, and
     * extensions containing additional information about the error. It leverages the
     * {@code DataFetchingEnvironment} to tie the error with the ongoing GraphQL operation context.
     *
     * @param ex the {@link BusinessException} to be handled
     * @param env the {@link DataFetchingEnvironment} containing the current GraphQL operation's context
     * @return a {@link GraphQLError} representing the resolved business exception
     */
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

    /**
     * Handles a {@code NoResourceFoundException} by transforming it into a {@code GraphQLError}.
     *
     * This method creates a {@code GraphQLError} with the message from the exception, an error type
     * of {@code DataFetchingException}, and custom extensions containing error details such as status,
     * status code, and error code. It utilizes the {@code DataFetchingEnvironment} to associate the
     * error with the current GraphQL operation.
     *
     * @param ex the {@link NoResourceFoundException} to be handled
     * @param env the {@link DataFetchingEnvironment} containing the context of the current GraphQL operation
     * @return a {@link GraphQLError} representing the resolved exception
     */
    protected GraphQLError handleNoResourceFound(NoResourceFoundException ex, DataFetchingEnvironment env) {
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

    /**
     * Handles an HttpRequestMethodNotSupportedException by transforming it into a GraphQLError.
     *
     * This method constructs a GraphQLError with a specific message indicating the unsupported
     * HTTP method, an error type of DataFetchingException, and additional extensions containing
     * error details such as status, status code, and error code.
     *
     * @param ex the HttpRequestMethodNotSupportedException to be handled, representing
     *           the unsupported HTTP method during the request
     * @param env the DataFetchingEnvironment containing context information about the
     *            current GraphQL request
     * @return a GraphQLError representing the resolved exception with error details
     */
    protected GraphQLError handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("Unsupported HTTP method: " + ex.getMethod())
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 405,
                        ERROR_CODE, "METHOD_NOT_ALLOWED"
                ))
                .build();
    }

    /**
     * Handles a {@code MethodArgumentNotValidException} and transforms it into a {@code GraphQLError}.
     *
     * This method extracts the first field error from the exception's binding result to construct a
     * {@code GraphQLError} with an error type of {@code ValidationError}. The error message and
     * information about the invalid field are included in the extensions for providing detailed feedback.
     *
     * @param ex the {@link MethodArgumentNotValidException} containing validation errors
     * @param env the {@link DataFetchingEnvironment} providing the context of the current GraphQL operation
     * @return a {@link GraphQLError} representing the validation error with relevant details
     */
    protected GraphQLError handleValidationException(MethodArgumentNotValidException ex, DataFetchingEnvironment env) {
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

    /**
     * Handles a {@code HttpMessageNotReadableException} and transforms it into a {@code GraphQLError}.
     *
     * This method constructs a {@code GraphQLError} with a predefined error message indicating
     * an invalid request message format. It sets the error type as {@code DataFetchingException}
     * and includes additional error details such as status, status code, and error code in
     * the extensions map. The {@code DataFetchingEnvironment} is used to associate the error
     * with the current GraphQL operation context.
     *
     * @param ex the {@link HttpMessageNotReadableException} that indicates a failed attempt
     *           to read the HTTP message
     * @param env the {@link DataFetchingEnvironment} associated with the current GraphQL operation
     * @return a {@link GraphQLError} representing the resolved error for an unreadable HTTP message
     */
    @SuppressWarnings("unused")
    protected GraphQLError handleMessageNotReadable(HttpMessageNotReadableException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("Invalid request message format.")
                .errorType(ErrorType.DataFetchingException)
                .extensions(Map.of(
                        STATUS, ApiStatus.FAILURE,
                        STATUS_CODE, 400,
                        ERROR_CODE, "MESSAGE_NOT_READABLE"
                ))
                .build();
    }

    /**
     * Handles a generic exception and transforms it into a {@code GraphQLError}.
     *
     * This method is used as a fallback for handling exceptions that do not match
     * specific exception handlers. It creates a {@code GraphQLError} with a
     * generalized error message, an error type of {@code DataFetchingException},
     * and includes additional extensions containing error details such as status,
     * status code, and error code.
     *
     * @param ex the throwable or exception to be handled
     * @param env the {@link DataFetchingEnvironment} associated with the current GraphQL operation
     * @return a {@link GraphQLError} representing the resolved generic exception
     */
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
