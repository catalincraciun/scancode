package com.ichack.scancode.model.corners;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * A class that handles the calculation of the position of the corners
 * for a given image.
 */
public class CornerAnalyzer {

  /**
   * The black sensitivity threshold parameter used to determine whether
   * two pixels are considered to have the "same" colour.
   */
  private static final int BLACK_SENSITIVITY = 60;

  /**
   * The pre-decided colour of the border.
   */
  private static final Color borderColor = new Color(0, 0, 0);

  /**
   * The colour cyan, which is used for the background of the images.
   */
  private static final Color cyan = new Color(0, 220, 220);

  /**
   * The image for which the calculation is done.
   */
  private final PictureUtils picture;

  /**
   * Four fields which are used to store the four detected corners
   * of the image, after the calculation is done.
   */
  private Point<Integer> topLeft;
  private Point<Integer> topRight;
  private Point<Integer> bottomLeft;
  private Point<Integer> bottomRight;

  /**
   * An array used to determine which pixels of the border have been
   * already found by the search. Refer to the contour method.
   */
  private final boolean[][] visited;

  /**
   * Two lists used to remember the coordinates of the border pixels.
   */
  private final List<Integer> xsBorder;
  private final List<Integer> ysBorder;

  /**
   * Construct a new CornerAnalyzer object from the given picture.
   *
   * @param picture The picture for which the calculation is done.
   */
  public CornerAnalyzer(PictureUtils picture) {
    xsBorder = new ArrayList<>();
    ysBorder = new ArrayList<>();
    visited = new boolean[picture.getWidth()][picture.getHeight()];

    this.picture = picture;
  }

  /**
   * Checks pixels until one that belongs to the border is found,
   * then calls the bfsHelper method on it to determine the whole border.
   */
  private void contour() {
    for (int x = 0; x < picture.getWidth(); x++) {
      for (int y = 0; y < picture.getHeight(); y++) {
        if (picture.getPixel(x, y).sameColor(borderColor, BLACK_SENSITIVITY)
            && checkPixel(x, y) && !visited[x][y]) {
          bfsHelper(x, y);
          visited[x][y] = true;
          return;
        } else if (!picture.getPixel(x, y).sameColor(borderColor, BLACK_SENSITIVITY)) {
          visited[x][y] = true;
        }
      }
    }
  }

  /**
   * Calculates and stores the corners of the image in the designated fields.
   */
  public void calculateCorners() {
    contour();
    corners(xsBorder, ysBorder);
  }

  /**
   * Returns the top left corner of the picture.
   *
   * @return top left corner of the picture.
   */
  public Point<Integer> getTopLeft() {
    return topLeft;
  }

  /**
   * Returns the top right corner of the picture.
   *
   * @return top right corner of the picture.
   */
  public Point<Integer> getTopRight() {
    return topRight;
  }

  /**
   * Returns the bottom left corner of the picture.
   *
   * @return bottom left corner of the picture.
   */
  public Point<Integer> getBottomLeft() {
    return bottomLeft;
  }

  /**
   * Returns the bottom right corner of the picture.
   *
   * @return bottom right corner of the picture.
   */
  public Point<Integer> getBottomRight() {
    return bottomRight;
  }

  /**
   * Checks whether a pixel is close to the cyan background of the image.
   * @param x the x coordinate of the pixel.
   * @param y the y coordinate of the pixel.
   * @return boolean representing whether the pixel is
   * close to the cyan background or not.
   */
  private boolean checkPixel(int x, int y) {
    boolean success = false;
    for (int i = -5; i <= 5; i++) {
      for (int j = -5; j <= 5; j++) {
        if (picture.contains(new Point<>(x + i, y + j)) &&
            picture.getPixel(x + i, y + j).sameColor(cyan, BLACK_SENSITIVITY)) {
            success = true;
        }
      }
    }

    return success;
  }

  /**
   * Performs a BFS from a given starting pixel in order to determine the
   * border of the image.
   *
   * @param x the x coordinate of the starting point.
   * @param y the y coordinate of the starting point.
   */
  private void bfsHelper(int x, int y) {
    Deque<Point<Integer>> queue = new ArrayDeque<>();
    queue.push(new Point<>(x, y));

    while (!queue.isEmpty()) {
      Point<Integer> popped = queue.pollLast();
      xsBorder.add(popped.getX());
      ysBorder.add(popped.getY());

      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          Point<Integer> next = new Point<>(popped.getX() + i, popped.getY() + j);

          if (picture.contains(next) &&
              borderColor.sameColor(picture.getPixel(next.getX(), next.getY()), BLACK_SENSITIVITY) &&
              !visited[next.getX()][next.getY()]) {
              visited[next.getX()][next.getY()] = true;
              queue.addLast(next);
          }
        }
      }
    }
  }

  /**
   * Returns the distance between two pixels.
   *
   * @param x1 x coordinate of the first pixel.
   * @param y1 y coordinate of the first pixel.
   * @param x2 x coordinate of the second pixel.
   * @param y2 y coordinate of the second pixel.
   * @return the distance between the two pixels.
   */
  private double dist(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  }

  /**
   * Determines the corners of an image.
   * @param x list representing the x coordinates of the border pixels.
   * @param y list representing the y coordinates of the border pixels.
   */
  private void corners(List<Integer> x, List<Integer> y) {
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
