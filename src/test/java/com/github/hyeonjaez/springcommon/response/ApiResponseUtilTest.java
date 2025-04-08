package com.github.hyeonjaez.springcommon.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseUtilTest {

    @Test
    @DisplayName("ok(String message, T data) : 200 OK 응답에 ApiStatus.SUCCESS, 전달된 메시지와 데이터가 포함되어야 한다.")
    void testOkWithCustomMessageAndData() {
        String expectedMessage = "테스트 메시지";
        String expectedData = "테스트 데이터";

        ResponseEntity<ApiResponse<String>> resultResponse = ApiResponseUtil.ok(expectedMessage, expectedData);

        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertNotNull(resultResponse.getBody());
        assertEquals(ApiStatus.SUCCESS, resultResponse.getBody().getStatus());
        assertEquals(expectedMessage, resultResponse.getBody().getMessage());
        assertNotNull(resultResponse.getBody().getData());
        assertEquals(expectedData, resultResponse.getBody().getData());
    }

    @Test
    @DisplayName("ok(T data) : 200 OK 응답에 ApiStatus.SUCCESS, 기본 메시지와 전달된 데이터가 포함되어야 한다.")
    void testOkWithDefaultMessageAndCustomData() {
        String expectedData = "테스트 데이터";

        ResponseEntity<ApiResponse<String>> resultResponse = ApiResponseUtil.ok(expectedData);

        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertNotNull(resultResponse.getBody());
        assertEquals(ApiStatus.SUCCESS, resultResponse.getBody().getStatus());
        assertEquals(ApiResponseUtil.DEFAULT_OK_MESSAGE, resultResponse.getBody().getMessage());
        assertNotNull(resultResponse.getBody().getData());
        assertEquals(expectedData, resultResponse.getBody().getData());
    }

    @Test
    @DisplayName("created(String message, T data) : 201 Created 응답에 ApiStatus.SUCCESS, 전달된 메시지와 데이터가 포함되어야 한다.")
    void testCreatedWithCustomMessageAndData() {
        String expectedMessage = "테스트 메시지";
        String expectedData = "테스트 데이터";

        ResponseEntity<ApiResponse<String>> resultResponse = ApiResponseUtil.created(expectedMessage, expectedData);

        assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode());
        assertNotNull(resultResponse.getBody());
        assertEquals(ApiStatus.SUCCESS, resultResponse.getBody().getStatus());
        assertEquals(expectedMessage, resultResponse.getBody().getMessage());
        assertNotNull(resultResponse.getBody().getData());
        assertEquals(expectedData, resultResponse.getBody().getData());
    }

    @Test
    @DisplayName("created(T data) : 201 Created 응답에 ApiStatus.SUCCESS, 기본 메시지와 전달된 데이터가 포함되어야 한다.")
    void testCreatedWithDefaultMessageAndCustomData() {
        String expectedData = "테스트 데이터";

        ResponseEntity<ApiResponse<String>> resultResponse = ApiResponseUtil.created(expectedData);

        assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode());
        assertNotNull(resultResponse.getBody());
        assertEquals(ApiStatus.SUCCESS, resultResponse.getBody().getStatus());
        assertEquals(ApiResponseUtil.DEFAULT_CREATED_MESSAGE, resultResponse.getBody().getMessage());
        assertNotNull(resultResponse.getBody().getData());
        assertEquals(expectedData, resultResponse.getBody().getData());
    }

    @Test
    @DisplayName("noContent(String message) : 204 No Content 응답에 ApiStatus.SUCCESS, 전달된 메시지와 EmptyResponse 인스턴스가 포함되어야 한다.")
    void testNoContentWithCustomMessageAndData() {
        String expectedMessage = "테스트 메시지";
        EmptyResponse expected = EmptyResponse.getInstance();

        ResponseEntity<ApiResponse<EmptyResponse>> responseEntity = ApiResponseUtil.noContent(expectedMessage);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ApiStatus.SUCCESS, responseEntity.getBody().getStatus());
        assertEquals(expectedMessage, responseEntity.getBody().getMessage());
        assertEquals(expected, responseEntity.getBody().getData());
    }

    @Test
    @DisplayName("noContent() : 204 No Content 응답에 ApiStatus.SUCCESS, 기본 메시지와 EmptyResponse 인스턴스가 포함되어야 한다.")
    void testNoContentWithDefaultMessage() {
        EmptyResponse expected = EmptyResponse.getInstance();

        ResponseEntity<ApiResponse<EmptyResponse>> responseEntity = ApiResponseUtil.noContent();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ApiStatus.SUCCESS, responseEntity.getBody().getStatus());
        assertEquals(ApiResponseUtil.DEFAULT_NO_CONTENT_MESSAGE, responseEntity.getBody().getMessage());
        assertEquals(expected, responseEntity.getBody().getData());
    }

    @Test
    @DisplayName("build() : 모든 파라미터가 Null 아닐때 응답이 올바르게 구성된다")
    void build_allNotNull() {
        String message = "메시지";
        String data = "데이터";

        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.OK, message, data);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(ApiStatus.SUCCESS, result.getBody().getStatus());
        assertEquals(message, result.getBody().getMessage());
        assertEquals(data, result.getBody().getData());
    }

    @Test
    @DisplayName("build() : 파라미터 enum status 가 null 일 경우 기본값 SUCCESS 가 적용된다")
    void build_withNullStatus() {
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(null, HttpStatus.CREATED, "메시지", "데이터");

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(ApiStatus.SUCCESS, result.getBody().getStatus());
    }

    @Test
    @DisplayName("build() : 파라미터 httpStatus 가 null 일 경우 기본값 HttpStatus.OK 가 적용된다.")
    void build_withNullHttpStatus() {
        String message = "메시지";
        String data = "데이터";
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, null, message, data);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(ApiStatus.SUCCESS, result.getBody().getStatus());
        assertEquals(message, result.getBody().getMessage());
        assertEquals(data, result.getBody().getData());
    }

    @Test
    @DisplayName("build() : 파라미터 message 가 null 일 경우 HttpStatus 상태에 따라 기본 메시지가 적용된다 - ok")
    void build_withNullMessage_whenHttpStatus_isOK() {
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.OK, null, "데이터");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ApiResponseUtil.DEFAULT_OK_MESSAGE, Objects.requireNonNull(result.getBody()).getMessage());
    }

    @Test
    @DisplayName("build() : 파라미터 message 가 null 일 경우 HttpStatus 상태에 따라 기본 메시지가 적용된다 - created")
    void build_withNullMessage_whenHttpStatus_isCreated() {
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.CREATED, null, "데이터");

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(ApiResponseUtil.DEFAULT_CREATED_MESSAGE, Objects.requireNonNull(result.getBody()).getMessage());
    }

    @Test
    @DisplayName("build() : 파라미터 message 가 null 일 경우 HttpStatus 상태에 따라 기본 메시지가 적용된다 - no content")
    void build_withNullMessage_whenHttpStatus_isNO_Content() {
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.NO_CONTENT, null, "데이터");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(ApiResponseUtil.DEFAULT_NO_CONTENT_MESSAGE, Objects.requireNonNull(result.getBody()).getMessage());
    }

    @Test
    @DisplayName("build() : 파라미터 message 가 null 일 경우 HttpStatus 상태에 따라 기본 메시지가 적용된다 - no content")
    void build_withNullMessage_whenHttpStatus_otherStatus() {
        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.ACCEPTED, null, "데이터");

        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals(ApiResponseUtil.DEFAULT_MESSAGE, Objects.requireNonNull(result.getBody()).getMessage());
    }


    @Test
    @DisplayName("build() : 파라미터 data 가 null 일 경우 EmptyResponse 인스턴스가 사용된다")
    void build_withNullData() {
        ResponseEntity<ApiResponse<EmptyResponse>> result = ApiResponseUtil.build(ApiStatus.SUCCESS, HttpStatus.OK, "메시지", null);

        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getData());
        assertTrue(result.getBody().getData() instanceof EmptyResponse);
        assertEquals(EmptyResponse.getInstance(), result.getBody().getData());
    }

    @Test
    @DisplayName("build() : 파라미터 ApiStatus 생략 시에도 정상적으로 동작한다 - build 메서드 오버로딩")
    void build_onlyHttpStatusOverload() {
        String message = "테스트 메시지";
        String data = "테스트 데이터";

        ResponseEntity<ApiResponse<String>> result = ApiResponseUtil.build(HttpStatus.CREATED, message, data);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(ApiStatus.SUCCESS, Objects.requireNonNull(result.getBody()).getStatus());
        assertEquals(message, result.getBody().getMessage());
        assertEquals(data, result.getBody().getData());
    }

    @Test
    @DisplayName("유틸리티 클래스는 인스턴스화 할 수 없어야 한다")
    void testPrivateConstructor() throws Exception {
        Constructor<ApiResponseUtil> constructor = ApiResponseUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable exceptionCause = exception.getCause();
        assertNotNull(exceptionCause);
        assertInstanceOf(UnsupportedOperationException.class, exceptionCause);
        assertEquals("Utility Class", exceptionCause.getMessage());
    }

}