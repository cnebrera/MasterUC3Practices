package com.cnebrera.uc3.tech.lesson9.json;

import com.cnebrera.uc3.tech.lesson9.jaxb.JaxbSerializer;
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
public class JsonTest
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonTest.class);

    private final static JaxbSerializer jaxbSerializer = new JaxbSerializer();
    private final static JsonSerializer jsonSerializer = new JsonSerializer();

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        URL url = JsonTest.class.getClassLoader().getResource("Example.xml");
        URL urlJson = JsonTest.class.getClassLoader().getResource("Example.json");

        // Read both files and convert to plain string
        String str = new String(Files.readAllBytes(Paths.get(url.toURI())));
        String json = new String(Files.readAllBytes(Paths.get(urlJson.toURI())));

        ReferenceData referenceData = jaxbSerializer.deserialize(str);

        LOGGER.debug("[Practica 1] Size of referenceData instrument list {}", referenceData.getListOfInstruments().size());
        LOGGER.debug("[Practica 1] Algorithm identifier{}", referenceData.getAlgorithmIdentifier());
        LOGGER.debug("[Practica 1] Algorithm marketId{}", referenceData.getMarketId());

        LOGGER.debug("[Practica 2] Json Serializer [{}] ", referenceData.equals(jsonSerializer.deserialize(json)));
    }
}

