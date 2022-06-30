package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;

import java.util.logging.Logger;

public class ObjectSword extends Object{
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public ObjectSword(PlayPanel pp) {
        super(pp);
        name = "Worn Sword";
        image = setup("/Objects/Objects prototype/sword_normal.png", pp.tileSize, pp.tileSize);
        atkVal = 1;
    }
}
