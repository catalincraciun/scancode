package com.ichack.scancode.model.corners;

/**
 * Extends the generic class Point providing a distanceTo method for double coords
 */
public class PointDouble extends Point<Double> {

  public PointDouble(double x, double y) {
    super(x, y);
  }

  public double distanceTo(PointDouble other) {
    return Math.sqrt((this.getX() - other.getX()) * (this.getX() - other.getX()) +
        (this.getY() - other.getY()) * (this.getY() - other.getY()));
  }
}
