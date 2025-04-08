package com.github.hyeonjaez.springcommon.util;

import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import com.github.hyeonjaez.springcommon.exception.BusinessException;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@code ObjectsUtil}은 객체의 null 여부 및 ID 유효성 검사에 특화된 유틸리티 클래스입니다.
 *
 * <p>
 * 이 클래스는 null 검증과 함께 ID 값이 유효한지 검사하는 공통 로직을 제공하며,
 * {@code Integer}와 {@code Long} 타입의 ID 값이 {@code null}이거나 0 이하일 경우
 * {@link BusinessException}을 발생시킵니다.
 * </p>
 *
 * <p>
 * 공통된 검증 로직을 캡슐화함으로써 코드 중복을 줄이고, 예외 처리를 일관되게 적용할 수 있도록 돕습니다.
 * </p>
 *
 * <pre>{@code
 * // 사용 예시
 * ObjectsUtil.checkAllNotNull(userId, contentId);
 * ObjectsUtil.checkIdLongs(1L, 2L);
 * if (ObjectsUtil.isNull(obj)) { ... }
 * }</pre>
 *
 * @author fiat_lux
 * @since 1.0.0
 */
public final class ObjectsUtil {

    /**
     * 객체가 {@code null}인지 검사하고, {@code null}이면 예외를 발생시킵니다.
     *
     * @param object 검사할 객체
     * @param <T>    객체 타입
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_NULL} 예외 발생
     */
    public static <T> void checkNotNull(T object) {
        if (isNull(object)) {
            throw new BusinessException(CommonErrorCode.PARAMETER_NULL);
        }
    }

    /**
     * 전달된 모든 객체가 {@code null}이 아닌지 검사합니다.
     * 하나라도 {@code null}이면 {@link BusinessException}이 발생합니다.
     *
     * @param objects 검사할 객체 목록
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_NULL} 예외 발생
     */
    public static void checkAllNotNull(Object... objects) {
        Arrays.stream(objects)
                .forEach(ObjectsUtil::checkNotNull);
    }

    /**
     * 객체가 {@code null}인지 여부를 반환합니다.
     *
     * @param object 검사할 객체
     * @return {@code true}이면 {@code null}, 그렇지 않으면 {@code false}
     */
    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static boolean isNotNull(Object object) {
        return Objects.nonNull(object);
    }

    /**
     * 모든 {@code Integer} ID가 {@code null}이 아니고 0보다 큰지 검사합니다.
     *
     * @param ids 검사할 Integer ID 목록
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_NULL}, {@link CommonErrorCode#PARAMETER_ID_VALUE} 예외 발생
     */
    public static void checkIdIntegers(Integer... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdIntegerValid);
    }

    /**
     * 모든 {@code Long} ID가 {@code null}이 아니고 0보다 큰지 검사합니다.
     *
     * @param ids 검사할 Long ID 목록
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_NULL}, {@link CommonErrorCode#PARAMETER_ID_VALUE} 예외 발생
     */
    public static void checkIdLongs(Long... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdLongValid);
    }

    /**
     * 단일 {@code Integer} ID가 {@code null}이 아니고 0보다 큰지 검증합니다.
     *
     * @param id 검사할 Integer ID
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_ID_VALUE} 예외 발생
     */
    public static void checkIdIntegerValid(Integer id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new BusinessException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }

    /**
     * 단일 {@code Long} ID가 {@code null}이 아니고 0보다 큰지 검증합니다.
     *
     * @param id 검사할 Long ID
     * @throws BusinessException {@link CommonErrorCode#PARAMETER_ID_VALUE} 예외 발생
     */
    public static void checkIdLongValid(Long id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new BusinessException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }


    /**
     * 객체 생성을 방지하기 위한 private 생성자입니다.
     *
     * <p>이 클래스는 정적 메서드만 제공하는 유틸리티 클래스입니다.</p>
     */
    private ObjectsUtil() {
        throw new UnsupportedOperationException("Utility Class");
    }
}
