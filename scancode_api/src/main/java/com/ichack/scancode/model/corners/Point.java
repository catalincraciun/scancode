package com.ichack.scancode.model.corners;

/**
 * Models a generic point with two coordinates
 * @param <T> The type of the coordinates
 */
public class Point<T> {

  private final T x;
  private final T y;

  public Point(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public T getX() {
    return x;
  }

  public T getY() {
    return y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
