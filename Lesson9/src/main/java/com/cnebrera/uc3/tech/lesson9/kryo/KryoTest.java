package com.cnebrera.uc3.tech.lesson9.kryo;

import com.cnebrera.uc3.tech.lesson9.jaxb.JaxbSerializer;
import com.cnebrera.uc3.tech.lesson9.json.JsonSerializer;
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
public class KryoTest
{
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(KryoTest.class);

    private final static JsonSerializer jsonSerializer = new JsonSerializer();
    private final static KryoSerializer kryoSerializer = new KryoSerializer();

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        //Read the info from a xml and populate the class
        URL urlJson = KryoTest.class.getClassLoader().getResource("Example.json");

        String json = new String(Files.readAllBytes(Paths.get(urlJson.toURI())));

        ReferenceData referenceData = jsonSerializer.deserialize(json);

        //Test Kryo
        LOGGER.debug("[Practica 4] Kryo Serializer [{}] ",referenceData.equals(kryoSerializer.deserialize(kryoSerializer.serialize(referenceData))));
    }
}

