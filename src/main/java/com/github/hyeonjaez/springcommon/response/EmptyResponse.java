package com.github.hyeonjaez.springcommon.response;

/**
 * API 응답에서 반환할 데이터가 없을 때 사용되는 단일톤(싱글턴) 응답 객체입니다.
 *
 * <p>
 * 일반적으로 HTTP 204 (No Content) 또는 성공하지만 반환할 구체적인 데이터가 없는 경우에
 * {@link com.github.hyeonjaez.springcommon.response.ApiResponse}의 제네릭 타입으로 사용됩니다.
 * </p>
 *
 * <p>
 * 이 클래스는 객체 생성을 제한하고, 내부 정적 클래스를 이용한 Bill Pugh Solution(Lazy Initialization)을 통해
 * 메모리 효율적인 싱글턴 객체를 제공합니다.
 * </p>
 *
 * <pre>{@code
 * // 예시 사용
 * return ApiResponseEntityBuilder.noContent();
 * // body: { status: "SUCCESS", message: "...", data: {} }
 * }</pre>
 *
 * @author fiat_lux
 * @since 1.0.0
 */
public final class EmptyResponse {

    /**
     * 내부 클래스 기반의 싱글턴 홀더 패턴을 사용하여 Lazy Initialization을 제공합니다.
     */
    private static class EmptyResponseInstanceHolder {
        private static final EmptyResponse INSTANCE = new EmptyResponse();
    }

    /**
     * 싱글턴 인스턴스를 반환합니다.
     *
     * @return 전역에서 공유 가능한 EmptyResponse 인스턴스
     */
    public static EmptyResponse getInstance() {
        return EmptyResponseInstanceHolder.INSTANCE;
    }

    /**
     * 외부에서 인스턴스 생성을 방지하기 위한 private 생성자입니다.
     */
    private EmptyResponse() {
    }
}
