package com.ichack.scancode.model.corners;

/**
 * Models a generic point with two coordinates
 *
 * @param <T> The type of the coordinates
 */
public class Point<T> {

  /**
   * The x coordinate of your point
   */
  private final T x;

  /**
   * The y coordinate of your point
   */
  private final T y;

  /**
   * Constructs a point out of two coordinatines
   *
   * @param x The x coordinate of your point
   * @param y The y coordinate of your point
   */
  public Point(T x, T y) {
    this.x = x;
    this.y = y;
  }

  /**
   * @return The x coordinate of this point
   */
  public T getX() {
    return x;
  }

  /**
   * @return The y coordinate of this point
   */
  public T getY() {
    return y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
