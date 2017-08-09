package com.ichack.scancode.model.corners;

public class Point {

  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Point setX(int newX) {
    return new Point(newX, y);
  }

  public Point setY(int newY) {
    return new Point(x, newY);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
