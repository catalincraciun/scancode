package com.ichack.scancode.model.corners;

/**
 * Models a color using the RGB direct color-model.
 *
 * The individual red,green and blue components of a colour are assigned a value
 * ranging from 0 to 255. A component value of 0 signifies no contribution is
 * made to the color.
 */
class Color {

  /**
   * Red component
   */
  private final int red;

  /**
   * Green component
   */
  private final int green;

  /**
   * Blue component
   */
  private final int blue;

  /**
   * Default Construct. Construct a new Color object with the specified
   * intensity values for the red, green and blue components.
   *
   * @param red the intensity of the red component contributed to this Color.
   * @param green the intensity of the green component contributed to this Color.
   * @param blue the intensity of the blue component contributed to this Color.
   */
  Color(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Return the contribution of the red component to <tt>this</tt> Color.
   *
   * @return the intensity of the red component.
   */
  int getRed() {
    return red;
  }

  /**
   * Return the contribution of the green component to <tt>this</tt> Color.
   *
   * @return the intensity of the green component.
   */
  int getGreen() {
    return green;
  }

  /**
   * Return the contribution of the blue component to <tt>this</tt> Color.
   *
   * @return the intensity of the blue component.
   */
  int getBlue() {
    return blue;
  }

  /**
   * Compares two colors using sensitivity
   *
   * @param otherColor The color you want to compare to
   * @param sensitivity An integer representing the sensitivity (used like an EPSILON)
   * @return True if colors are approximately the same
   */
  boolean sameColor(Color otherColor, int sensitivity) {
    return Math.abs(otherColor.getRed() - red) < sensitivity &&
        Math.abs(otherColor.getBlue() - blue) < sensitivity &&
        Math.abs(otherColor.getGreen() - green) < sensitivity;
  }
}
