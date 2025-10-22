package com.cnebrera.uc3.tech.lesson9.proto;

import com.cnebrera.uc3.tech.lesson9.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Class that measure the performance
 */
public class ProtoTest {
    /**
     * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoTest.class);

    private static final ProtoSerializer protoSerializer = new ProtoSerializer();

    public static void main(String[] args) {
        //Create test object
        Lesson9.ReferenceData referenceDataProto = Utils.getProtoReferenceData();

        // Test
        LOGGER.debug("[Task 1] Proto Serializer [{}] ", referenceDataProto.equals(
                protoSerializer.deserialize(protoSerializer.serialize(referenceDataProto))));
    }
}

