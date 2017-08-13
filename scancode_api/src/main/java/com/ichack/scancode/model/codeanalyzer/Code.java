package com.ichack.scancode.model.codeanalyzer;

import com.ichack.scancode.model.corners.PointDouble;
import com.ichack.scancode.model.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Code {

  private long code;

  static final int DOT_SIZE = 32;
  static final int MAX_SIZE = 512;

  private Color getAveragePixel(Image img, PointDouble[] corners) {
    return img.getPixel(corners[0].getY().intValue(), corners[0].getX().intValue());
  }

  private long generateCode(List<LocatedPixel> pixels) {
    int red[][] = new int[MAX_SIZE+10][MAX_SIZE+10];
    int green[][] = new int[MAX_SIZE+10][MAX_SIZE+10];
    int blue[][] = new int[MAX_SIZE+10][MAX_SIZE+10];
    int count[][] = new int[MAX_SIZE+10][MAX_SIZE+10];
    for (int i = 0; i < pixels.size(); i++) {
      LocatedPixel p = pixels.get(i);
      double x = p.getPoint().getX();
      double y= p.getPoint().getY();
      int pozX = (int)x;
      int pozY = (int)(MAX_SIZE - y);

      red[pozX][pozY] +=p.getPixel().getRed();
      green[pozX][pozY] +=p.getPixel().getGreen();
      blue[pozX][pozY] +=p.getPixel().getBlue();
      count[pozX][pozY] ++;
    }
    int[][] nrBlack = new int[MAX_SIZE/DOT_SIZE + 1][MAX_SIZE/DOT_SIZE + 1];
    int[][] nrTotal = new int[MAX_SIZE/DOT_SIZE + 1][MAX_SIZE/DOT_SIZE + 1];
    for (int j = 0; j < MAX_SIZE; j++) {
      for (int i = 0; i < MAX_SIZE; i++) {
        if(count[i][j] > 0) {
          red[i][j] /= count[i][j];
          green[i][j] /= count[i][j];
          blue[i][j] /= count[i][j];
          if (red[i][j] < 60 && green[i][j] < 60 && blue[i][j] < 60) {
            ++nrBlack[i / DOT_SIZE][j / DOT_SIZE];
          }
          nrTotal[i/DOT_SIZE][j/DOT_SIZE] ++;
        }
      }
    }
    for (int j = 0; j < MAX_SIZE; j++) {
      for (int i = 0; i < MAX_SIZE; i++) {
        if(i%32 == 0 || j % 32 == 0){
          continue;
        }
      }
    }
    long code = 0;
    int k = 0;
    for (int i = 1; i < MAX_SIZE/DOT_SIZE/2; i++) {
      for(int j = 1; j < MAX_SIZE/DOT_SIZE/2; j++) {
        if(i < MAX_SIZE/DOT_SIZE/4 || j < MAX_SIZE/DOT_SIZE/4) {
          ++k;
          code *= 2;
          if(nrBlack[i][j] > nrTotal[i][j]/4) {
            code += 1;
          }
        }
      }
    }
    return code;
  }

  private List<LocatedPixel> scaleImage(Image img, PointDouble[] corners, PointDouble[] realCorners) {

    List<LocatedPixel> pixels = new ArrayList<LocatedPixel>();

    if (realCorners[1].getX().intValue() == realCorners[0].getX().intValue() ||
        realCorners[1].getY().intValue() == realCorners[0].getY().intValue())  {
      pixels.add(new LocatedPixel(getAveragePixel(img,corners),realCorners[1]));
    } else {
      Line d1 = new Line(corners[0], corners[3]);
      Line d2 = new Line(corners[1], corners[2]);
      PointDouble center = d1.intersect(d2);
      Line l12 = new Line(corners[0], corners[1]);
      Line l24 = new Line(corners[1], corners[3]);
      Line l43 = new Line(corners[3], corners[2]);
      Line l31 = new Line(corners[2], corners[0]);
      PointDouble mid12 = l12.getMiddle(l31.length()/(l24.length() + l31.length()));
      PointDouble mid24 = l24.getMiddle(l12.length()/(l43.length() + l12.length()));
      PointDouble mid43 = l43.getMiddle(l24.length()/(l31.length() + l24.length()));;
      PointDouble mid31 = l31.getMiddle(l43.length()/(l12.length() + l43.length()));
      PointDouble[] corners1 = new PointDouble[]{corners[0], mid12, mid31, center};
      PointDouble[] corners2 = new PointDouble[]{mid12, corners[1], center, mid24};
      PointDouble[] corners3 = new PointDouble[]{mid31, center, corners[2], mid43};
      PointDouble[] corners4 = new PointDouble[]{center, mid24, mid43, corners[3]};
      int midX = (realCorners[1].getX().intValue() + realCorners[0].getX().intValue())/2 + 1;
      int midY = (realCorners[1].getY().intValue() + realCorners[0].getY().intValue())/2 + 1;

      PointDouble[] realCorners1 = new PointDouble[] {
        new PointDouble(realCorners[0].getX(), realCorners[0].getY()),
        new PointDouble(midX - 1,midY - 1)
      };
      PointDouble[] realCorners2 = new PointDouble[] {
              new PointDouble(midX , realCorners[0].getY()),
              new PointDouble(realCorners[1].getX(), midY - 1)
      };
      PointDouble[] realCorners3 = new PointDouble[] {
        new PointDouble(realCorners[0].getX(), midY ),
        new PointDouble(midX - 1, realCorners[1].getY())
      };
      PointDouble[] realCorners4 = new PointDouble[] {
        new PointDouble(midX, midY ),
        new PointDouble(realCorners[1].getX(),realCorners[1].getY())
      };
      pixels.addAll(scaleImage(img, corners1, realCorners1));
      pixels.addAll(scaleImage(img, corners2, realCorners2));
      pixels.addAll(scaleImage(img, corners3, realCorners3));
      pixels.addAll(scaleImage(img, corners4, realCorners4));
    }
    return pixels;
  }

  public Code(Image img, PointDouble[] corners) {
    code = generateCode(scaleImage(img, corners, new PointDouble[] {
        new PointDouble(0,0),
        new PointDouble(MAX_SIZE,MAX_SIZE)}));
  }

  public long getCode() {
    return code;
  }
}
