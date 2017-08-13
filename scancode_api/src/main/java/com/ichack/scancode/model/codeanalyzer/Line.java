package com.ichack.scancode.model.codeanalyzer;

import com.ichack.scancode.model.corners.PointDouble;

/**
 * Models a line composite of two Points with double coordinates
 */
class Line {

  private static final double EPSILON = 0.001;

  private final PointDouble p1;
  private final PointDouble p2;

  private final double a;
  private final double b;
  private final double c;

  private final boolean isVertical;

  Line(PointDouble p1, PointDouble p2) {
    this.p1 = p1;
    this.p2 = p2;
    if (!(p2.getY() - p1.getY() < EPSILON && p1.getY() - p2.getY() < EPSILON)) {
      this.a = 1;
      this.b = (p2.getX() - p1.getX()) / (p1.getY() - p2.getY());
      this.c = -(a * p1.getX() + b * p1.getY());
      isVertical = false;
    } else {
      this.a = 0;
      this.b = 1;
      this.c = -p1.getY();
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

  PointDouble getMiddle(double dist1) {
    return new PointDouble(
        (p2.getX() - p1.getX()) * dist1 + p1.getX(),
        (p2.getY() - p1.getY()) * dist1 + p1.getY());
  }

  PointDouble intersect(Line other) {
    if (!isVertical && !other.isVertical) {
      double y = (c - other.getC()) / (other.getB() - b);
      return new PointDouble(-c - y * b, y);
    } else if (isVertical) {
      double y = c;
      return new PointDouble((-other.getC() - other.getB() * y) / other.getA(), y);
    } else {
      double y = other.getC();
      return new PointDouble((-c - b * y) / a, y);
    }
  }

  double length() {
    return p1.distanceTo(p2);
  }
}
