package com.cnebrera.uc3.tech.lesson9.jaxb;

import com.cnebrera.uc3.tech.lesson9.json.JsonSerializer;
import com.cnebrera.uc3.tech.lesson9.kryo.KryoSerializer;
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
public class JaxbTest
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(JaxbTest.class);

    private final static JaxbSerializer jaxbSerializer = new JaxbSerializer();

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        //Read the info from a xml and populate the class
        URL url = JaxbTest.class.getClassLoader().getResource("Example.xml");

        // Read the file and convert to plain string
        String str = new String(Files.readAllBytes(Paths.get(url.toURI())));

        ReferenceData referenceData = jaxbSerializer.deserialize(str);

        LOGGER.debug("[Practica 1] Size of referenceData instrument list {}", referenceData.getListOfInstruments().size());
        LOGGER.debug("[Practica 1] Algorithm identifier{}", referenceData.getAlgorithmIdentifier());
        LOGGER.debug("[Practica 1] Algorithm marketId{}", referenceData.getMarketId());
    }
}

