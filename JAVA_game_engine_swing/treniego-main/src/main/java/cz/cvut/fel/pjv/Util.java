package cz.cvut.fel.pjv;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class Util {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );


    /**
     * Rescales booted image to a given width and height.
     * */
    public BufferedImage scaleImage(BufferedImage orig, int w, int h){
        if(w < 0 || h < 0) {
            LOGGER.severe("Width and height must be > 0!");
        }
        BufferedImage scaledImage = new BufferedImage(w, h, orig.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(orig, 0, 0, w, h, null);
        g2.dispose();

        return scaledImage;
    }


    /**
     * Returns half the size of a given text string.
     * */
    public int getTextSize(String string, Graphics2D g2){
        return (int)(g2.getFontMetrics().getStringBounds(string, g2).getWidth()/2);
    }

    /**
     * Returns given integer minus lenght of a given text string
     * */
    public int getTextEnd(Graphics2D g2, String text, int tailX){
        return tailX - (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();

    }

}
