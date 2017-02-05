package hello.corners;

import java.util.ArrayList;
import java.util.List;


public class CornerAnalyzer {

    private static final Color borderColor = new Color(0, 0, 0);
    private static final Color cyan = new Color (0, 220, 220);
    private PictureUtils picture;
    private boolean[][] visited;
    private List<Integer> xl;
    private List<Integer> yl;
    private static final int blackSensitivity = 60;
    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;

    public CornerAnalyzer(PictureUtils picture) {
        xl = new ArrayList<>();
        yl = new ArrayList<>();
        this.picture = picture;
        visited = new boolean[picture.getWidth()][picture.getHeight()];
    }

    private void contour(){
        boolean quit = false;
        for(int x = 0; x < picture.getWidth(); x++){
            for(int y = 0; y < picture.getHeight(); y++){
                if(picture.getPixel(x, y).sameColor(borderColor, blackSensitivity)
                        && checkPixel(x, y) && !visited[x][y]){
                    helper(x, y);
                    visited[x][y] = true;
                    quit = true;
                    break;
                } else if(!picture.getPixel(x, y).sameColor(borderColor, blackSensitivity )){
                    visited[x][y] = true;
                }
            }
            if(quit) {
                break;
            }
        }
    }

    public void calculateCorners() {
        contour();
        check(xl, yl);
    }

    public Point getTopLeft() {
      return topLeft;
    }

    public Point getTopRight() {
      return topRight;
    }

    public Point getBottomLeft() {
      return bottomLeft;
    }

    public Point getBottomRight() {
      return bottomRight;
    }

    private boolean checkPixel(int x, int y){
        boolean success = false;
        for(int i = -5; i <= 5; i++){
            for(int j = -5; j <= 5; j++){
                if(PictureUtils.isInBounds(picture, x + i, y + j)){
                    if(picture.getPixel(x + i, y + j).sameColor(cyan, blackSensitivity )){
                        success = true;
                    }
                }
            }
        }

        return success;
    }

    private void helper(int x, int y){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (PictureUtils.isInBounds(picture, x + i, y + j) &&
                        borderColor.sameColor(picture.getPixel(x + i, y + j), blackSensitivity )) {
                    if(!visited[x + i][y + j]) {
                        visited[x + i][y + j] = true;
                        xl.add(x);
                        yl.add(y);
                        helper(x + i, y + j);
                    }
                }
            }
        }
    }


    private double dist(int x1, int y1, int x2, int y2) {
      return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
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
        if (x.get(i) < xm && y.get(i) < ym && dist < dist((int)xm, (int)ym, x.get(i), y.get(i))) {
          dist = dist((int)xm, (int)ym, x.get(i), y.get(i));
          bestX = x.get(i);
          bestY = y.get(i);
        }
      }

      topLeft = new Point(bestX, bestY);

      dist = 0;
      bestX = 0;
      bestY = 0;
      for (int i = 0; i < x.size(); i++) {
        if (x.get(i) > xm && y.get(i) < ym && dist < dist((int)xm, (int)ym, x.get(i), y.get(i))) {
          dist = dist((int)xm, (int)ym, x.get(i), y.get(i));
          bestX = x.get(i);
          bestY = y.get(i);
        }
      }

      topRight = new Point(bestX, bestY);

      dist = 0;
      bestX = 0;
      bestY = 0;
      for (int i = 0; i < x.size(); i++) {
        if (x.get(i) > xm && y.get(i) > ym && dist < dist((int)xm, (int)ym, x.get(i), y.get(i))) {
          dist = dist((int)xm, (int)ym, x.get(i), y.get(i));
          bestX = x.get(i);
          bestY = y.get(i);
        }
      }

      bottomRight = new Point(bestX, bestY);

      dist = 0;
      bestX = 0;
      bestY = 0;
      for (int i = 0; i < x.size(); i++) {
        if (x.get(i) < xm && y.get(i) > ym && dist < dist((int)xm, (int)ym, x.get(i), y.get(i))) {
          dist = dist((int)xm, (int)ym, x.get(i), y.get(i));
          bestX = x.get(i);
          bestY = y.get(i);
        }
      }

      bottomLeft = new Point(bestX, bestY);
    }
}
