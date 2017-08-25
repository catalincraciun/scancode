package com.ichack.scancode.model.geometry;

/**
 * Models a line composite of two Points with double coordinates
 */
public class Line {

  private static final double EPSILON = 0.001;

  private final Point p1;
  private final Point p2;

  private final double a;
  private final double b;
  private final double c;

  private final boolean isVertical;

  public Line(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
    if (!(p2.getY().doubleValue() - p1.getY().doubleValue() < EPSILON && p1.getY().doubleValue() - p2.getY().doubleValue() < EPSILON)) {
      this.a = 1;
      this.b = (p2.getX().doubleValue() - p1.getX().doubleValue()) / (p1.getY().doubleValue() - p2.getY().doubleValue());
      this.c = -(a * p1.getX().doubleValue() + b * p1.getY().doubleValue());
      isVertical = false;
    } else {
      this.a = 0;
      this.b = 1;
      this.c = -p1.getY().doubleValue();
      isVertical = true;
    }
  }

  private double getA() {
    return a;
  }

  private double getB() {
    return b;
  }

  private double getC() {
    return c;
  }

  public Point getMiddle(double dist1) {
    return new Point(
        (p2.getX().doubleValue() - p1.getX().doubleValue()) * dist1 + p1.getX().doubleValue(),
        (p2.getY().doubleValue() - p1.getY().doubleValue()) * dist1 + p1.getY().doubleValue());
  }

  public Point getIntersection(Line other) {
    if (!isVertical && !other.isVertical) {
      double y = (c - other.getC()) / (other.getB() - b);
      return new Point(-c - y * b, y);
    } else if (isVertical) {
      double y = c;
      return new Point((-other.getC() - other.getB() * y) / other.getA(), y);
    } else {
      double y = other.getC();
      return new Point((-c - b * y) / a, y);
    }
  }

  public double getLength() {
    return p1.distanceTo(p2);
  }
}
