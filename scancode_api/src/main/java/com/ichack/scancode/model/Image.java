package com.ichack.scancode.model;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.awt.image.BufferedImage;

public class Image {

  private final String base64;
  private BufferedImage image;
  private static final String defaultLocation =
      "src/main/resources/baseImage512x512.png";
  private int sideLength = 0; //nr of squares in a row

  public Image(String base64) throws IOException {
    this.base64 = base64;

    BASE64Decoder decoder = new BASE64Decoder();
    byte[] imageByte = decoder.decodeBuffer(base64);
    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
    this.image = ImageIO.read(bis);
  }

  public Image(long code) throws IOException {
    try {
      this.image = ImageIO.read(new File(defaultLocation));
      sideLength = image.getWidth() / 16;
    }
    catch (IOException e) {
      e.printStackTrace();
    }

     for (int i = 0; i < 33; i ++) {
      if ((code & ((long) 1 << (32 - i))) != 0L) {
        drawTopLeft(i);
      }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, "png", baos);
    byte[] imageBytes = baos.toByteArray();

    this.base64 = Base64.getEncoder().encodeToString(imageBytes);
  }

  public String getBase64() {
    return base64;
  }

  public BufferedImage getImage() {
    return image;
  }

  public Color getPixel(int x, int y) {
    return new Color(image.getRGB(x, y));
  }

  private void drawCircle(int centerX, int centerY) {
    int radius = sideLength * 3 / 8;

    for (int i = centerX - radius; i <= centerX + radius; i ++) {
      for (int j = centerY - (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX));
           j <= centerY + (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX)); j ++) {
        image.setRGB(i, j, new Color(0, 0, 0).getRGB());
      }
    }
  }

  private void drawTopLeft(int i) {
    int topLeftX = 0;
    int topLeftY = 0;

    if (0 <= i && i <= 6) {

      topLeftX = (i % 7 + 1) * sideLength;
      topLeftY = sideLength;

    } else if (7 <= i && i <= 13) {

      topLeftX = (i % 7 + 1) * sideLength;
      topLeftY = 2 * sideLength;

    } else if (14 <= i && i <= 20) {

      topLeftX = (i % 7 + 1) * sideLength;
      topLeftY = 3 * sideLength;

    } else if (21 <= i && i <= 23) {

      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 4 * sideLength;

    } else if (24 <= i & i <= 26) {

      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 5 * sideLength;

    } else if (27 <= i & i <= 29) {

      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 6 * sideLength;

    } else if (30 <= i & i <= 32) {

      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 7 * sideLength;

    }

    drawCircle(topLeftX + sideLength / 2, topLeftY + sideLength / 2);

    drawTopRight(topLeftX + sideLength / 2, topLeftY + sideLength / 2);
  }

  private void drawTopRight(int i, int j) {
    int topRightX = image.getHeight() - j - 1;
    int topRightY = i;

    drawCircle(topRightX, topRightY);
    drawBottomRight(topRightX, topRightY);
  }

  private void drawBottomRight(int i, int j) {
    int bottomRightX = image.getHeight() - j - 1;
    int bottomRightY = i;

    drawCircle(bottomRightX, bottomRightY);
    drawBottomLeft(bottomRightX, bottomRightY);
  }

  private void drawBottomLeft(int i, int j) {
    int bottomLeftX = image.getHeight() - j - 1;
    int bottomLeftY = i;

    drawCircle(bottomLeftX, bottomLeftY);
  }
}
