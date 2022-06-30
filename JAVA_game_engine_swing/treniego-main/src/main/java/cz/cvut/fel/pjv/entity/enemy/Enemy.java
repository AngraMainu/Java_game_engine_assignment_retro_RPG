package cz.cvut.fel.pjv.entity.enemy;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.entity.AI;
import cz.cvut.fel.pjv.entity.Entity;

import java.awt.*;
import java.util.logging.Logger;

/**
 * Enemy class that holds specific update and draw methods.
 * */
public class Enemy extends Entity {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public AI ai = new AI();
    public int atk;

    public Enemy(PlayPanel pp){
        super(pp);

        name = "Slime";
        speed = 1;
        maxHP = 12;
        curHP = maxHP;
        atk = 1;

        direction = "down";

        solidArea = new Rectangle(3, 18, 42, 30);
        solidAreaDefaultX  = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        walkAnim[0]=setup("/Enemy/Slime/greenslime_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[1]=setup("/Enemy/Slime/greenslime_down_2.png", pp.tileSize, pp.tileSize);
        walkAnim[2]=setup("/Enemy/Slime/greenslime_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[3]=setup("/Enemy/Slime/greenslime_down_2.png", pp.tileSize, pp.tileSize);
        walkAnim[4]=setup("/Enemy/Slime/greenslime_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[5]=setup("/Enemy/Slime/greenslime_down_2.png", pp.tileSize, pp.tileSize);
        walkAnim[6]=setup("/Enemy/Slime/greenslime_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[7]=setup("/Enemy/Slime/greenslime_down_2.png", pp.tileSize, pp.tileSize);
    }

    /**
     * Has collision on tiles and objects.
     * */
    public void update(){
        ai.randomWalk(this);

        collisionOn = false;

        //Has collision on: Tiles, objects, player
        pp.collision.checkTiles(this);
        pp.collision.checkObject(this);
        //Check if hit player
        if (pp.collision.checkSingleEntityCollision(this, pp.player)) {
            actionHandler.playerGotHit(this);
        }

        if (invincible) {
            if (invincibleCounter++ > 30) {
                invincibleCounter = 0;
                invincible = false;
            }
        }

        //Can't move when collision or invincible
        if (!collisionOn && !invincible) {
            actionHandler.walk(direction, this);
            changeSpriteEveryNFrames(12);
        }
    }

    /**
     * If invincibility frames are on, draws sprite with transparency parameter.
     * */
    public void drawEntity (Graphics2D g2) {
        if(ifOnScreen(pp)){
            int screenX = worldX - pp.player.worldX + pp.player.screenX;
            int screenY = worldY - pp.player.worldY + pp.player.screenY;

            //Monster health display
            g2.setColor(Color.darkGray);
            g2.fillRect(screenX-1, screenY-1, pp.tileSize+2, 10+2);
            g2.setColor(Color.red);
            g2.fillRect(screenX, screenY, (pp.tileSize/maxHP)*curHP, 10);

            if(invincible) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            g2.drawImage(actionHandler.getNextWalkAnimImage(this.walkAnim, spriteNum, direction), screenX, screenY, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

}
