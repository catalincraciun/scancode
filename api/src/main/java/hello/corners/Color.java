package hello.corners;

/**
 * Encapsulate the colours using the RGB direct color-model. The individual red,
 * green and blue components of a colour are assigned a value ranging from 0 to
 * 255. A component value of 0 signifies no contribution is made to the color.
 */
public class Color {

  /**
   * the intensity of the red component
   */
  private int red;

  /**
   * the intensity of the green component
   */
  private int green;

  /**
   * the intensity of the blue component
   */
  private int blue;

  /**
   * Default Construct. Construct a new Color object with the specified
   * intensity values for the red, green and blue components.
   * 
   * @param red
   *          the intensity of the red component contributed to this Color.
   * @param green
   *          the intensity of the green component contributed to this Color.
   * @param blue
   *          the intensity of the blue component contributed to this Color.
   */
  public Color(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Return the contribution of the red component to <tt>this</tt> Color.
   * 
   * @return the intensity of the red component.
   */
  public int getRed() {
    return red;
  }

  /**
   * Return the contribution of the green component to <tt>this</tt> Color.
   * 
   * @return the intensity of the green component.
   */
  public int getGreen() {
    return green;
  }

  /**
   * Return the contribution of the blue component to <tt>this</tt> Color.
   * 
   * @return the intensity of the blue component.
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Set the contribution of the red component to <tt>this</tt> Color.
   * 
   * @param red
   *          the new intensity value of the red component.
   */
  public void setRed(int red) {
    this.red = red;
  }

  /**
   * Set the contribution of the green component to <tt>this</tt> Color.
   * 
   * @param green
   *          the new intensity value of the green component.
   */
  public void setGreen(int green) {
    this.green = green;
  }

  /**
   * Set the contribution of the blue component to <tt>this</tt> Color.
   * 
   * @param blue
   *          the new intensity value of the blue component.
   */
  public void setBlue(int blue) {
    this.blue = blue;
  }

  public boolean sameColor(Color otherPixel, int sensitivity){
    return Math.abs(otherPixel.getRed() - red) < sensitivity &&
            Math.abs(otherPixel.getBlue() - blue) < sensitivity &&
            Math.abs(otherPixel.getGreen() - green) < sensitivity;
  }
}
