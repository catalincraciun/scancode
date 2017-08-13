package com.ichack.scancode.model;

/**
 * Models the interaction with Storage
 */
public interface StorageGuard {

  /**
   * Checks if a code is inside the storage
   *
   * @param code The code you want to check for
   * @return True if storage has data for your code
   */
  boolean containsData(long code);

  /**
   * Adds data for a code to the storage
   *
   * @param code The code to which the data coresponds
   * @param data The data you want to store
   */
  void add(long code, String data);

  /**
   * Retrieves data associated with a code from the storage
   *
   * @param code The code you want to query data for
   * @return The data stored for your code
   */
  String getData(long code);
}
