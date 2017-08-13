package com.ichack.scancode.responses;

/**
 * Models the response of the API for the 'generateCode' method
 */
public class GeneratedCode {

  private final Long id;
  private final String image;

  public GeneratedCode(Long id, String image) {
    this.id = id;
    this.image = image;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof GeneratedCode) {
      GeneratedCode other = (GeneratedCode)o;
      return other.image.equals(image) && other.id.equals(id);
    }

    return false;
  }

  public String getImage() {
    return image;
  }

  @Override
  public int hashCode() {
    return image.hashCode();
  }
}
