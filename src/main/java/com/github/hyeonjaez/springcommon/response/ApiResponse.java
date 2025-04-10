package com.github.hyeonjaez.springcommon.response;

import com.github.hyeonjaez.springcommon.util.ObjectsUtil;

/**
 * Defines a common API response structure using generics.
 *
 * <p>
 * All API responses are wrapped in this class and returned to the client
 * with a consistent format including status, message, and data fields.
 * </p>
 *
 * <p>
 * This class does not rely on any specific JSON library.
 * If you want to exclude {@code null} fields from the JSON output,
 * configure your JSON serializer (e.g., Jackson) accordingly.
 * </p>
 *
 * <pre>{@code
 * ApiResponse<UserDto> response = new ApiResponse<>(ApiStatus.SUCCESS, "Request successful", userDto);
 * }</pre>
 *
 * @param <T> the type of the response body
 * @author fiat_lux
 * @see ApiStatus
 * @since 0.0.1
 */
public class ApiResponse<T> {
    /**
     * Indicates the processing status of the API response (e.g., SUCCESS, FAILURE, ERROR).
     */
    private final ApiStatus status;

    /**
     * A human-readable message intended for the end user or client developer.
     */
    private final String message;

    /**
     * The actual data returned in the response. This could be any type.
     */
    private final T data;

    /**
     * Constructs an ApiResponse with the provided status, message, and data.
     *
     * @param status  the response status
     * @param message a descriptive message
     * @param data    the actual response payload
     */
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

    /**
     * Builder class for constructing {@link ApiResponse} instances in a fluent manner.
     *
     * @param <T> the type of data in the response
     */
    public static class Builder<T> {

        /**
         * The response status. Defaults to {@code ApiStatus.SUCCESS}.
         */
        private ApiStatus status = ApiStatus.SUCCESS;

        /**
         * The response message. Defaults to a generic success message.
         */
        private String message = "요청이 처리되었습니다.";

        /**
         * The actual payload to be included in the response.
         */
        private T data;

        /**
         * Sets the response status.
         *
         * @param status the API status
         * @return the builder instance
         */
        public Builder<T> status(ApiStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the response message.
         *
         * @param message the message to be included
         * @return the builder instance
         */
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the response data.
         *
         * @param data the data payload
         * @return the builder instance
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Builds and returns the final {@link ApiResponse} instance.
         * If data is {@code null}, it will be replaced with an instance of {@link EmptyResponse}.
         *
         * @return a fully constructed ApiResponse
         */
        public ApiResponse<T> build() {
            if (ObjectsUtil.isNull(this.data)) {
                data = (T) EmptyResponse.getInstance();
            }

            return new ApiResponse<>(this);
        }


    }
}