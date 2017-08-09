package com.ichack.scancode.model;

public class ImageJSON {

  private String apiKey;
  private String image;

  public ImageJSON(String apiKey, String image) {
    this.apiKey = apiKey;
    this.image = image;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getImage() {
    return image;
  }
}
