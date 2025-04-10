package com.github.hyeonjaez.springcommon.util;

import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import com.github.hyeonjaez.springcommon.exception.BusinessException;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@code ObjectsUtil} is a utility class specialized in checking {@code null} values
 * and validating ID values for {@code Integer} and {@code Long} types.
 *
 * <p>
 * It provides common validation logic for null checks and ID value constraints.
 * If an ID is {@code null} or less than or equal to zero, a {@link BusinessException} is thrown.
 * </p>
 *
 * <p>
 * By encapsulating common validation logic, it helps reduce code duplication
 * and maintain consistent exception handling across the application.
 * </p>
 *
 * <pre>{@code
 * // Example usage:
 * ObjectsUtil.checkAllNotNull(userId, contentId);
 * ObjectsUtil.checkIdLongs(1L, 2L);
 * if (ObjectsUtil.isNull(obj)) { ... }
 * }</pre>
 *
 * @author fiat_lux
 * @since 0.0.1
 */
public final class ObjectsUtil {

    /**
     * Checks if the given object is {@code null} and throws a {@link BusinessException} if so.
     *
     * @param object the object to check
     * @param <T>    the type of the object
     * @throws BusinessException if {@code object} is {@code null}
     */
    public static <T> void checkNotNull(T object) {
        if (isNull(object)) {
            throw new BusinessException(CommonErrorCode.PARAMETER_NULL);
        }
    }

    /**
     * Checks if all provided objects are not {@code null}.
     * Throws a {@link BusinessException} if any object is {@code null}.
     *
     * @param objects objects to check
     * @throws BusinessException if any object is {@code null}
     */
    public static void checkAllNotNull(Object... objects) {
        Arrays.stream(objects)
                .forEach(ObjectsUtil::checkNotNull);
    }

    /**
     * Returns whether the given object is {@code null}.
     *
     * @param object the object to check
     * @return {@code true} if {@code object} is {@code null}, otherwise {@code false}
     */
    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    /**
     * Returns whether the given object is not {@code null}.
     *
     * @param object the object to check
     * @return {@code true} if {@code object} is not {@code null}, otherwise {@code false}
     */
    public static boolean isNotNull(Object object) {
        return Objects.nonNull(object);
    }

    /**
     * Validates that all provided {@code Integer} IDs are not {@code null} and greater than zero.
     *
     * @param ids the {@code Integer} IDs to validate
     * @throws BusinessException if any ID is {@code null} or less than or equal to zero
     */
    public static void checkIdIntegers(Integer... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdIntegerValid);
    }

    /**
     * Validates that all provided {@code Long} IDs are not {@code null} and greater than zero.
     *
     * @param ids the {@code Long} IDs to validate
     * @throws BusinessException if any ID is {@code null} or less than or equal to zero
     */
    public static void checkIdLongs(Long... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdLongValid);
    }

    /**
     * Validates that the given {@code Integer} ID is not {@code null} and greater than zero.
     *
     * @param id the {@code Integer} ID to validate
     * @throws BusinessException if the ID is {@code null} or less than or equal to zero
     */
    public static void checkIdIntegerValid(Integer id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new BusinessException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }

    /**
     * Validates that the given {@code Long} ID is not {@code null} and greater than zero.
     *
     * @param id the {@code Long} ID to validate
     * @throws BusinessException if the ID is {@code null} or less than or equal to zero
     */
    public static void checkIdLongValid(Long id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new BusinessException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }


    /**
     * Private constructor to prevent instantiation.
     *
     * <p>This is a utility class and should not be instantiated.</p>
     */
    private ObjectsUtil() {
        throw new UnsupportedOperationException("Utility Class");
    }
}
