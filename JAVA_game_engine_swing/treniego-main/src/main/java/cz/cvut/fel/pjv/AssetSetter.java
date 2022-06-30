package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.entity.NPC.OldMan;
import cz.cvut.fel.pjv.entity.enemy.Enemy;
import cz.cvut.fel.pjv.lvl.object.ObjectBoots;
import cz.cvut.fel.pjv.lvl.object.ObjectChest;
import cz.cvut.fel.pjv.lvl.object.ObjectDoor;
import cz.cvut.fel.pjv.lvl.object.ObjectKey;

import java.util.logging.Logger;

/**
 * A class that takes all entities and objects
 * and spawns them on the map*/
public class AssetSetter {

    PlayPanel pp;

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public AssetSetter(PlayPanel pp) {
        if(pp == null){
            LOGGER.severe("Couldn't pass PLay Panel!");
        }
        this.pp = pp;
    }

    public void setObjects() {
        pp.obj[0] = new ObjectBoots(pp);
        pp.obj[0].spawn(pp.tileSize*38, pp.tileSize*42);

        pp.obj[1] = new ObjectKey(pp);
        pp.obj[1].spawn(pp.tileSize*23, pp.tileSize*42);

        pp.obj[2] = new ObjectKey(pp);
        pp.obj[2].spawn(pp.tileSize*37, pp.tileSize*9);

        pp.obj[3] = new ObjectDoor(pp);
        pp.obj[3].spawn(pp.tileSize*10, pp.tileSize*11);

        pp.obj[4] = new ObjectChest(pp);
        pp.obj[4].spawn(pp.tileSize*10, pp.tileSize*7);

    }

    public void setNPCs() {
        pp.NPC[0] = new OldMan(pp);
        pp.NPC[0].spawn(pp.player.worldX-pp.tileSize*2,pp.player.worldY);

    }

    public void setEnemies() {
        pp.enemy[0] = new Enemy(pp);
        pp.enemy[0].spawn(pp.tileSize*23,pp.tileSize*40);
    }
}
