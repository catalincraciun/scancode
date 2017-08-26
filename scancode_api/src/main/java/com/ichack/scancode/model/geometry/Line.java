package com.ichack.scancode.model.geometry;

/**
 * Models a line composite of two Points
 */
public class Line {

  private static final double EPSILON = 0.001;

  private final Point<Double> p1;
  private final Point<Double> p2;

  private final double a;
  private final double b;
  private final double c;

  private final boolean isVertical;

  /**
   * Constructs a new line
   *
   * @param p1 One end of the line
   * @param p2 The other end of the line
   */
  public Line(Point<Double> p1, Point<Double> p2) {
    this.p1 = p1;
    this.p2 = p2;

    if (p2.getY() - p1.getY() >= EPSILON ||
        p1.getY() - p2.getY() >= EPSILON) {
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

  /**
   * Calculates a translated point on this line, by using the line as a unit
   * It will return the middle of the line for scale = 1 / 2
   *
   * @implNote This will go from p1 to p2, direction wise
   *
   * @param scale The number of units used for translation
   * @return The translated Point, always located on this line
   */
  public Point<Double> getTranslation(double scale) {
    return new Point<>(
        (p2.getX() - p1.getX()) * scale + p1.getX(),
        (p2.getY() - p1.getY()) * scale + p1.getY());
  }

  /**
   * Gets the point of intersection between two lines
   *
   * @param other The line with which you want to intersect this line
   * @return A Point representing the point of intersection
   */
  public Point<Double> getIntersection(Line other) {
    if (!isVertical && !other.isVertical) {
      double y = (c - other.c) / (other.b - b);
      return new Point<>(-c - y * b, y);
    } else if (isVertical) {
      double y = c;
      return new Point<>((-other.c - other.b * y) / other.a, y);
    } else {
      double y = other.c;
      return new Point<>((-c - b * y) / a, y);
    }
  }

  /**
   * Calculates the length of the line
   * @return A double representing the length of the line
   */
  public double getLength() {
    return p1.distanceTo(p2);
  }
}
