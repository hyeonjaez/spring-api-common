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
    // No specific implementation - using default behavior
}
