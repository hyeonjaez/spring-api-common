package com.github.hyeonjaez.springcommon.response;

/**
 * API 응답의 상태를 나타내는 열거형(enum)입니다.
 *
 * <p>
 * 각 상태는 API 요청에 대한 처리 결과를 의미하며, 클라이언트가 응답을 해석하는 데 도움을 줍니다.
 * 일반적으로 {@link com.github.hyeonjaez.springcommon.response.ApiResponse}에서 함께 사용됩니다.
 * </p>
 *
 * <ul>
 *     <li>{@code SUCCESS} - 요청이 성공적으로 처리됨을 나타냅니다. (HTTP 2xx 범위)</li>
 *     <li>{@code FAILURE} - 클라이언트 오류로 인해 요청이 실패했음을 나타냅니다. (HTTP 4xx 범위)</li>
 *     <li>{@code ERROR} - 서버 내부 오류 등으로 인해 요청이 실패했음을 나타냅니다. (HTTP 5xx 범위)</li>
 * </ul>
 *
 * <p>
 * 예를 들어, 입력값 검증 실패는 {@code FAILURE}, 서버 예외는 {@code ERROR} 상태로 응답됩니다.
 * </p>
 *
 * @author fiat_lux
 * @see com.github.hyeonjaez.springcommon.response.ApiResponse
 * @since 1.0.0
 */
public enum ApiStatus {

    /**
     * 요청이 정상적으로 처리되었을 때 사용됩니다.
     */
    SUCCESS,

    /**
     * 클라이언트 측 입력 오류 또는 요청 실패를 나타냅니다.
     */
    FAILURE,

    /**
     * 서버 측 예외 또는 예상치 못한 에러 발생 시 사용됩니다.
     */
    ERROR
}
