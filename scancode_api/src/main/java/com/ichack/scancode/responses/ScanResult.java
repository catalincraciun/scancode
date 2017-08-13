package com.ichack.scancode.responses;

/**
 * Models the response of the API for the 'scanCode' method
 */
public class ScanResult {

  private final String data;

  public ScanResult(String data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof ScanResult) {
      ScanResult other = (ScanResult) o;
      return other.data.equals(data);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }
}
