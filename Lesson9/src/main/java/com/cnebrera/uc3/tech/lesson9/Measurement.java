package com.cnebrera.uc3.tech.lesson9;

import com.cnebrera.uc3.tech.lesson9.jaxb.JaxbSerializer;
import com.cnebrera.uc3.tech.lesson9.json.JsonSerializer;
import com.cnebrera.uc3.tech.lesson9.kryo.KryoSerializer;
import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.cnebrera.uc3.tech.lesson9.proto.Lesson9;
import com.cnebrera.uc3.tech.lesson9.proto.ProtoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main Class that measure the performance
 */
public class Measurement {
  private static final long NUM_ITERATIONS = 100000000;
  /**
   * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Measurement.class);

  private static final JaxbSerializer jaxbSerializer = new JaxbSerializer();
  private static final JsonSerializer jsonSerializer = new JsonSerializer();
  private static final KryoSerializer kryoSerializer = new KryoSerializer();
  private static final ProtoSerializer protoSerializer = new ProtoSerializer();

  public static void main(String[] args) throws URISyntaxException, IOException {


    //Read the info from a xml and populate the class

    URL url = Measurement.class.getClassLoader().getResource("Example.xml");
    URL urlJson = Measurement.class.getClassLoader().getResource("Example.json");

    String str = new String(Files.readAllBytes(Paths.get(url.toURI())));
    String json = new String(Files.readAllBytes(Paths.get(urlJson.toURI())));

    ReferenceData referenceData = jaxbSerializer.deserialize(str);

    LOGGER.debug("[Practica 1] Size of referenceData instrument list {}", referenceData.getListOfInstruments().size());
    LOGGER.debug("[Practica 1] Algorithm identifier{}", referenceData.getAlgorithmIdentifier());
    LOGGER.debug("[Practica 1] Algorithm marketId{}", referenceData.getMarketId());

    LOGGER.debug("[Practica 2] Json Serializer [{}] ", referenceData.equals(jsonSerializer.deserialize(json)));

    Lesson9.ReferenceData.Builder referenceDataBuilder = Lesson9.ReferenceData.newBuilder();

    // Set the parameters in the builder using the values read in referenceData from JSON to ensure both have the same contents
    referenceDataBuilder.setMarketId(referenceData.getMarketId())
        .setAlgorithmIdentifier(referenceData.getAlgorithmIdentifier());
    for (Instrument value : referenceData.getListOfInstruments()) {
      referenceDataBuilder.addInstrument(Lesson9.Instrument.newBuilder()
          .setInstrumentId(value.getInstrumentId())
          .setSymbol(value.getSymbol()));
    }

    //Test Proto
    Lesson9.ReferenceData referenceDataProto = referenceDataBuilder.build();
    LOGGER.debug("[Practica 3] Proto Serializer [{}] ", referenceDataProto.equals(protoSerializer.deserialize(protoSerializer.serialize(referenceDataProto))));

    //Test Kryo
    LOGGER.debug("[Practica 4] Kryo Serializer [{}] ", referenceData.equals(kryoSerializer.deserialize(kryoSerializer.serialize(referenceData))));

    //Test performance serialization
    testPerformanceSerialization(referenceData, referenceDataProto);

    //Test performance deserialization
    testPerformanceDeSerialization(str.getBytes(), jsonSerializer.serialize(referenceData).getBytes(), kryoSerializer.serialize(referenceData), referenceDataProto.toByteArray());

    //Test performance serialization and deserialization
    testPerformanceSerializationAndDeserialization(referenceData, referenceDataProto);
  }

  private static void testPerformanceSerialization(ReferenceData referenceData, Lesson9.ReferenceData referenceDataProto) {
    //JAXB serialization
    long jaxbSerializationIni = System.nanoTime();
    String obj = "";
    byte[] objJaxb = null;
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to String and transform it to bytes
      //TODO store result in objJaxb
    }
    long jaxbSerializationFin = System.nanoTime();
    long meanJaxb = (jaxbSerializationFin - jaxbSerializationIni) / NUM_ITERATIONS;

    //Json serialization
    long jsonSerializationIni = System.nanoTime();
    byte[] objJson = null;
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to String and transform it to bytes
      //TODO store result in objJson
    }
    long jsonSerializationFin = System.nanoTime();
    long meanJson = (jsonSerializationFin - jsonSerializationIni) / NUM_ITERATIONS;

    //Protocol Buffers serialization
    long protoSerializationIni = System.nanoTime();
    byte[] objProto = null;
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to bytes
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

  }

  private static void testPerformanceDeSerialization(byte[] jaxbSerialize, byte[] jsonSerialize, byte[] kryoSerialize, byte[] protoSerialize) {
    //JAXB serialization
    long jaxbSerializationIni = System.nanoTime();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Transform jaxbSerialize to String and deserialize to ReferenceData
    }
    long jaxbSerializationFin = System.nanoTime();
    long meanJaxb = (jaxbSerializationFin - jaxbSerializationIni) / NUM_ITERATIONS;

    //Json serialization
    long jsonSerializationIni = System.nanoTime();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Transform jsonSerialize to String and deserialize to ReferenceData
    }
    long jsonSerializationFin = System.nanoTime();
    long meanJson = (jsonSerializationFin - jsonSerializationIni) / NUM_ITERATIONS;

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

  private static void testPerformanceSerializationAndDeserialization(ReferenceData referenceData, Lesson9.ReferenceData referenceDataProto) {
    //JAXB serialization
    long jaxbSerializationIni = System.nanoTime();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to String, transform it to bytes, transform it back to String and deserialize
    }
    long jaxbSerializationFin = System.nanoTime();
    long meanJaxb = (jaxbSerializationFin - jaxbSerializationIni) / NUM_ITERATIONS;

    //Json serialization
    long jsonSerializationIni = System.nanoTime();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to String, transform it to bytes, transform it back to String and deserialize
    }
    long jsonSerializationFin = System.nanoTime();
    long meanJson = (jsonSerializationFin - jsonSerializationIni) / NUM_ITERATIONS;

    //Protocol Buffers serialization
    long protoSerializationIni = System.nanoTime();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      //TODO Serialize referenceData to bytes and deserialize to ReferenceData
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

