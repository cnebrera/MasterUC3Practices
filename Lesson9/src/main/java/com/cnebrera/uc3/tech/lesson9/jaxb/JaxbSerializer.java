package com.cnebrera.uc3.tech.lesson9.jaxb;

import com.cnebrera.uc3.tech.lesson9.Serializer;
import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by alexvarela on 23/11/16.
 */
public class JaxbSerializer implements Serializer<ReferenceData, String>
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(JaxbSerializer.class);

    /** The {@link javax.xml.bind.Marshaller} in order to serialize the xml */
    private Marshaller marshaller;

    /** The {@link javax.xml.bind.Unmarshaller} in order to deserialize the xml */
    private Unmarshaller unmarshaller;

    public JaxbSerializer()
    {
        JAXBContext jaxbContext = null;
        try
        {
            jaxbContext = JAXBContext.newInstance(ReferenceData.class, Instrument.class);

        }
        catch (JAXBException e)
        {
           LOGGER.error("Error initialising the JAXBConetxt", e);

        }
        try
        {
            this.marshaller = jaxbContext.createMarshaller();
        }
        catch (JAXBException e)
        {
            LOGGER.error("Error creating the Marshaller", e);
        }


        try
        {
            this.unmarshaller = jaxbContext.createUnmarshaller();
        }
        catch (JAXBException e)
        {
            LOGGER.error("Error creating the Unmarshaller", e);
        }

    }


    public String serialize(ReferenceData referenceData)
    {
        StringWriter stringWriter = new StringWriter();

        try
        {
            this.marshaller.marshal(referenceData, stringWriter);
        }
        catch (JAXBException e)
        {
            System.err.println(e);
        }
        return stringWriter.toString();
    }

    public ReferenceData deserialize(String rawData)
    {
        StringReader reader = new StringReader(rawData);
        ReferenceData referenceData = null;
        try
        {
            referenceData = (ReferenceData) this.unmarshaller.unmarshal(reader);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

        return referenceData;
    }
}
