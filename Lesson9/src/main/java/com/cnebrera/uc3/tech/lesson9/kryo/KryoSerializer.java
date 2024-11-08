package com.cnebrera.uc3.tech.lesson9.kryo;

import com.cnebrera.uc3.tech.lesson9.Serializer;
import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;


/**
 * Kryo Serializer
 */
public class KryoSerializer implements Serializer<ReferenceData, byte[]> {

  private final Kryo kryo;

  public KryoSerializer() {
    kryo = new Kryo();
    kryo.register(ReferenceData.class);
    kryo.register(Instrument.class);
    kryo.register(ArrayList.class);
  }


  public byte[] serialize(ReferenceData referenceData) {
    Output output = new Output(1024);
    kryo.writeObject(output, referenceData);
    return output.toBytes();
  }

  public ReferenceData deserialize(byte[] rawData) {
    return kryo.readObject(new Input(rawData), ReferenceData.class);
  }
}
