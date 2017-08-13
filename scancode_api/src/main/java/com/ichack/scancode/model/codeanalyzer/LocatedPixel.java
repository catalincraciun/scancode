package com.ichack.scancode.model.codeanalyzer;

import java.awt.Color;
import com.ichack.scancode.model.corners.PointDouble;

/**
 * A LocatedPixel is composite of a Color and a Point with double coordinates
 */
class LocatedPixel {

  private final Color pixel;
  private final PointDouble point;

  LocatedPixel(Color pixel, PointDouble point) {
    this.pixel = pixel;
    this.point = point;
  }

  Color getPixel() {
    return pixel;
  }

  PointDouble getPoint() {
    return point;
  }
}
