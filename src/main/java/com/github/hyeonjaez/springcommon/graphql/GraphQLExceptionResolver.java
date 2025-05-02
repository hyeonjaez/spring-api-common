package com.github.hyeonjaez.springcommon.graphql;

import org.springframework.stereotype.Component;

/**
 * GraphQLExceptionResolver acts as a Spring Component responsible for handling exceptions
 * during the GraphQL data fetching process. It extends AbstractGraphQLExceptionResolver
 * to utilize the default exception handling behavior defined in its parent class.
 *
 * This implementation does not override or extend the behavior of AbstractGraphQLExceptionResolver,
 * but rather serves as a concrete implementation for Spring to inject where needed.
 *
 * It allows seamless integration of exception handling into a Spring-based application without additional configuration.
 */
// GraphQLExceptionResolver.java (구현체)
@Component  // Spring Bean으로 등록
public class GraphQLExceptionResolver extends AbstractGraphQLExceptionResolver {
    /**
     * GraphQLExceptionResolver is the default bean provided to ensure functionality "out-of-the-box"
     * when this library is added.
     *
     * It is not strictly required; if a user registers their own custom {@code CustomGraphQLExceptionResolver}
     * bean, that custom implementation will be used instead without any issues.
     *
     * However, if *no* such bean is registered by the user at all, the exception handling logic
     * would not be active. Therefore, this default implementation is included to guarantee
     * that the exception handling feature is available by default, requiring no additional setup.
     */
    }
