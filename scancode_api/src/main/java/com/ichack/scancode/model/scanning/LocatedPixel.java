package com.ichack.scancode.model.scanning;

import java.awt.Color;
import com.ichack.scancode.model.geometry.Point;

/**
 * A LocatedPixel is a pixel made of a Color and a Point with double coordinates
 */
class LocatedPixel {

  private final Color pixel;
  private final Point<Double> point;

  LocatedPixel(Color pixel, Point<Double> point) {
    this.pixel = pixel;
    this.point = point;
  }

  Color getPixel() {
    return pixel;
  }

  Point<Double> getPoint() {
    return point;
  }
}
