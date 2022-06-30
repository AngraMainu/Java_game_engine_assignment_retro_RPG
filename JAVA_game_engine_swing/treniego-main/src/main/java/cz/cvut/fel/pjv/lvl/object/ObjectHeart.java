package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class ObjectHeart extends Object{

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public ObjectHeart(PlayPanel pp) {
        super(pp);
        name = "Heart";
        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/heart_blank.png")));
            image = util.scaleImage(image, pp.tileSize, pp.tileSize);
            image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/heart_half.png")));
            image2 = util.scaleImage(image2, pp.tileSize, pp.tileSize);
            image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/heart_full.png")));
            image3 = util.scaleImage(image3, pp.tileSize, pp.tileSize);
        } catch (IOException e){
            LOGGER.severe("Couldn't find path of the image!");
        }
    }
}