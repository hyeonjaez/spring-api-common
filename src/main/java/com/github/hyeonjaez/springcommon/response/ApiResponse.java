package com.github.hyeonjaez.springcommon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.hyeonjaez.springcommon.util.ObjectsUtil;

/**
 * 공통 API 응답 구조를 정의하는 제네릭 클래스입니다.
 *
 * <p>
 * 모든 API 응답은 이 클래스를 통해 감싸져 클라이언트에게 전달됩니다.
 * 응답은 세 가지 주요 필드로 구성됩니다:
 * <ul>
 *     <li>{@code status} - 응답의 처리 상태 ({@link ApiStatus})</li>
 *     <li>{@code message} - 사용자에게 전달할 설명 메시지</li>
 *     <li>{@code data} - 실제 응답 데이터</li>
 * </ul>
 * </p>
 *
 * <p>
 * {@code @JsonInclude(JsonInclude.Include.NON_NULL)} 설정으로 인해,
 * 필드가 {@code null}일 경우 JSON 응답에서 제외되어 불필요한 데이터 전송을 방지합니다.
 * </p>
 *
 * <pre>{@code
 * // 사용 예시
 * ApiResponse<UserDto> response = new ApiResponse<>(ApiStatus.SUCCESS, "요청 성공", userDto);
 * }</pre>
 *
 * @param <T> 응답 데이터의 타입
 * @author fiat_lux
 * @see ApiStatus
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final ApiStatus status;
    private final String message;
    private final T data;

    public ApiResponse(ApiStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(Builder<T> builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public ApiStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }


    public static class Builder<T> {
        private ApiStatus status = ApiStatus.SUCCESS;
        private String message = "요청이 처리되었습니다.";
        private T data;

        public Builder<T> status(ApiStatus status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponse<T> build() {
            if (ObjectsUtil.isNull(this.data)) {
                data = (T) EmptyResponse.getInstance();
            }

            return new ApiResponse<>(this);
        }


    }
}