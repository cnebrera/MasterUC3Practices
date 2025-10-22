package com.cnebrera.uc3.tech.lesson9;

import com.cnebrera.uc3.tech.lesson9.kryo.KryoSerializer;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.cnebrera.uc3.tech.lesson9.proto.Lesson9;
import com.cnebrera.uc3.tech.lesson9.proto.ProtoSerializer;
import com.cnebrera.uc3.tech.lesson9.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Class that measure the performance
 */
public class Measurement {
    private static final long NUM_ITERATIONS = 5000000;
    /**
     * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Measurement.class);

    private static final KryoSerializer kryoSerializer = new KryoSerializer();
    private static final ProtoSerializer protoSerializer = new ProtoSerializer();

    public static void main(String[] args) {
        //Create test objects
        ReferenceData referenceData = Utils.getReferenceData();
        Lesson9.ReferenceData referenceDataProto = Utils.getProtoReferenceData();

        LOGGER.debug("[Lesson 9] Size of referenceData instrument list {}",
                referenceData.getListOfInstruments().size());
        LOGGER.debug("[Lesson 9] Algorithm identifier{}",
                referenceData.getAlgorithmIdentifier());
        LOGGER.debug("[Lesson 9] Algorithm marketId{}",
                referenceData.getMarketId());

        //Test Proto
        LOGGER.debug("[Lesson 9] Proto Serializer [{}] ", referenceDataProto.equals(
                protoSerializer.deserialize(protoSerializer.serialize(referenceDataProto))));

        //Test Kryo
        LOGGER.debug("[Lesson 9] Kryo Serializer [{}] ", referenceData.equals(
                kryoSerializer.deserialize(kryoSerializer.serialize(referenceData))));

        //Test performance serialization
        testPerformanceSerialization(referenceData, referenceDataProto);

        //Test performance deserialization
        testPerformanceDeSerialization(kryoSerializer.serialize(referenceData), referenceDataProto.toByteArray());

        //Test performance serialization and deserialization
        testPerformanceSerializationAndDeserialization(referenceData, referenceDataProto);
    }

    private static void testPerformanceSerialization(ReferenceData referenceData,
                                                     Lesson9.ReferenceData referenceDataProto) {
        //Protocol Buffers serialization
        long protoSerializationIni = System.nanoTime();
        byte[] objProto = null;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Serialize referenceDataProto to bytes
            //TODO store result in objProto

        }
        long protoSerializationFin = System.nanoTime();
        long meanProto = (protoSerializationFin - protoSerializationIni) / NUM_ITERATIONS;

        //Kryo serialization
        long kryoSerializationIni = System.nanoTime();
        byte[] objKryo = null;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Serialize referenceData to bytes
            //TODO store result in objKryo

        }
        long kryoSerializationFin = System.nanoTime();
        long meanKryo = (kryoSerializationFin - kryoSerializationIni) / NUM_ITERATIONS;

        //TODO Show ALL saved means using the LOGGER
        LOGGER.info("Results for Serialization");

        //TODO Show ALL saved object sizes using the LOGGER
        LOGGER.info("Object size");

    }

    private static void testPerformanceDeSerialization(byte[] kryoSerialize,
                                                       byte[] protoSerialize) {
        //Protocol Buffers serialization
        long protoSerializationIni = System.nanoTime();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Deserialize protoSerialize to ReferenceData

        }
        long protoSerializationFin = System.nanoTime();
        long meanProto = (protoSerializationFin - protoSerializationIni) / NUM_ITERATIONS;

        //Kryo serialization
        long kryoSerializationIni = System.nanoTime();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Deserialize kryoSerialize to ReferenceData

        }
        long kryoSerializationFin = System.nanoTime();
        long meanKryo = (kryoSerializationFin - kryoSerializationIni) / NUM_ITERATIONS;

        //TODO Show ALL saved means using the LOGGER
        LOGGER.info("Results for Deserialization");

    }

    private static void testPerformanceSerializationAndDeserialization(ReferenceData referenceData,
                                                                       Lesson9.ReferenceData referenceDataProto) {
        //Protocol Buffers serialization
        long protoSerializationIni = System.nanoTime();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Serialize referenceDataProto to bytes and deserialize to ReferenceDataProto

        }
        long protoSerializationFin = System.nanoTime();
        long meanProto = (protoSerializationFin - protoSerializationIni) / NUM_ITERATIONS;

        //Kryo serialization
        long kryoSerializationIni = System.nanoTime();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            //TODO Serialize referenceData to bytes and deserialize to ReferenceData

        }
        long kryoSerializationFin = System.nanoTime();
        long meanKryo = (kryoSerializationFin - kryoSerializationIni) / NUM_ITERATIONS;

        //TODO Show ALL saved means using the LOGGER
        LOGGER.info("Results for Serialization-Deserialization");

    }

}
