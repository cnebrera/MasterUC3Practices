package com.cnebrera.uc3.tech.lesson9.proto;

import com.google.protobuf.InvalidProtocolBufferException;

public final class Lesson9
{
  private Lesson9()
  {
  }

  public static final class Instrument
  {

  }

  public static final class ReferenceData
  {

    public byte[] toByteArray()
    {
      return new byte[0];
    }

    public static ReferenceData parseFrom(byte[] rawData) throws InvalidProtocolBufferException
    {
      return null;
    }

    public static Builder newBuilder()
    {
      return null;
    }

    public class Builder
    {
      public ReferenceData build()
      {
        return null;
      }
    }
  }
}
