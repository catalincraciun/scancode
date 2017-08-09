package com.ichack.scancode.model.codeanalyzer;

import java.awt.*;

/**
 * Created by paul on 04/02/17.
 */
public class LocatedPixel {
  Color pixel;
  Point point;

  public LocatedPixel(Color pixel, Point point) {
    this.pixel = pixel;
    this.point = point;
  }

  public Color getPixel() {
    return pixel;
  }

  public Point getPoint() {
    return point;
  }
}
