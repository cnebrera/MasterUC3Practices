package com.cnebrera.uc3.tech.lesson9.proto;

import com.cnebrera.uc3.tech.lesson9.json.JsonSerializer;
import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
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
public class ProtoTest {
  /**
   * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ProtoTest.class);

  private static final JsonSerializer jsonSerializer = new JsonSerializer();
  private static final ProtoSerializer protoSerializer = new ProtoSerializer();

  public static void main(String[] args) throws URISyntaxException, IOException {
    //Read the info from a xml and populate the class
    URL urlJson = ProtoTest.class.getClassLoader().getResource("Example.json");

    String json = new String(Files.readAllBytes(Paths.get(urlJson.toURI())));

    // The reference data read from JSON
    ReferenceData referenceData = jsonSerializer.deserialize(json);

    // Create a new builder for a reference data object
    Lesson9.ReferenceData.Builder referenceDataBuilder = Lesson9.ReferenceData.newBuilder();

    // Set the parameters in the builder using the values read in referenceData from JSON to ensure both have the same contents
    referenceDataBuilder.setMarketId(referenceData.getMarketId())
        .setAlgorithmIdentifier(referenceData.getAlgorithmIdentifier());
    for (Instrument value : referenceData.getListOfInstruments()) {
      referenceDataBuilder.addInstrument(Lesson9.Instrument.newBuilder()
          .setInstrumentId(value.getInstrumentId())
          .setSymbol(value.getSymbol()));
    }

    // Call build on the builder to generate the object
    Lesson9.ReferenceData referenceDataProto = referenceDataBuilder.build();

    // Test
    LOGGER.debug("[Practica 3] Proto Serializer [{}] ", referenceDataProto.equals(protoSerializer.deserialize(protoSerializer.serialize(referenceDataProto))));
  }
}

