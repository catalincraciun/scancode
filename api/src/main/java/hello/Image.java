package hello;

import java.awt.*;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
      "baseImagecopy512x512.png";
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

  private void drawCircle(int x, int y) {
    int centerX = x + sideLength / 2;
    int centerY = y + sideLength / 2;
    int radius = sideLength * 3 / 8;

    for (int i = centerX - radius; i <= centerX + radius; i ++) {
      for (int j = centerY - (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX));
           j <= centerY + (int) Math.sqrt(radius * radius - (i - centerX) * (i - centerX)); j ++) {
        image.setRGB(i, j, new Color(0, 0, 0).getRGB());
      }
    }
  }

  private void drawTopLeft(int i) {
    if (0 <= i && i <= 6) {
      drawCircle((i % 7 + 1) * sideLength, sideLength);
    } else if (7 <= i && i <= 13) {
      drawCircle((i % 7 + 1) * sideLength, 2 * sideLength);
    } else if (14 <= i && i <= 20) {
      drawCircle((i % 7 + 1) * sideLength, 3 * sideLength);
    } else if (21 <= i && i <= 23) {
      drawCircle((i % 3 + 1) * sideLength, 4 * sideLength);
    } else if (24 <= i & i <= 26) {
      drawCircle((i % 3 + 1) * sideLength, 5 * sideLength);
    } else if (27 <= i & i <= 29) {
      drawCircle((i % 3 + 1) * sideLength, 6 * sideLength);
    } else if (30 <= i & i <= 32) {
      drawCircle((i % 3 + 1) * sideLength, 7 * sideLength);
    }
  }


}
