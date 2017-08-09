package com.ichack.scancode.model;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BaseImage {


    private BufferedImage image;

    public BaseImage(String logoLocation, Color color) throws IOException {

        image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i <= 511; i++) {
            for (int j = 0; j<= 511; j++) {
                if (i < 10 || j < 10 || i > 502 || j > 502){
                    image.setRGB(i,j,new Color(0,0,0).getRGB());
                } else {
                    image.setRGB(i,j, color.getRGB());
                }
            }
        }

        BufferedImage logo = ImageIO.read(new File(logoLocation));

        int maxSize = Math.max(logo.getHeight(), logo.getWidth());

        BufferedImage squareLogo = new BufferedImage(maxSize, maxSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < squareLogo.getWidth(); i ++) {
            for (int j = 0; j < squareLogo.getHeight(); j ++) {
                squareLogo.setRGB(i, j, color.getRGB());
            }
        }

        for (int i = maxSize / 2 - logo.getWidth() / 2; i < maxSize / 2 + logo.getWidth() / 2; i ++) {
            for (int j = maxSize / 2 - logo.getHeight() / 2; j < maxSize / 2 + logo.getHeight() / 2; j ++) {
                /*if ((logo.getRGB(i - (maxSize / 2 - logo.getWidth() / 2),
                        j - (maxSize / 2 - logo.getHeight() / 2)) >> 24) == 0x00) {
                    squareLogo.setRGB(i, j, color.getRGB());
                } else {*/
                    squareLogo.setRGB(i, j, logo.getRGB(i - (maxSize / 2 - logo.getWidth() / 2),
                            j - (maxSize / 2 - logo.getHeight() / 2)));
                //}
            }
        }

        if(squareLogo.getWidth() > 255 || squareLogo.getHeight() > 255) {
            squareLogo = createResizedCopy(squareLogo, 255,255, true);
        }

        for (int i = 255 - squareLogo.getWidth() / 2; i < 255 + squareLogo.getWidth() / 2; i++) {
            for (int j = 255 - squareLogo.getHeight() / 2; j < 255 + squareLogo.getHeight() / 2; j++) {
                image.setRGB(i, j, squareLogo.getRGB(i - 255 + squareLogo.getWidth()/2, j - 255 + squareLogo.getHeight() / 2));
            }
        }


    }

    BufferedImage createResizedCopy(BufferedImage logo,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(logo, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public BufferedImage getImage() {
        return image;
    }
}
