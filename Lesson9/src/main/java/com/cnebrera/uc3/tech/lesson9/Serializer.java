package com.cnebrera.uc3.tech.lesson9;

/**
 * Comon interface
 */
public interface Serializer<K, T> {
  /**
   * Method to implement in order to serialize.
   *
   * @param referenceData
   * @return
   */
  T serialize(K referenceData);

  /**
   * Method to implement in order to deserialize.
   *
   * @param rawData
   * @return
   */
  K deserialize(T rawData);
}
