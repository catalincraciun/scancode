package com.ichack.scancode.model.geometry;

/**
 * Models a generic point with two coordinates of Number subtypes
 */
public class Point<T extends Number> {

  /**
   * The x coordinate of your point
   */
  private final T x;

  /**
   * The y coordinate of your point
   */
  private final T y;

  /**
   * Constructs a point out of two Number coordinates
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

  /**
   * Computes the distance between this point and a given point.
   *
   * @param other The other point
   * @return A double representing the distance between the two points
   */
  public <S extends Number> double distanceTo(Point<S> other) {
    return Math.sqrt(Math.pow(this.x.doubleValue() - other.x.doubleValue(), 2)
        + Math.pow(this.y.doubleValue() - other.y.doubleValue(), 2));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
