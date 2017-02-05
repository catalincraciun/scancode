package hello.corners;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * A set of convenient methods to create and display Picture objects.
 */
public class Utils {

  /**
   * Hide default constructor (static methods only).
   */
  private Utils() {
  }

  /**
   * Create a new instance of a Picture object of the specified width and
   * height, using the RGB colour model.
   * 
   * @param width
   *          width of new Picture
   * @param height
   *          height of new Picture
   * @return a new instance of a Picture object of the specified size.
   */
  public static PictureUtils createPicture(int width, int height) {
    BufferedImage img = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
    return new PictureUtils(img);
  }

  /**
   * Create a Picture object from the the image at the specified location (URL
   * or local file).
   * 
   */
  public static PictureUtils loadPicture(String locationString) {

    final BufferedImage img;
    final BufferedImage origImage;

    URL locationURL = null;
    File locationFile = null;
    try {
      locationURL = new URL(locationString);
    } catch (MalformedURLException e) {
      File tmpFile = new File(locationString);
      if (tmpFile.exists() && tmpFile.canRead()) {
        locationFile = tmpFile;
      }
    }

    try {
      if (locationURL != null) {
        origImage = ImageIO.read(locationURL);
      } else if (locationFile != null) {
        origImage = ImageIO.read(locationFile);
      } else {
        return null;
      }
      
      if (origImage == null) {
        return null;
      }
      
    } catch (IOException e) {
      return null;
    }

    // If image loaded, then create a BufferedImage which is modifiable
    int imageWidth = origImage.getWidth(null);
    int imageHeight = origImage.getHeight(null);
    img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    Graphics g = img.createGraphics();
    g.drawImage(origImage, 0, 0, null);
    return new PictureUtils(img);
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   * 
   * @param picture
   *          the picture to convert to a String
   * @return a String representation of the specified Picture
   */
  public static String toArray(PictureUtils picture) {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < picture.getHeight(); y++) {
      for (int x = 0; x < picture.getWidth(); x++) {
        Color rgb = picture.getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Saves the given picture in png format in the given destination.
   * 
   * @param picture
   *          the picture to save to disk
   * @param destination
   *          where to save the picture
   * @return true iff the file was saved successfully
   */
  public static boolean savePicture(PictureUtils picture, String destination) {
    try {
      return ImageIO.write(picture.getImage(), "png", new File(destination));
    } catch (IOException e) {
      return false;
    }
  }
}
