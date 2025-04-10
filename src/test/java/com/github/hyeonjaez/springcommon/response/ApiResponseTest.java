package com.github.hyeonjaez.springcommon.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    @DisplayName("builder() 사용 시 기본값이 설정되어야 한다")
    void testBuilderDefaultValue() {
        ApiResponse<Object> response = ApiResponse.builder().build();

        assertEquals(response.getStatus(), ApiStatus.SUCCESS);
        assertEquals(response.getMessage(), "요청이 처리되었습니다.");
        assertEquals(response.getData().getClass(), EmptyResponse.class);
    }

    @Test
    @DisplayName("builder 체이닝으로 필드 설정이 가능해야 한다")
    void testBuilderChaining() {
        String message = "xxx domain 요청 성공";
        String data = "result";

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(ApiStatus.SUCCESS)
                .message(message)
                .data(data)
                .build();

        assertEquals(response.getStatus(), ApiStatus.SUCCESS);
        assertEquals(response.getMessage(), message);
        assertEquals(response.getData(), data);
    }

    @Test
    @DisplayName("data가 null인 경우 EmptyResponse로 대체되어야 한다")
    void testNullDataIsReplaced() {
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .data(null)
                .build();

        assertEquals(response.getData().getClass(), EmptyResponse.class);
    }
}