package hello.codeanalyzer;

/**
 * Created by paul on 04/02/17.
 */
public class Line {
  private final Point p1,p2;
  private final double a,b,c;
  private final boolean isVertical;
  private static double epsilon = 0.001;

  public double getA() {
    return a;
  }

  public double getB() {
    return b;
  }

  public double getC() {
    return c;
  }

  public Line(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
    if(!(p2.getY() - p1.getY() < epsilon && p1.getY() - p2.getY() < epsilon)) {
      this.a = 1;
      this.b = (p2.getX() - p1.getX()) / (p1.getY() - p2.getY());
      this.c = -(a * p1.getX() + b * p1.getY());
      isVertical = false;
    }
    else {
      this.a = 0;
      this.b = 1;
      this.c = -p1.getY();
      isVertical = true;
    }

  }


  public Point getMiddle() {
    return new Point ( (p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2);
  }

  public Point getMiddle(double dist1) {
    return new Point ( (p2.getX() - p1.getX())*dist1 + p1.getX(), (p2.getY() - p1.getY())*dist1 + p1.getY());

  }


  public Point intersect(Line other) {
    if (!(isVertical || other.isVertical)) {
      if(other.getB() == getB())
        System.out.println("*");
      double y = (getC() - other.getC()) / (other.getB() - getB());
      return new Point(
              -getC() - y * getB(),
              y);
    } else {
      if(isVertical){
        double y = getC();
        return new Point(
                (-other.getC() - other.getB()*y)/ other.getA(),
                y
        );
      }
      else {
        double y = other.getC();
        return new Point(
                (-getC() - getB()*y)/ getA(),
                y
        );
      }
      //System.out.println(p1.getX() + " " + p1.getY() + p2.getX() + p2.getY());
      //System.out.println(other.p1.getX() + " " + other.p1.getY() + other.p2.getX() + other.p2.getY());
    }
  }

  public double length() {
    return p1.distanceTo(p2);
  }
}
