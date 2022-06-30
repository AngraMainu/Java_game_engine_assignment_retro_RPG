package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class ObjectDoor extends Object {
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public ObjectDoor(PlayPanel pp) {
        super(pp);
        name = "Door";
        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/door.png")));
            image = util.scaleImage(image, pp.tileSize, pp.tileSize);
        } catch (IOException e){
            LOGGER.severe("Couldn't find path of the image!");
        }
        collision = true;
        this.description = "What lies ahead?...";
    }
}
