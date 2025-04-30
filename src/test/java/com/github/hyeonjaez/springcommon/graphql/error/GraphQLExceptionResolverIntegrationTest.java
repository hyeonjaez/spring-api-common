package com.github.hyeonjaez.springcommon.graphql.error;

import com.github.hyeonjaez.springcommon.exception.BusinessException;
import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import com.github.hyeonjaez.springcommon.graphql.AbstractGraphQLExceptionResolver;
import com.github.hyeonjaez.springcommon.graphql.GraphQLExceptionResolver;
import com.github.hyeonjaez.springcommon.response.ApiStatus;
import graphql.GraphQLError;
import graphql.Scalars;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.MergedField;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import graphql.schema.GraphQLFieldDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphQLExceptionResolverTest {

    private AbstractGraphQLExceptionResolver resolver;
    private DataFetchingEnvironment env;

    /**
     * Sets up the test environment for the test cases in the GraphQLExceptionResolverTest class.
     *
     * This method initializes the necessary objects and state required for testing the
     * behavior of the GraphQLExceptionResolver. The setup includes the following steps:
     *
     * 1. Creates a GraphQLFieldDefinition named "dummy" with the type Scalars.GraphQLString.
     * 2. Creates an AST Field object representing the "dummy" field, with a source location
     *    specified at line 1 and column 1.
     * 3. Constructs a MergedField object that includes the AST Field instance.
     * 4. Builds an ExecutionStepInfo object representing the execution context for the
     *    "dummy" field. This includes the path ["dummy"], the field definition, and the type.
     * 5. Instantiates a DataFetchingEnvironment with the merged field and execution step
     *    information.
     *
     * The initialized objects include:
     * - `resolver`: An instance of GraphQLExceptionResolver to be tested.
     * - `env`: A configured DataFetchingEnvironment instance that simulates the GraphQL
     *          execution context for the tests.
     *
     * This method is annotated with @BeforeEach, ensuring it is executed before each test
     * method in the GraphQLExceptionResolverTest class.
     */
    @BeforeEach
    void setUp() {
        resolver = new GraphQLExceptionResolver();

        // 1) GraphQLFieldDefinition
        GraphQLFieldDefinition fd = GraphQLFieldDefinition.newFieldDefinition()
                .name("dummy")
                .type(Scalars.GraphQLString)
                .build();

        // 2) AST Field with SourceLocation
        Field astField = Field.newField("dummy")
                .sourceLocation(new SourceLocation(1, 1))
                .build();

        // 3) MergedField
        MergedField mergedField = MergedField.newMergedField()
                .addField(astField)
                .build();

        // 4) ExecutionStepInfo with path ["dummy"]
        ResultPath path = ResultPath.rootPath().segment("dummy");
        ExecutionStepInfo info = ExecutionStepInfo.newExecutionStepInfo()
                .path(path)
                .type(Scalars.GraphQLString)
                .fieldDefinition(fd)
                .build();

        // 5) Build DataFetchingEnvironment with mergedField and executionInfo
        env = DataFetchingEnvironmentImpl.newDataFetchingEnvironment()
                .mergedField(mergedField)
                .executionStepInfo(info)
                .build();
    }

    @Test
    void handleBusinessException() {
        BusinessException ex = new BusinessException(CommonErrorCode.INVALID_INPUT_VALUE);
        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertEquals(CommonErrorCode.INVALID_INPUT_VALUE.getMessage(), error.getMessage());
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus().value(), ext.get("statusCode"));
        assertEquals(CommonErrorCode.INVALID_INPUT_VALUE.getCode(), ext.get("errorCode"));
    }

    @Test
    void handleNoResourceFoundException() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/test");
        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertEquals(ex.getMessage(), error.getMessage());
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(404, ext.get("statusCode"));
        assertEquals("RESOURCE_NOT_FOUND", ext.get("errorCode"));
    }

    @Test
    void handleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertTrue(error.getMessage().contains("지원하지 않는 HTTP 메서드"));
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(405, ext.get("statusCode"));
        assertEquals("METHOD_NOT_ALLOWED", ext.get("errorCode"));
    }

    /**
     * Tests the handling of a `MethodArgumentNotValidException` thrown due to invalid method argument constraints.
     *
     * The test verifies the following behavior:
     * 1. Constructs an invalid method parameter using the `Dummy` class with a validation error.
     * 2. Simulates the exception `MethodArgumentNotValidException` using a mocked `BindingResult`.
     * 3. Uses the `resolver` to process the exception and map it to a list of `GraphQLError` results.
     *
     * Assertions:
     * - Verifies that the list of errors is not null and contains exactly one error.
     * - Ensures the error message is consistent with the validation error details.
     * - Verifies the custom extensions in the `GraphQLError` object, including:
     *   - `status` indicating the API failure status.
     *   - `statusCode` confirming the HTTP status code (400 in this case).
     *   - `errorCode` identifying the specific error type (`INVALID_ARGUMENT`).
     *   - `field` indicating the invalid parameter name.
     *
     * Note:
     * This test checks how validation errors are properly encapsulated and surfaced via GraphQL error responses.
     *
     * @throws NoSuchMethodException if the method being tested does not exist
     */
    @Test
    void handleMethodArgumentNotValidException() throws NoSuchMethodException {
        class Dummy { public void dummy(@SuppressWarnings("unused") String param) {} }
        Method method = Dummy.class.getMethod("dummy", String.class);
        MethodParameter methodParam = new MethodParameter(method, 0);
        Dummy target = new Dummy();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "dummy");
        bindingResult.addError(new FieldError("dummy", "param", "must not be blank"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParam, bindingResult);

        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertEquals("must not be blank", error.getMessage());
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(400, ext.get("statusCode"));
        assertEquals("INVALID_ARGUMENT", ext.get("errorCode"));
        assertEquals("param", ext.get("field"));
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON parse error");
        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertEquals("잘못된 요청 메시지 형식입니다.", error.getMessage());
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(400, ext.get("statusCode"));
        assertEquals("MESSAGE_NOT_READABLE", ext.get("errorCode"));
    }

    @Test
    void handleGenericException() {
        Exception ex = new Exception("boom");
        List<GraphQLError> errors = resolver.resolveException(ex, env).block();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        GraphQLError error = errors.get(0);

        assertEquals("Internal server error", error.getMessage());
        Map<String, Object> ext = error.getExtensions();
        assertEquals(ApiStatus.FAILURE, ext.get("status"));
        assertEquals(500, ext.get("statusCode"));
        assertEquals("INTERNAL_ERROR", ext.get("errorCode"));
    }
}
