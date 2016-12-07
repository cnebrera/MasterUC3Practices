package com.cnebrera.uc3.tech.lesson9.json;

import com.cnebrera.uc3.tech.lesson9.Serializer;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.file.SymbolArchive;

import java.io.IOException;

/**
 * Created by alexvarela on 23/11/16.
 */
public class JsonSerializer implements Serializer<ReferenceData, String>
{

    /** The {@link com.fasterxml.jackson.databind.ObjectMapper} that helps to JSON serialization */
    private final ObjectMapper mapper;

    public JsonSerializer()
    {
        this.mapper = new ObjectMapper();
    }

    public String serialize(ReferenceData referenceData)
    {
        String result = null;
        try
        {
            result=  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(referenceData);
        }
        catch (JsonProcessingException e)
        {
            System.err.println(e);
        }

        return result;
    }

    public ReferenceData deserialize(String rawData)
    {
        ReferenceData referenceData = null;
        try
        {
            referenceData =  mapper.readValue(rawData, ReferenceData.class);;
        }
        catch (IOException e)
        {
            System.err.println(e);
        }

        return referenceData;
    }
}
