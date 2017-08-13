package com.ichack.scancode.model.corners;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CornerAnalyzer {

  private static final int BLACK_SENSITIVITY = 60;
  private static final Color borderColor = new Color(0, 0, 0);
  private static final Color cyan = new Color(0, 220, 220);
  private PictureUtils picture;
  private boolean[][] visited;
  private List<Integer> xl;
  private List<Integer> yl;
  private Point<Integer> topLeft;
  private Point<Integer> topRight;
  private Point<Integer> bottomLeft;
  private Point<Integer> bottomRight;

  public CornerAnalyzer(PictureUtils picture) {
    xl = new ArrayList<>();
    yl = new ArrayList<>();
    visited = new boolean[picture.getWidth()][picture.getHeight()];

    this.picture = picture;
  }

  private void contour() {
    for (int x = 0; x < picture.getWidth(); x++) {
      for (int y = 0; y < picture.getHeight(); y++) {
        if (picture.getPixel(x, y).sameColor(borderColor, BLACK_SENSITIVITY)
            && checkPixel(x, y) && !visited[x][y]) {
          helper(x, y);
          visited[x][y] = true;
          return;
        } else if (!picture.getPixel(x, y).sameColor(borderColor, BLACK_SENSITIVITY)) {
          visited[x][y] = true;
        }
      }
    }
  }

  public void calculateCorners() {
    contour();
    check(xl, yl);
  }

  public Point<Integer> getTopLeft() {
    return topLeft;
  }

  public Point<Integer> getTopRight() {
    return topRight;
  }

  public Point<Integer> getBottomLeft() {
    return bottomLeft;
  }

  public Point<Integer> getBottomRight() {
    return bottomRight;
  }

  private boolean checkPixel(int x, int y) {
    boolean success = false;
    for (int i = -5; i <= 5; i++) {
      for (int j = -5; j <= 5; j++) {
        if (PictureUtils.isInBounds(picture, x + i, y + j)) {
          if (picture.getPixel(x + i, y + j).sameColor(cyan, BLACK_SENSITIVITY)) {
            success = true;
          }
        }
      }
    }

    return success;
  }

  private void helper(int x, int y) {
    Deque<Point<Integer>> queue = new ArrayDeque();
    queue.push(new Point<>(x, y));
    while (!queue.isEmpty()) {
      Point<Integer> popped = queue.pollLast();
      xl.add(popped.getX());
      yl.add(popped.getY());
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (PictureUtils.isInBounds(picture, popped.getX() + i, popped.getY() + j) &&
              borderColor.sameColor(picture.getPixel(popped.getX() + i, popped.getY() + j),
                  BLACK_SENSITIVITY)) {
            if (!visited[popped.getX() + i][popped.getY() + j]) {
              visited[popped.getX() + i][popped.getY() + j] = true;
              queue.addLast(new Point<>(popped.getX() + i, popped.getY() + j));
            }
          }
        }
      }
    }
  }


  private double dist(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  }

  private void check(List<Integer> x, List<Integer> y) {
    float xm = x.get(0);
    float ym = y.get(0);
    for (int i = 1; i < x.size(); i++) {
      xm += x.get(i);
      ym += y.get(i);
    }

    xm /= x.size();
    ym /= y.size();

    double dist = 0;
    int bestX = 0;
    int bestY = 0;
    for (int i = 0; i < x.size(); i++) {
      if (x.get(i) < xm && y.get(i) < ym && dist < dist((int) xm, (int) ym, x.get(i), y.get(i))) {
        dist = dist((int) xm, (int) ym, x.get(i), y.get(i));
        bestX = x.get(i);
        bestY = y.get(i);
      }
    }

    topLeft = new Point<>(bestX, bestY);

    dist = 0;
    bestX = 0;
    bestY = 0;
    for (int i = 0; i < x.size(); i++) {
      if (x.get(i) > xm && y.get(i) < ym && dist < dist((int) xm, (int) ym, x.get(i), y.get(i))) {
        dist = dist((int) xm, (int) ym, x.get(i), y.get(i));
        bestX = x.get(i);
        bestY = y.get(i);
      }
    }

    topRight = new Point<>(bestX, bestY);

    dist = 0;
    bestX = 0;
    bestY = 0;
    for (int i = 0; i < x.size(); i++) {
      if (x.get(i) > xm && y.get(i) > ym && dist < dist((int) xm, (int) ym, x.get(i), y.get(i))) {
        dist = dist((int) xm, (int) ym, x.get(i), y.get(i));
        bestX = x.get(i);
        bestY = y.get(i);
      }
    }

    bottomRight = new Point<>(bestX, bestY);

    dist = 0;
    bestX = 0;
    bestY = 0;
    for (int i = 0; i < x.size(); i++) {
      if (x.get(i) < xm && y.get(i) > ym && dist < dist((int) xm, (int) ym, x.get(i), y.get(i))) {
        dist = dist((int) xm, (int) ym, x.get(i), y.get(i));
        bestX = x.get(i);
        bestY = y.get(i);
      }
    }

    bottomLeft = new Point<>(bestX, bestY);
  }
}
