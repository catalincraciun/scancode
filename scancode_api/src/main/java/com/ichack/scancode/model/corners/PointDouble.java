package com.ichack.scancode.model.corners;

public class PointDouble extends Point<Double> {

  public PointDouble(double x, double y) {
    super(x, y);
  }

  public double distanceTo(PointDouble other) {
    return Math.sqrt((this.getX() - other.getX()) * (this.getX() - other.getX()) +
        (this.getY() - other.getY()) * (this.getY() - other.getY()));
  }
}
