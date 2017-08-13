package com.ichack.scancode.responses;

/**
 * Models the response of the API for the 'generateCode' method
 */
public class GeneratedCode {

  private final String image;

  public GeneratedCode(String image) {
    this.image = image;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof GeneratedCode) {
      GeneratedCode other = (GeneratedCode)o;
      return other.image.equals(image);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return image.hashCode();
  }
}
