package hello.codeanalyzer;

import java.awt.Color;
import hello.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 04/02/17.
 */
public class Code {

  long code;

  final static double epsilon = 0.001;
  final static int dotSize = 32;
  final static int maxSize = 512;

  private Color getAveragePixel(Image img, Point[] corners) {
  //  Line d1 = new Line(corners[0], corners[3]);
    //Line d2 = new Line(corners[1], corners[2]);
    //Point center = d1.intersect(d2);
    //System.out.println("\n" + corners[0].getX() + " " + corners[0].getY());
    //System.out.println(center.getX() + " " + center.getY());
    //return img.getPixel(761 - (int)corners[0].getY() + 250, (int)corners[0].getX());
    return img.getPixel((int)corners[0].getY(), (int) corners[0].getX());

  }

  private long generateCode(List<LocatedPixel> pixels) {
    System.out.println("generating code");
    int red[][] = new int[maxSize+10][maxSize+10];
    int green[][] = new int[maxSize+10][maxSize+10];
    int blue[][] = new int[maxSize+10][maxSize+10];
    int count[][] = new int[maxSize+10][maxSize+10];
    for (int i = 0; i < pixels.size(); i++) {
      LocatedPixel p = pixels.get(i);
      double x = p.getPoint().getX();
      double y= p.getPoint().getY();
      int pozX = (int)x;
      int pozY = (int)(maxSize - y);
     // System.out.println(pozX + " " + pozY);

      red[pozX][pozY] +=p.getPixel().getRed();
      green[pozX][pozY] +=p.getPixel().getGreen();
      blue[pozX][pozY] +=p.getPixel().getBlue();
      count[pozX][pozY] ++;
    }
    int[][] nrBlack = new int[maxSize/dotSize + 1][maxSize/dotSize + 1];
    int[][] nrTotal = new int[maxSize/dotSize + 1][maxSize/dotSize + 1];
    for (int j = 0; j < maxSize; j++) {
      for (int i = 0; i < maxSize; i++) {
        //System.out.print(count[i][j] + " ");
        if(count[i][j] > 0) {
          red[i][j] /= count[i][j];
          green[i][j] /= count[i][j];
          blue[i][j] /= count[i][j];
          if (red[i][j] < 60 && green[i][j] < 60 && blue[i][j] < 60) {
            ++nrBlack[i / dotSize][j / dotSize];
          }
          nrTotal[i/dotSize][j/dotSize] ++;
        } else {
      //     System.out.println(i + " " + j);
        }
      }
     // System.out.println();
    }
    for (int j = 0; j < maxSize; j++) {
      for (int i = 0; i < maxSize; i++) {
        if(i%32 == 0 || j % 32 == 0){
          System.out.print(2);
          continue;
        }
        if (red[i][j] < 60 && green[i][j] < 60 && blue[i][j] < 60) {
          System.out.print(0);
        } else {
          System.out.print(1);
        }
      }
      System.out.println();
    }
    long code = 0;
    System.out.println(maxSize/dotSize);
    int k = 0;
    for (int i = 1; i < maxSize/dotSize/2; i++) {
      for(int j = 1; j < maxSize/dotSize/2; j++) {
        if(i < maxSize/dotSize/4 || j < maxSize/dotSize/4) {
          ++k;
          //System.out.println(k);
          code *= 2;
          //System.out.print((double)nrBlack[i][j]/(double)nrTotal[i][j] + " ");
          if(nrBlack[i][j] > nrTotal[i][j]/4) {
            code += 1;
          }
          System.out.println(i + " " + j);
        }
      }
     // System.out.println();
    }
    System.out.println(pixels.size());
    return code;
  }

  private List<LocatedPixel> scaleImage(Image img, Point[] corners, Point[] realCorners) {

    List<LocatedPixel> pixels = new ArrayList<LocatedPixel>();

    if ((int)realCorners[1].getX() == (int)realCorners[0].getX() || (int)realCorners[1].getY() == (int)realCorners[0].getY())  {
      pixels.add(new LocatedPixel(getAveragePixel(img,corners),realCorners[1]));
      /*if(realCorners[1].getX() < 256 && realCorners[1].getY() < 256 && (realCorners[1].getX() - corners[0].getX() > 4 || realCorners[1].getY() - corners[0].getY() > 4)) {
        System.out.println(realCorners[1].getX() + " " + realCorners[1].getY());
        System.out.print((corners[0].getX() - 250) + " " + (corners[0].getY() - 320) + " " + (corners[1].getX() - 250) + " " + (corners[1].getY() - 320)+ " ");
        System.out.print((corners[2].getX() - 250) + " " + (corners[2].getY() - 320) + " " + (corners[3].getX() - 250) + " " + (corners[3].getY() - 320) + "\n\n\n\n");
      }
      if(realCorners[1].getX() == 158 && realCorners[1].getY() == 42 ) {
        System.out.println(realCorners[1].getX() + " " + realCorners[1].getY());
        System.out.println(corners[0].getX() + " " + (int)corners[0].getY() );
      }*/
    } else {
      Line d1 = new Line(corners[0], corners[3]);
      Line d2 = new Line(corners[1], corners[2]);
      Point center = d1.intersect(d2);
      if(realCorners[0].getX() == 0 && realCorners[0].getY() == 0 && realCorners[1].getX() == 512 && realCorners[1].getY() == 512) {
        System.out.println(center.getX() + " " + center.getY());
      }
     // System.out.print(corners[0].getX() + " " + corners[0].getY() + " " + corners[1].getX() + " " + corners[1].getY() + " " );
      //System.out.print(corners[2].getX() + " " + corners[2].getY() + " " + corners[3].getX() + " " + corners[3].getY() + "\n\n\n\n" );
      Line l12 = new Line(corners[0], corners[1]);
      Line l24 = new Line(corners[1], corners[3]);
      Line l43 = new Line(corners[3], corners[2]);
      Line l31 = new Line(corners[2], corners[0]);
      Point mid12 = l12.getMiddle(l31.length()/(l24.length() + l31.length()));
      Point mid24 = l24.getMiddle(l12.length()/(l43.length() + l12.length()));
      Point mid43 = l43.getMiddle(l24.length()/(l31.length() + l24.length()));;
      Point mid31 = l31.getMiddle(l43.length()/(l12.length() + l43.length()));
      Point[] corners1 = new Point[]{corners[0], mid12, mid31, center};
      Point[] corners2 = new Point[]{mid12, corners[1], center, mid24};
      Point[] corners3 = new Point[]{mid31, center, corners[2], mid43};
      Point[] corners4 = new Point[]{center, mid24, mid43, corners[3]};
      if(realCorners[0].getX() == 0 && realCorners[0].getY() == 0 && realCorners[1].getX() == 512 && realCorners[1].getY() == 512) {
        System.out.println("*****");
        System.out.println(center.getX() + " " + center.getY());
        System.out.println(mid12.getX() + " " + mid12.getY());
        System.out.println(mid43.getX() + " " + mid43.getY());
      }
      int midX = ((int)realCorners[1].getX() + (int)realCorners[0].getX())/2 + 1;
      int midY = ((int)realCorners[1].getY() + (int)realCorners[0].getY())/2 + 1;
     // System.out.println(midX + " " + midY);

      Point[] realCorners1 = new Point[] {
        new Point(realCorners[0].getX(), realCorners[0].getY()),
        new Point(midX - 1,midY - 1)
      };
      Point[] realCorners2 = new Point[] {
              new Point(midX , realCorners[0].getY()),
              new Point(realCorners[1].getX(), midY - 1)
      };
      Point[] realCorners3 = new Point[] {
        new Point(realCorners[0].getX(), midY ),
        new Point(midX - 1, realCorners[1].getY())
      };
      Point[] realCorners4 = new Point[] {
        new Point(midX, midY ),
        new Point(realCorners[1].getX(),realCorners[1].getY())
      };
      pixels.addAll(scaleImage(img, corners1, realCorners1));
      pixels.addAll(scaleImage(img, corners2, realCorners2));
      pixels.addAll(scaleImage(img, corners3, realCorners3));
      pixels.addAll(scaleImage(img, corners4, realCorners4));
    }
    return pixels;
  }

  public Code(Image img, Point[] corners) {
    code = generateCode(scaleImage(img, corners, new Point[] { new Point(0,0), new Point(maxSize,maxSize)}));
  }

  public long getCode() {
    return code;
  }
}
