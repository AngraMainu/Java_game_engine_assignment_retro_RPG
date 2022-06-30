package cz.cvut.fel.pjv.action;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.Util;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class that handles all the interactions between other classes
 * and provides abilities for entities.
 * */
public class ActionHandler {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    //Center of a tile as a trigger point for an event
    public final Rectangle triggerPoint = new Rectangle(23, 23, 2, 2);

    public PlayPanel pp;
    public Util util = new Util();

    //Event messages
    public final String damagePitMessage = "You sink into dirt!";
    public final String healingPoolMessage = "As you drink the shimmering water, your wounds heal.";


    public ActionHandler(PlayPanel pp) {
        if(pp == null){
            LOGGER.severe("Couldn't pass PLay Panel!");
        }
        this.pp = pp;
    }

    /**
     * Changes current entity coordinates based on
     * direction they're facing and their speed.
     * */
    public void walk(String input, Entity entity) {
        if (Objects.equals(input, "up")) {
            entity.worldY -= entity.speed;
        } else if (Objects.equals(input, "down")) {
            entity.worldY += entity.speed;
        } else if (Objects.equals(input, "left")) {
            entity.worldX -= entity.speed;
        } else if (Objects.equals(input, "right")) {
            entity.worldX += entity.speed;
        }
    }


    /**
     * Handles specific interactions with objects, upon pressing interact button
     * near an object.
     * */
    public void interactOBJ(int objID, Player player){
        if(objID != -1){
            if(pp.obj[objID].interactable && pp.obj[objID].name.contentEquals("Boots")) {
                if (player.inputHandler.interactInput) {
                    LOGGER.info("Picked up boots, speed increased.");
                    pp.playSE(2);
                    player.speed += 2;
                    pp.obj[objID] = null;
                }
            }else if(pp.obj[objID].interactable&& pp.obj[objID].name.contentEquals("Key")) {
                if(player.inputHandler.interactInput) {
                    LOGGER.info("Picked up a key.");
                    pp.playSE(1);
                    player.keys++;
                    pp.obj[objID] = null;
                }
            } else if(pp.obj[objID].interactable&& pp.obj[objID].name.contentEquals("Door")) {
                if(player.inputHandler.interactInput) {
                    LOGGER.info("Unlocked the door using a key.");
                    pp.playSE(3);
                    player.keys--;
                    pp.obj[objID] = null;
                }
            } else if (pp.obj[objID].interactable&& pp.obj[objID].name.contentEquals("Chest")) {
                if(player.inputHandler.interactInput) {
                    LOGGER.info("Unlocked the chest using a key.");
                    pp.playSE(4);
                    player.keys--;
                    pp.obj[objID] = null;
                }
            }
        }
    }

    /**
     * Makes NPC say next phrase
     * */
    public void interactNPC(int npcId){
        pp.gameState = pp.dialogState;

        if(pp.NPC[npcId] == null){
            LOGGER.severe("Couldn't access NPC, NPC was erased or not created!");
        }

        if(pp.NPC[npcId].curDialogue >= pp.NPC[npcId].dialogueString.length-1){
            pp.NPC[npcId].curDialogue = -1;
            pp.gameState = pp.playState;
        }
    }


    /**
     * Returns next walk animation image.
     * */
    public BufferedImage getNextWalkAnimImage(BufferedImage[] walkAnim, int spriteNum, String input) {
        switch (input) {
            case "up": return (spriteNum == 1 ) ? walkAnim[0] : walkAnim[1];
            case "down": return (spriteNum == 1 ) ? walkAnim[2] : walkAnim[3];
            case "left": return (spriteNum == 1 ) ? walkAnim[4] : walkAnim[5];
            case "right": return (spriteNum == 1 ) ? walkAnim[6] : walkAnim[7];
            default: break;
        }
        return walkAnim[0];
    }


    /**
     * Returns next attack animation image.
     * */
    public BufferedImage getNextAttackAnimImage(BufferedImage[] attackAnim, int spriteNum, String input) {
        switch (input) {
            case "up": return (spriteNum == 1 ) ? attackAnim[0] : attackAnim[1];
            case "down": return (spriteNum == 1 ) ? attackAnim[2] : attackAnim[3];
            case "left": return (spriteNum == 1 ) ? attackAnim[4] : attackAnim[5];
            case "right": return (spriteNum == 1 ) ? attackAnim[6] : attackAnim[7];
            default: break;
        }
        return attackAnim[0];
    }

    public void eventDetect(Player player) {
        if(triggerPointIntersect(37,43)){
            damagePit();
        } else if (triggerPointIntersect(23, 7)){
            healingPool();
        }
    }


    /**
     * Upon interacting with a certain trigger point, returns name of an event to trigger.
     * */
    public String getEvent(){
        if(triggerPointIntersect(37, 43)){
            return "damagePit";
        } else if (triggerPointIntersect(23, 7)){
            return "healingPool";
        } else{
            return "";
        }
    }

    /**
     * Activates damage pit event.
     * Damages a player, shows message and teleports player back a tile.
     * */
    public void damagePit(){
        LOGGER.info("Damage pit event triggered!");
        pp.gameState = pp.dialogState;
        if(pp.player.curHP > 0) {
            pp.player.curHP--;
        } else {
            pp.player.kill("player", 0);
        }
    }


    /**
     * Collision check with an event trigger point.
     *
     * Calculates player's current hitbox position in the world,
     * calculates trigger point current position in the world,
     * checks for rectangles intersection and returns true upon intersection.
     * */
    public boolean triggerPointIntersect(int worldX, int worldY){

        boolean hit = false;
        pp.player.solidArea.x += pp.player.worldX;
        pp.player.solidArea.y += pp.player.worldY;
        triggerPoint.x += worldX*pp.tileSize;
        triggerPoint.y += worldY*pp.tileSize;

        if(pp.player.solidArea.intersects(triggerPoint)){
            hit = true;
        }

        //Setting back to default
        pp.player.solidArea.x = pp.player.solidAreaDefaultX;
        pp.player.solidArea.y = pp.player.solidAreaDefaultY;
        triggerPoint.x = 23;
        triggerPoint.y = 23;

        return hit;
    }

    /**
     * Healing pool event gives player 1 HP and prints a special dialogue.
     * */
    public void healingPool(){
        if(pp.player.inputHandler.interactInput){
            LOGGER.info("Healing pool event triggered!");
            pp.gameState = pp.dialogState;
            if(pp.player.curHP < 6) {
                pp.player.curHP++;
            }
        }
    }

    /**
     * Handles player getting hit.
     * Upon getting hit, player becomes invincible and gets pushed back.
     * Pushing back is implemented with a for loop to avoid being
     * stuck in collidable tiles.
     * */
    public void playerGotHit(Entity entity){
        if(entity != null && !pp.player.invincible){
            switch (entity.direction) {
                case "up" -> {
                    for (int i = 10; i > 0; i--) {
                        pp.player.direction="up";
                        if(!pp.collision.checkTiles(pp.player)) {
                            pp.player.worldY -= i;
                        }
                    }
                }
                case "down" -> {
                    for (int i = 10; i > 0; i--) {
                        pp.player.direction="down";
                        if(!pp.collision.checkTiles(pp.player)) {
                            pp.player.worldY+=i;
                        }
                    }
                }
                case "left" -> {
                    for (int i = 10; i > 0; i--) {
                        pp.player.direction="left";
                        if(!pp.collision.checkTiles(pp.player)) {
                            pp.player.worldX-=i;
                        }
                    }
                }
                case "right" -> {
                    for (int i = 10; i > 0; i--) {
                        pp.player.direction="right";
                        if(!pp.collision.checkTiles(pp.player)) {
                            pp.player.worldX+=i;
                        }
                    }
                }
                default -> {}
            }
            if(pp.player.curHP > 0) {
                pp.player.curHP+=(entity.atk-pp.player.getDef());
                pp.playSE(6);
                LOGGER.info("Player got hit, enabling 90 frames of invincibility.");
                pp.player.invincible = true;
            } else {
                pp.player.kill("player", 0);
            }
        }
    }

    /**
     * Damage calculation for when a player hits an enemy.
     * Activates enemy's invincibility frames.
     * */
    public void handleDamage(int enemyId){
        if(enemyId != -1){
            if(!pp.enemy[enemyId].invincible) {
                pp.enemy[enemyId].curHP-= pp.player.getAtk();
                pp.playSE(5);
                pp.enemy[enemyId].invincible = true;
                LOGGER.info("Enemy got hit, enabling 30 frames of invincibility.");
            }
        }
    }

}
