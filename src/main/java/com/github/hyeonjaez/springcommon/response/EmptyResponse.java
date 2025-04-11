package com.github.hyeonjaez.springcommon.response;

/**
 * A singleton response object used when there is no data to return in an API response.
 *
 * <p>
 * This class is typically used as the generic type of {@link com.github.hyeonjaez.springcommon.response.ApiResponse}
 * when the API returns a success response without any actual data, such as HTTP 204 (No Content).
 * </p>
 *
 * <p>
 * It uses the Bill Pugh Singleton pattern (static inner class) to provide a memory-efficient, lazily initialized singleton instance.
 * </p>
 *
 * <pre>{@code
 * // Example usage
 * return ApiResponseUtil.noContent();
 * // Response body: { status: "SUCCESS", message: "...", data: {} }
 * }</pre>
 *
 * @author fiat_lux
 * @since 0.0.1
 */
public final class EmptyResponse {

    private static final String NO_CONTENT_RESULT = "No content";

    /**
     * Holds the singleton instance using the Bill Pugh Singleton pattern.
     */
    private static class EmptyResponseInstanceHolder {
        private static final EmptyResponse INSTANCE = new EmptyResponse();
    }

    /**
     * Returns the singleton instance of {@code EmptyResponse}.
     *
     * @return a globally shared instance of {@code EmptyResponse}
     */
    public static EmptyResponse getInstance() {
        return EmptyResponseInstanceHolder.INSTANCE;
    }

    /**
     * Returns a fixed message indicating that no data is available.
     *
     * @return a default message string "No content"
     */
    public String getResult() {
        return NO_CONTENT_RESULT;
    }

    /**
     * Private constructor to prevent external instantiation.
     */
    private EmptyResponse() {
    }
}
