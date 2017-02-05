package hello.codeanalyzer;

/**
 * Created by paul on 04/02/17.
 */
public class Point {
  private double x,y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public Point heightFoot (Line line) {
    if (line.getB() == 0) {
      return new Point(-line.getC()/line.getA(), this.y);
    }

    double xH = (line.getB() * line.getB() * this.x - line.getA() * line.getB() * this.y
        - line.getA() * line.getC()) / (line.getA() * line.getA() + line.getB() * line.getB());

    return new Point(xH, (-line.getC() - line.getA() * xH) / line.getB());
  }

  public double distanceTo(Point other) {
    return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
  }
}
