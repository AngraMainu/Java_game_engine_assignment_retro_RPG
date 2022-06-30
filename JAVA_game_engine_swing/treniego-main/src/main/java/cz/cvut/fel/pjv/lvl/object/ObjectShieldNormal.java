package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;

import java.util.logging.Logger;

public class ObjectShieldNormal extends Object{
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public ObjectShieldNormal(PlayPanel pp) {
        super(pp);
        name = "Wooden Shield";
        image = setup("/Objects/Objects prototype/shield_wood.png", pp.tileSize, pp.tileSize);
        defVal = 1;
    }
}
