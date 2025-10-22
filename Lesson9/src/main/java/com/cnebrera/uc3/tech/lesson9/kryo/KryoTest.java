package com.cnebrera.uc3.tech.lesson9.kryo;

import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.cnebrera.uc3.tech.lesson9.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Class that measure the performance
 */
public class KryoTest {
    /**
     * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KryoTest.class);

    private static final KryoSerializer kryoSerializer = new KryoSerializer();

    public static void main(String[] args) {
        //Create test object
        ReferenceData referenceData = Utils.getReferenceData();

        //Test Kryo
        LOGGER.debug("[Task 2] Kryo Serializer [{}] ", referenceData.equals(
                kryoSerializer.deserialize(kryoSerializer.serialize(referenceData))));
    }
}

