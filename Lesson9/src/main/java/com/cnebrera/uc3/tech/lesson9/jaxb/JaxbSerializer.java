package com.cnebrera.uc3.tech.lesson9.jaxb;

import com.cnebrera.uc3.tech.lesson9.Serializer;
import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * JAXB Serializer
 */
public class JaxbSerializer implements Serializer<ReferenceData, String> {
  /**
   * a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(JaxbSerializer.class);

  /**
   * The {@link jakarta.xml.bind.Marshaller} in order to serialize the xml
   */
  private Marshaller marshaller;

  /**
   * The {@link jakarta.xml.bind.Unmarshaller} in order to deserialize the xml
   */
  private Unmarshaller unmarshaller;

  public JaxbSerializer() {
    JAXBContext jaxbContext = null;
    try {
      jaxbContext = JAXBContext.newInstance(ReferenceData.class, Instrument.class);

    } catch (JAXBException e) {
      LOGGER.error("Error initialising the JAXBConetxt", e);

    }
    try {
      this.marshaller = jaxbContext.createMarshaller();
    } catch (JAXBException e) {
      LOGGER.error("Error creating the Marshaller", e);
    }


    try {
      this.unmarshaller = jaxbContext.createUnmarshaller();
    } catch (JAXBException e) {
      LOGGER.error("Error creating the Unmarshaller", e);
    }

  }


  public String serialize(ReferenceData referenceData) {
    StringWriter stringWriter = new StringWriter();

    try {
      this.marshaller.marshal(referenceData, stringWriter);
    } catch (JAXBException e) {
      System.err.println(e);
    }
    return stringWriter.toString();
  }

  public ReferenceData deserialize(String rawData) {
    StringReader reader = new StringReader(rawData);
    ReferenceData referenceData = null;
    try {
      referenceData = (ReferenceData) this.unmarshaller.unmarshal(reader);
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    return referenceData;
  }
}
