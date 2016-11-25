package com.cnebrera.uc3.tech.lesson9.proto;

import com.cnebrera.uc3.tech.lesson9.Serializer;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by alexvarela on 23/11/16.
 */
public class ProtoSerializer implements Serializer<Lesson9.ReferenceData, byte[]>
{
    public byte[] serialize(Lesson9.ReferenceData referenceData)
    {
        return referenceData.toByteArray();
    }

    public Lesson9.ReferenceData deserialize(byte[] rawData)
    {
        Lesson9.ReferenceData referenceData = null;
        try
        {
            referenceData = Lesson9.ReferenceData.parseFrom(rawData);
        }
        catch (InvalidProtocolBufferException e)
        {
            System.err.println(e);
        }
        return referenceData;
    }
}
