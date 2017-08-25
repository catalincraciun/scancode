package com.ichack.scancode.model.codeanalyzer;

import java.awt.Color;
import com.ichack.scancode.model.corners.Point;

/**
 * A LocatedPixel is a pixel made of a Color and a Point with double coordinates
 */
class LocatedPixel {

  private final Color pixel;
  private final Point point;

  LocatedPixel(Color pixel, Point point) {
    this.pixel = pixel;
    this.point = point;
  }

  Color getPixel() {
    return pixel;
  }

  Point getPoint() {
    return point;
  }
}
