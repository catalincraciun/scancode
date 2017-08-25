package com.ichack.scancode.model.scanning;

import com.ichack.scancode.model.geometry.Point;
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
   * The colour cyan, which is used for the background of the codes.
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
  private Point topLeft;
  private Point topRight;
  private Point bottomLeft;
  private Point bottomRight;

  /**
   * An array used to determine which pixels of the border have been
   * already found by the search. Refer to the contour method.
   */
  private final boolean[][] visited;

  /**
   * A list used to remember the coordinates of the border pixels.
   */
  private final List<Point> borderPoints;

  /**
   * Construct a new CornerAnalyzer object from the given picture.
   *
   * @param picture The picture for which the calculation is done.
   */
  public CornerAnalyzer(PictureUtils picture) {
    borderPoints = new ArrayList<>();
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
  public void scanCorners() {
    contour();
    computeCorners(borderPoints);
  }

  /**
   * Returns the top left corner of the picture.
   *
   * @return top left corner of the picture.
   */
  public Point getTopLeft() {
    return topLeft;
  }

  /**
   * Returns the top right corner of the picture.
   *
   * @return top right corner of the picture.
   */
  public Point getTopRight() {
    return topRight;
  }

  /**
   * Returns the bottom left corner of the picture.
   *
   * @return bottom left corner of the picture.
   */
  public Point getBottomLeft() {
    return bottomLeft;
  }

  /**
   * Returns the bottom right corner of the picture.
   *
   * @return bottom right corner of the picture.
   */
  public Point getBottomRight() {
    return bottomRight;
  }

  /**
   * Checks whether a pixel is close to the cyan background of the image.
   *
   * @param x the x coordinate of the pixel.
   * @param y the y coordinate of the pixel.
   * @return boolean representing whether the pixel is
   * close to the cyan background or not.
   */
  private boolean checkPixel(int x, int y) {
    boolean success = false;
    for (int i = -5; i <= 5; i++) {
      for (int j = -5; j <= 5; j++) {
        if (picture.contains(new Point(x + i, y + j)) &&
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
    Deque<Point> queue = new ArrayDeque<>();
    queue.push(new Point(x, y));

    while (!queue.isEmpty()) {
      Point popped = queue.pollLast();
      borderPoints.add(popped);

      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          Point next = new Point(popped.getX().intValue() + i, popped.getY().intValue() + j);

          if (picture.contains(next) &&
              borderColor.sameColor(picture.getPixel(next.getX().intValue(),
                  next.getY().intValue()), BLACK_SENSITIVITY) &&
              !visited[next.getX().intValue()][next.getY().intValue()]) {
              visited[next.getX().intValue()][next.getY().intValue()] = true;
              queue.addLast(next);
          }
        }
      }
    }
  }

  /**
   * Determines the corners of an image.
   *
   * @param borderPoints list of points representing the border.
   */
  private void computeCorners(List<Point> borderPoints) {
    Point middle;

    double xMiddle = borderPoints.get(0).getX().doubleValue();
    double yMiddle = borderPoints.get(0).getY().doubleValue();

    for (int i = 1; i < borderPoints.size(); i++) {
      xMiddle += borderPoints.get(i).getX().doubleValue();
      yMiddle += borderPoints.get(i).getY().doubleValue();
    }

    middle = new Point(xMiddle / borderPoints.size(), yMiddle / borderPoints.size());

    calculateTopLeft(borderPoints, middle);
    calculateTopRight(borderPoints, middle);
    calculateBottomRight(borderPoints, middle);
    calculateBottomLeft(borderPoints, middle);
  }

  /**
   * Determines the top left corner.
   *
   * @param borderPoints a list representing the border points.
   * @param middle the middle point of the image.
   */
  private void calculateTopLeft(List<Point> borderPoints, Point middle) {
    double dist = 0;

    for (Point borderPoint : borderPoints) {
      if (isInTopLeftCorner(borderPoint, middle) &&
          dist < borderPoint.distanceTo(middle)) {
        dist = borderPoint.distanceTo(middle);
        topLeft = borderPoint;
      }
    }
  }

  /**
   * A helper method to determine whether the given point is in the
   * top left corner of the image, relative to the middle point.
   *
   * @param point the point to check.
   * @param middle the middle point.
   * @return a boolean representing whether the given point is in
   * the top left quarter.
   */
  private boolean isInTopLeftCorner(Point point, Point middle) {
    return point.getX().doubleValue() < middle.getX().doubleValue() &&
        point.getY().doubleValue() < middle.getY().doubleValue();
  }

  /**
   * Determines the top right corner.
   *
   * @param borderPoints a list representing the border points.
   * @param middle the middle point of the image.
   */
  private void calculateTopRight(List<Point> borderPoints, Point middle) {
    double dist = 0;

    for (Point borderPoint : borderPoints) {
      if (isInTopRightCorner(borderPoint, middle) &&
          dist < borderPoint.distanceTo(middle)) {
        dist = borderPoint.distanceTo(middle);
        topRight = borderPoint;
      }
    }
  }

  /**
   * A helper method to determine whether the given point is in the
   * top right corner of the image, relative to the middle point.
   *
   * @param point the point to check.
   * @param middle the middle point.
   * @return a boolean representing whether the given point is in
   * the top right corner.
   */
  private boolean isInTopRightCorner(Point point, Point middle) {
    return point.getX().doubleValue() > middle.getX().doubleValue() &&
        point.getY().doubleValue() < middle.getY().doubleValue();
  }

  /**
   * Determines the bottom right corner.
   *
   * @param borderPoints a list representing the border points.
   * @param middle the middle point of the image.
   */
  private void calculateBottomRight(List<Point> borderPoints, Point middle) {
    double dist = 0;

    for (Point borderPoint : borderPoints) {
      if (isInBottomRightCorner(borderPoint, middle) &&
          dist < borderPoint.distanceTo(middle)) {
        dist = borderPoint.distanceTo(middle);
        bottomRight = borderPoint;
      }
    }
  }

  /**
   * A helper method to determine whether the given point is in the
   * bottom right corner of the image, relative to the middle point.
   *
   * @param point the point to check.
   * @param middle the middle point.
   * @return a boolean representing whether the given point is in
   * the bottom right corner.
   */
  private boolean isInBottomRightCorner(Point point, Point middle) {
    return point.getX().doubleValue() > middle.getX().doubleValue() &&
        point.getY().doubleValue() > middle.getY().doubleValue();
  }

  /**
   * Determines the bottom left corner.
   *
   * @param borderPoints a list representing the border points.
   * @param middle the middle point of the image.
   */
  private void calculateBottomLeft(List<Point> borderPoints, Point middle) {
    double dist = 0;

    for (Point borderPoint : borderPoints) {
      if (isInBottomLeftCorner(borderPoint, middle) &&
          dist < borderPoint.distanceTo(middle)) {
        dist = borderPoint.distanceTo(middle);
        bottomLeft = borderPoint;
      }
    }
  }

  /**
   * A helper method to determine whether the given point is in the
   * bottom left corner of the image, relative to the middle point.
   *
   * @param point the point to check.
   * @param middle the middle point.
   * @return a boolean representing whether the given point is in
   * the bottom left corner.
   */
  private boolean isInBottomLeftCorner(Point point, Point middle) {
    return point.getX().doubleValue() < middle.getX().doubleValue() &&
        point.getY().doubleValue() > middle.getY().doubleValue();
  }
}
