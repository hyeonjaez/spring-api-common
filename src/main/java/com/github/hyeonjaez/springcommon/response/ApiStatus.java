package com.github.hyeonjaez.springcommon.response;

/**
 * Enum representing the status of an API response.
 *
 * <p>
 * Each status indicates the result of processing an API request and helps the client interpret the response.
 * This is commonly used within {@link com.github.hyeonjaez.springcommon.response.ApiResponse}.
 * </p>
 *
 * <ul>
 *     <li>{@code SUCCESS} - Indicates the request was successfully processed (HTTP 2xx)</li>
 *     <li>{@code FAILURE} - Indicates the request failed due to a client-side error (HTTP 4xx)</li>
 *     <li>{@code ERROR} - Indicates the request failed due to a server-side error (HTTP 5xx)</li>
 * </ul>
 *
 * <p>
 * For example, a validation error would result in a {@code FAILURE} status,
 * while an unexpected exception would result in an {@code ERROR} status.
 * </p>
 *
 * @author fiat_lux
 * @see com.github.hyeonjaez.springcommon.response.ApiResponse
 * @since 0.0.1
 */
public enum ApiStatus {

    /**
     * Indicates that the request was successfully processed.
     */
    SUCCESS,

    /**
     * Indicates a failure caused by client-side input or request error.
     */
    FAILURE,

    /**
     * Indicates a failure caused by a server-side exception or internal error.
     */
    ERROR
}
