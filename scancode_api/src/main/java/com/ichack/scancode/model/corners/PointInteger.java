package com.ichack.scancode.model.corners;

/**
 * Extends the generic class Point providing a distanceTo method for double coords
 */
public class PointInteger extends Point<Integer> {

  public PointInteger(int x, int y) {
    super(x, y);
  }

  /**
   * Calculates the distance to another PointDouble
   *
   * @param other The point to calculate the distance to
   * @return A double representing the distance from this point to your point
   */
  public double distanceTo(PointInteger other) {
    return Math.sqrt((this.getX() - other.getX()) * (this.getX() - other.getX()) +
        (this.getY() - other.getY()) * (this.getY() - other.getY()));
  }
}
