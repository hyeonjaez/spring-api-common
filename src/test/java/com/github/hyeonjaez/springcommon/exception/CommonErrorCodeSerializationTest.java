package com.github.hyeonjaez.springcommon.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommonErrorCodeSerializationTest {

    @Test
    @DisplayName("직렬화 테스트")
    void testCommonErrorCodeSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        CommonErrorCode original = CommonErrorCode.PARAMETER_NULL;

        byte[] serializedData;
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArray)) {
            objectOutputStream.writeObject(original);
            serializedData = byteArray.toByteArray();
        }

        CommonErrorCode deserialized;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
             ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream)) {
            deserialized = (CommonErrorCode) ois.readObject();
        }

        assertNotNull(deserialized);
        assertEquals(original, deserialized);
        assertEquals(original.getCode(), deserialized.getCode());
        assertEquals(original.getMessage(), deserialized.getMessage());
        assertEquals(original.getHttpStatus(), deserialized.getHttpStatus());
    }
}