package com.ichack.scancode.responses;

public class ScanResult {

  private String data;

  public ScanResult(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof ScanResult) {
      ScanResult other = (ScanResult)o;
      return other.data.equals(data);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }
}
