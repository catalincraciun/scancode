package com.ichack.scancode.model;

import java.awt.Color;
import java.io.ByteArrayInputStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.awt.image.BufferedImage;

public class Image {

  private static final String DEFAULT_LOCATION = "src/main/resources/baseImage512x512.png";

  private final String base64;
  private BufferedImage bufferedImage;
  private int sideLength = 0; //nr of squares in a row

  public Image(String base64) throws IOException {
    this.base64 = base64;

    byte[] bufferedImageByte = Base64.getDecoder().decode(base64);
    ByteArrayInputStream bis = new ByteArrayInputStream(bufferedImageByte);
    this.bufferedImage = ImageIO.read(bis);
  }

  public Image(long code) throws IOException {
    try {
      this.bufferedImage = ImageIO.read(new File(DEFAULT_LOCATION));
      sideLength = bufferedImage.getWidth() / 16;
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
    }

    for (int i = 0; i < 33; i++) {
      if ((code & ((long) 1 << (32 - i))) != 0L) {
        drawTopLeft(i);
      }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "png", baos);
    byte[] bufferedImageBytes = baos.toByteArray();

    this.base64 = Base64.getEncoder().encodeToString(bufferedImageBytes);
  }

  public String getBase64() {
    return base64;
  }

  public BufferedImage getImage() {
    return bufferedImage;
  }

  public Color getPixel(int x, int y) {
    return new Color(bufferedImage.getRGB(x, y));
  }

  private void drawCircle(int centerX, int centerY) {
    int radius = sideLength * 3 / 8;

    for (int i = centerX - radius; i <= centerX + radius; i++) {
      for (int j = centerY - (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX));
          j <= centerY + (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX)); j++) {
        bufferedImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
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
    } else if (24 <= i && i <= 26) {
      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 5 * sideLength;
    } else if (27 <= i && i <= 29) {
      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 6 * sideLength;
    } else if (30 <= i && i <= 32) {
      topLeftX = (i % 3 + 1) * sideLength;
      topLeftY = 7 * sideLength;
    }

    drawCircle(topLeftX + sideLength / 2, topLeftY + sideLength / 2);
    drawTopRight(topLeftX + sideLength / 2, topLeftY + sideLength / 2);
  }

  private void drawTopRight(int i, int j) {
    int topRightX = bufferedImage.getHeight() - j - 1;
    drawCircle(topRightX, i);
    drawBottomRight(topRightX, i);
  }

  private void drawBottomRight(int i, int j) {
    int bottomRightX = bufferedImage.getHeight() - j - 1;
    drawCircle(bottomRightX, i);
    drawBottomLeft(bottomRightX, i);
  }

  private void drawBottomLeft(int i, int j) {
    int bottomLeftX = bufferedImage.getHeight() - j - 1;
    drawCircle(bottomLeftX, i);
  }
}
