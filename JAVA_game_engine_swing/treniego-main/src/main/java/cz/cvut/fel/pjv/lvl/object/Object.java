package cz.cvut.fel.pjv.lvl.object;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

//Super class of objects
public class Object{

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    //System parameters
    public String name = "";
    public String description = "";
    public BufferedImage image, image2, image3;
    public boolean collision = false;
    public boolean interactable = false;
    public final Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    Util util = new Util();
    PlayPanel pp;

    //Object parameters
    public int atkVal;
    public int defVal;
    public int worldX = 0, worldY = 0;

    public Object(PlayPanel pp){
        if(pp == null){
            LOGGER.severe("Coulnd't pass Play Panel!");
        }
        this.pp = pp;
    }

    public void drawObj(Graphics2D g2, PlayPanel pp){
        int screenX = worldX - pp.player.worldX + pp.player.screenX;
        int screenY = worldY - pp.player.worldY + pp.player.screenY;

        // Draw if on the screen + 1 tile for better performance
        if (ifOnScreen(pp)){
            g2.drawImage(image, screenX, screenY, null);
        }
    }

    public void spawn(int x, int y){
        this.worldX = x;
        this.worldY = y;
    }

    public boolean ifOnScreen(PlayPanel pp) {
        return (worldX + pp.tileSize > pp.player.worldX - pp.player.screenX &&
                worldX - pp.tileSize < pp.player.worldX + pp.player.screenX &&
                worldY + pp.tileSize > pp.player.worldY - pp.player.screenY &&
                worldY - pp.tileSize < pp.player.worldY + pp.player.screenY);
    }

    public String getName(){
        return this.name;
    }

    public void setSprite(BufferedImage sprite){
        this.image = sprite;
    }

    public void setCollision(boolean collision){
        this.collision = collision;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public BufferedImage setup(String imagePath, int w, int h) {

        Util util = new Util();
        BufferedImage image = null;

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            image = util.scaleImage(image, w, h);
        } catch (IOException e) {
            LOGGER.severe("Couldn't find path of the image!");
        }
        return image;
    }
}
