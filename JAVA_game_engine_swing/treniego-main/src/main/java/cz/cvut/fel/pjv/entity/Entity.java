package cz.cvut.fel.pjv.entity;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.Util;
import cz.cvut.fel.pjv.action.ActionHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * Super class of all being classes: player, old man npc and slime enemy.
 * */
public class Entity{

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public PlayPanel pp;

    //Personal stats
    public String name;
    public String[] dialogueString;
    public int curDialogue;
    public int speed;
    public int worldX, worldY;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int maxHP;
    public int curHP;
    public int atk;
    public String direction;

    //System stats

    public Rectangle solidArea = new Rectangle(0,0,0,0);
    public Rectangle interactableArea = new Rectangle(0,0,0,0);
    public Rectangle attackArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public ActionHandler actionHandler;
    public BufferedImage[] walkAnim = new BufferedImage[8];
    public BufferedImage[] attackAnim = new BufferedImage[8];
    public boolean invincible;
    public int invincibleCounter = 0;
    public boolean interactable = false;
    public boolean collisionOn = false;


    public Entity(PlayPanel pp){
        if(pp == null){
            LOGGER.severe("Couldn't pass PLay Panel!");
        }
        this.pp = pp;
        this.actionHandler = pp.actionHandler;
    }

    /**
     * Sets given coordinates to an entity.
     * */
    public void spawn(int worldX, int worldY){
        if(worldX > pp.maxWorldXSize || worldY > pp.maxWorldYSize){
            LOGGER.warning("Entity spawned outside bondarioes of the map.");
        }
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * Sets entity value to null.
     * */
    public void kill(String type, int id) {
        if(type.contentEquals("enemy")){
            pp.enemy[id] = null;
        } else if (type.contentEquals("npc")){
            pp.NPC[id] = null;
        }
    }

    public void update(){
    }

    /**
     * Sets up an image of an entity from a given path.
     * Scales it using util function for modern big screens.
     * */
    public BufferedImage setup(String imagePath, int w, int h) {

        Util util = new Util();
        BufferedImage image = null;

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            image = util.scaleImage(image, w, h);
        } catch (IOException e) {
            LOGGER.severe("Couldn't find path for the image!");
        }
        return image;
    }


    /**
     * Basic draw method for all entities.
     * Draws with transparency parameter if got hit, normally otherwise.
     * */
    public void drawEntity (Graphics2D g2) {
        int screenX = worldX - pp.player.worldX + pp.player.screenX;
        int screenY = worldY - pp.player.worldY + pp.player.screenY;
        if(ifOnScreen(pp)){
            if(this.invincible){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.drawImage(actionHandler.getNextWalkAnimImage(this.walkAnim, spriteNum, direction), screenX, screenY, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }else {
                g2.drawImage(actionHandler.getNextWalkAnimImage(this.walkAnim, spriteNum, direction), screenX, screenY, null);
            }
        }
    }

    /**
     * Returns true if entity's coordinates lie within player's screen.
     * */
    public boolean ifOnScreen(PlayPanel pp) {
        return (worldX + pp.tileSize > pp.player.worldX - pp.player.screenX &&
                worldX - pp.tileSize < pp.player.worldX + pp.player.screenX &&
                worldY + pp.tileSize > pp.player.worldY - pp.player.screenY &&
                worldY - pp.tileSize < pp.player.worldY + pp.player.screenY);
    }

    /**
     * Increases frame counter every given amount of frames.
     * */
    public void changeSpriteEveryNFrames(int n){

        if (spriteCounter++ > n) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

}

