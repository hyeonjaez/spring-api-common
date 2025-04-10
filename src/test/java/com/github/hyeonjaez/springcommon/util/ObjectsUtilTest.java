package com.github.hyeonjaez.springcommon.util;

import com.github.hyeonjaez.springcommon.exception.CommonErrorCode;
import com.github.hyeonjaez.springcommon.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsUtilTest {
    @Test
    @DisplayName("checkAllNotNull - 모두 null 아님 -> 예외 발생 안함")
    void testCheckAllNotNullSuccess() {
        assertDoesNotThrow(() -> ObjectsUtil.checkAllNotNull("abc", 123, new Object()));
    }

    @Test
    @DisplayName("checkNotNull - null 아님 -> 예외 발생 안함")
    void testCheckNotNullSuccess() {
        assertDoesNotThrow(() -> ObjectsUtil.checkAllNotNull("abc"));
    }

    @Test
    @DisplayName("checkAllNotNull - 하나라도 null -> CustomException 발생 및 CommonErrorCode.PARAMETER_NULL")
    void testCheckAllNotNullFail() {
        BusinessException resultException = assertThrows(BusinessException.class, () -> ObjectsUtil.checkAllNotNull("abc", new Object(), null));
        assertEquals(CommonErrorCode.PARAMETER_NULL, resultException.getErrorCode());
    }

    @Test
    @DisplayName("checkNotNull - null 이면 -> CustomException 발생 및 CommonErrorCode.PARAMETER_NULL")
    void testCheckNotNullFail() {
        BusinessException resultException = assertThrows(BusinessException.class, () -> ObjectsUtil.checkNotNull(null));
        assertEquals(CommonErrorCode.PARAMETER_NULL, resultException.getErrorCode());
    }

    @Test
    @DisplayName("isNull - null 이면 true 반환")
    void testIsNullTrue() {
        assertTrue(ObjectsUtil.isNull(null));
    }

    @Test
    @DisplayName("isNull - 객체가 존재하면 false 반환")
    void testIsNullFalse() {
        assertFalse(ObjectsUtil.isNull("test"));
    }

    @Test
    @DisplayName("isNotNull - null 이 아니면 true 반환")
    void testIsNotNullTrue() {
        assertTrue(ObjectsUtil.isNotNull("test"));
    }

    @Test
    @DisplayName("isNotNull - null 이면 false 반환")
    void testIsNotNullFalse() {
        assertFalse(ObjectsUtil.isNotNull(null));
    }

    @Test
    @DisplayName("checkIdIntegers - 모든 ID가 유효하면 예외 없음")
    void testCheckIdIntegersValid() {
        assertDoesNotThrow(() -> ObjectsUtil.checkIdIntegers(1, 100));
    }

    @Test
    @DisplayName("checkIdIntegers - null 포함 시 예외")
    void testCheckIdIntegersNull() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> ObjectsUtil.checkIdIntegers(1, null));
        assertEquals(CommonErrorCode.PARAMETER_NULL, exception.getErrorCode());
    }

    @Test
    @DisplayName("checkIdIntegers - 0 이하 값 포함 시 예외")
    void testCheckIdIntegersInvalid() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> ObjectsUtil.checkIdIntegers(0, 10));
        assertEquals(CommonErrorCode.PARAMETER_ID_VALUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("checkIdLongs - 모든 ID가 유효하면 예외 없음")
    void testCheckIdLongsValid() {
        assertDoesNotThrow(() -> ObjectsUtil.checkIdLongs(1L, 100L));
    }

    @Test
    @DisplayName("checkIdLongs - null 포함 시 예외")
    void testCheckIdLongsNull() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> ObjectsUtil.checkIdLongs(null, 10L));
        assertEquals(CommonErrorCode.PARAMETER_NULL, exception.getErrorCode());
    }

    @Test
    @DisplayName("checkIdLongs - 0 이하 값 포함 시 예외")
    void testCheckIdLongsInvalid() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> ObjectsUtil.checkIdLongs(-1L));
        assertEquals(CommonErrorCode.PARAMETER_ID_VALUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("private 생성자 호출 시 UnsupportedOperationException 발생")
    void testPrivateConstructor() throws Exception {
        Constructor<ObjectsUtil> constructor = ObjectsUtil.class.getDeclaredConstructor();
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