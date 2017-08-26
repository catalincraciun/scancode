package com.ichack.scancode.model.scanning;

import com.ichack.scancode.model.geometry.Line;
import com.ichack.scancode.model.geometry.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Code {

  private static final int DOT_SIZE = 32;
  private static final int MAX_SIZE = 512;
  private long code;

  public Code(Image img, List<Point<Double>> corners) {
    code = generateCode(scaleImage(img, corners, new ArrayList<Point<Double>>() {{
        add(new Point<>(0.0, 0.0));
        add(new Point<>((double) MAX_SIZE, (double) MAX_SIZE));
    }}));
  }

  private Color getAveragePixel(Image img, List<Point<Double>> corners) {
    return img.getPixel(corners.get(0).getY().intValue(), corners.get(0).getX().intValue());
  }

  private long generateCode(List<LocatedPixel> pixels) {
    int red[][] = new int[MAX_SIZE + 10][MAX_SIZE + 10];
    int green[][] = new int[MAX_SIZE + 10][MAX_SIZE + 10];
    int blue[][] = new int[MAX_SIZE + 10][MAX_SIZE + 10];
    int count[][] = new int[MAX_SIZE + 10][MAX_SIZE + 10];
    for (int i = 0; i < pixels.size(); i++) {
      LocatedPixel p = pixels.get(i);
      double x = p.getPoint().getX();
      double y = p.getPoint().getY();
      int pozX = (int) x;
      int pozY = (int) (MAX_SIZE - y);

      red[pozX][pozY] += p.getPixel().getRed();
      green[pozX][pozY] += p.getPixel().getGreen();
      blue[pozX][pozY] += p.getPixel().getBlue();
      count[pozX][pozY]++;
    }
    int[][] nrBlack = new int[MAX_SIZE / DOT_SIZE + 1][MAX_SIZE / DOT_SIZE + 1];
    int[][] nrTotal = new int[MAX_SIZE / DOT_SIZE + 1][MAX_SIZE / DOT_SIZE + 1];
    for (int j = 0; j < MAX_SIZE; j++) {
      for (int i = 0; i < MAX_SIZE; i++) {
        if (count[i][j] > 0) {
          red[i][j] /= count[i][j];
          green[i][j] /= count[i][j];
          blue[i][j] /= count[i][j];
          if (red[i][j] < 60 && green[i][j] < 60 && blue[i][j] < 60) {
            ++nrBlack[i / DOT_SIZE][j / DOT_SIZE];
          }
          nrTotal[i / DOT_SIZE][j / DOT_SIZE]++;
        }
      }
    }
    for (int j = 0; j < MAX_SIZE; j++) {
      for (int i = 0; i < MAX_SIZE; i++) {
        if (i % 32 == 0 || j % 32 == 0) {
          continue;
        }
      }
    }
    long code = 0;
    int k = 0;
    for (int i = 1; i < MAX_SIZE / DOT_SIZE / 2; i++) {
      for (int j = 1; j < MAX_SIZE / DOT_SIZE / 2; j++) {
        if (i < MAX_SIZE / DOT_SIZE / 4 || j < MAX_SIZE / DOT_SIZE / 4) {
          ++k;
          code *= 2;
          if (nrBlack[i][j] > nrTotal[i][j] / 4) {
            code += 1;
          }
        }
      }
    }
    return code;
  }

  private List<LocatedPixel> scaleImage(Image img, List<Point<Double>> corners,
      List<Point<Double>> realCorners) {

    List<LocatedPixel> pixels = new ArrayList<>();

    if (realCorners.get(1).getX().intValue() == realCorners.get(0).getX().intValue() ||
        realCorners.get(1).getY().intValue() == realCorners.get(0).getY().intValue()) {
      pixels.add(new LocatedPixel(getAveragePixel(img, corners), realCorners.get(1)));
    } else {
      Line d1 = new Line(corners.get(0), corners.get(3));
      Line d2 = new Line(corners.get(1), corners.get(2));
      Point<Double> center = d1.getIntersection(d2);
      Line l12 = new Line(corners.get(0), corners.get(1));
      Line l24 = new Line(corners.get(1), corners.get(3));
      Line l43 = new Line(corners.get(3), corners.get(2));
      Line l31 = new Line(corners.get(2), corners.get(0));

      Point<Double> mid12 = l12.getTranslation(l31.getLength() / (l24.getLength() + l31.getLength()));
      Point<Double> mid24 = l24.getTranslation(l12.getLength() / (l43.getLength() + l12.getLength()));
      Point<Double> mid43 = l43.getTranslation(l24.getLength() / (l31.getLength() + l24.getLength()));
      Point<Double> mid31 = l31.getTranslation(l43.getLength() / (l12.getLength() + l43.getLength()));

      List<Point<Double>> corners1 = new ArrayList<Point<Double>>() {{
        add(corners.get(0));
        add(mid12);
        add(mid31);
        add(center);
      }};

      List<Point<Double>> corners2 = new ArrayList<Point<Double>>() {{
        add(mid12);
        add(corners.get(1));
        add(center);
        add(mid24);
      }};

      List<Point<Double>> corners3 = new ArrayList<Point<Double>>() {{
        add(mid31);
        add(center);
        add(corners.get(2));
        add(mid43);
      }};

      List<Point<Double>> corners4 = new ArrayList<Point<Double>>() {{
        add(center);
        add(mid24);
        add(mid43);
        add(corners.get(3));
      }};

      int midX = (realCorners.get(1).getX().intValue() + realCorners.get(0).getX().intValue()) / 2 + 1;
      int midY = (realCorners.get(1).getY().intValue() + realCorners.get(0).getY().intValue()) / 2 + 1;

      List<Point<Double>> realCorners1 = new ArrayList<Point<Double>>() {{
          add(new Point<>(realCorners.get(0).getX(), realCorners.get(0).getY()));
          add(new Point<>((double) midX - 1, (double) midY - 1));
      }};
      List<Point<Double>> realCorners2 = new ArrayList<Point<Double>>() {{
          add(new Point<>((double) midX, realCorners.get(0).getY()));
          add(new Point<>(realCorners.get(1).getX(), (double) midY - 1));
      }};
      List<Point<Double>> realCorners3 = new ArrayList<Point<Double>>() {{
          add(new Point<>(realCorners.get(0).getX(), (double) midY));
          add(new Point<>((double) midX - 1, realCorners.get(1).getY()));
      }};
      List<Point<Double>> realCorners4 = new ArrayList<Point<Double>>() {{
          add(new Point<>((double) midX, (double) midY));
          add(new Point<>(realCorners.get(1).getX(), realCorners.get(1).getY()));
      }};

      pixels.addAll(scaleImage(img, corners1, realCorners1));
      pixels.addAll(scaleImage(img, corners2, realCorners2));
      pixels.addAll(scaleImage(img, corners3, realCorners3));
      pixels.addAll(scaleImage(img, corners4, realCorners4));
    }
    return pixels;
  }

  public long getCode() {
    return code;
  }
}
