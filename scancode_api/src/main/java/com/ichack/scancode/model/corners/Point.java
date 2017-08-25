package com.ichack.scancode.model.corners;

/**
 * Models a generic point with two Number coordinates
 */
public class Point {

  /**
   * The x coordinate of your point
   */
  private final Number x;

  /**
   * The y coordinate of your point
   */
  private final Number y;

  /**
   * Constructs a point out of two Number coordinatines
   *
   * @param x The x coordinate of your point
   * @param y The y coordinate of your point
   */
  public Point(Number x, Number y) {
    this.x = x;
    this.y = y;
  }

  /**
   * @return The x coordinate of this point
   */
  public Number getX() {
    return x;
  }

  /**
   * @return The y coordinate of this point
   */
  public Number getY() {
    return y;
  }

  /**
   * Computes the distance between this point and a given point.
   *
   * @param other the other point.
   * @return a double representing the distance between the two points.
   */
  public double distanceTo(Point other) {
    return Math.sqrt(Math.pow(this.x.doubleValue() - other.x.doubleValue(), 2)
        + Math.pow(this.y.doubleValue() - other.y.doubleValue(), 2));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
