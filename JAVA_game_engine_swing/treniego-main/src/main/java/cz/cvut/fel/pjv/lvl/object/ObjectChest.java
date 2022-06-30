package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class ObjectChest extends Object{
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public ObjectChest(PlayPanel pp) {
        super(pp);
        name = "Chest";
        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/chest.png")));
            image = util.scaleImage(image, pp.tileSize, pp.tileSize);
        } catch (IOException e){
            LOGGER.severe("Couldn't find path of the image!");
        }
        collision = true;
    }
}
