package com.ichack.scancode.responses;

public class GeneratedCode {

  private String image;

  public GeneratedCode(String image) {
    this.image = image;
  }

  public String getImage() {
    return image;
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
