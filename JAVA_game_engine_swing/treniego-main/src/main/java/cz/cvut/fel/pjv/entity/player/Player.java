package cz.cvut.fel.pjv.entity.player;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.InputHandler;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.lvl.object.ObjectShieldNormal;
import cz.cvut.fel.pjv.lvl.object.ObjectSword;
import cz.cvut.fel.pjv.lvl.object.Object;

import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

public class Player extends Entity {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    public InputHandler inputHandler;

    public final int screenX;
    public final int screenY;

    public boolean attacking = false;

    //Char stats
    public final int atk = 1;
    public final int def = 1;
    public int keys;
    public Object weapon;
    public Object shield;

    public Player(PlayPanel pp, InputHandler inputHandler){
        super(pp);
        if (inputHandler == null){
            LOGGER.severe("Couldn't pass Input Handler!");
        }
        this.inputHandler = inputHandler;

        // World coord
        this.worldX = pp.tileSize * 23;
        this.worldY = pp.tileSize * 21;
        this.speed = 3;
        this.direction = "down";
        getPlayerImage();
        getPlayerSwordAttackImage();

        // Center of the screen
        this.screenX = pp.screenWidth/2 - (pp.tileSize/2);
        this.screenY = pp.screenHeight/2 - (pp.tileSize/2);

        // Player's hitbox
        this.solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.interactableArea = new Rectangle(0, 0, pp.tileSize*3, pp.tileSize*3);
        this.attackArea = new Rectangle(0,0,36,36);

        //Set player's stats
        this.maxHP = 6;
        this.curHP = maxHP;
        keys = 0;
        weapon = new ObjectSword(pp);
        shield = new ObjectShieldNormal(pp);
    }

    public String getWepname(){
        return weapon.name;
    }
    public String getShldName(){
        return shield.name;
    }

    public int getAtk(){
        return atk+weapon.atkVal;
    }
    public int getDef(){
        return def+shield.defVal;
    }

    /**
     * Has collision on tiles, objects and enemies.
     * Specific update method allows for input handling
     * such as not attacking when near an NPC.
     * */
    public void update() {
        int objId = -1;
        if(pp.gameState == pp.playState) {
            if(attacking){
                handleAttack();
            } else if (inputHandler.anyDirectionInput()) {

                direction = setDirection();

                // Check object collision
                this.collisionOn = false;
                this.pp.collision.checkTiles(this);

                //Object collision
                objId =  pp.collision.checkObject(this);
                pickUpObject(objId);

                // Collision on enemy
                pp.collision.checkEntityCollision(this, pp.enemy);

                // If collision == false, player can move
                if (!collisionOn) {
                    actionHandler.walk(direction, this);
                }

                //change sprite every 12 frames if moving
                changeSpriteEveryNFrames(12);

            }
            if (inputHandler.interactInput) {
                int npcId = pp.collision.checkEntityCollision(this, pp.NPC);
                interactWithNPC(npcId);
                if(objId == -1 && npcId == -1){
                    attacking = true;
                }
            }
            //Event detection
            actionHandler.eventDetect(this);

            if(invincible){
                if(invincibleCounter++ > 90) {
                    invincibleCounter = 0;
                    invincible = false;
                }
            }
        } else if (pp.gameState == pp.dialogState){
            //Either NPC dialogue or event dialogue
            if(inputHandler.interactInput){
                handleInteractWithNPC();
                handleEvent();
            }
        }
        this.inputHandler.interactInput = false;
    }


    public void pickUpObject(int objID) {
        if(objID != -1) {
            actionHandler.interactOBJ(objID, this);
        }
    }

    public void interactWithNPC(int npcID) {
        if(npcID != -1) {
            actionHandler.interactNPC(npcID);
        }
    }


    /**
     * Nearly identical collision method, but
     * for attack hitbox. If attack hitbox intersects with
     * an enemy hitbox, calls damage handle method.
     * */
    public void handleAttack(){
        spriteCounter++;
        if(spriteCounter < 13){
            spriteNum=1;
        }else if (spriteCounter < 36) {
            spriteNum=2;

            //saveCur worldX, worldY, solidArea
            int curX = worldX;
            int curY = worldY;
            int solidAreaW = solidArea.width;
            int solidAreaH = solidArea.height;

            //Adjust player's hitbox for attack
            switch (direction) {
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.width;
                case "right" -> worldX += attackArea.width;
                default -> {
                }
            }
            //Transforming player gitbox into attack hitbox for easier collision use
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //Check for hit collision
            int enemyId = pp.collision.checkEntityCollision(this, pp.enemy);
            actionHandler.handleDamage(enemyId);

            worldX = curX;
            worldY = curY;
            solidArea.width = solidAreaW;
            solidArea.height = solidAreaH;

        } else {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }


    /**
     * Checks for collision with trigger points
     * teleports character back a tile if got into damaging pit event.
     * */
    public void handleEvent(){
        if(actionHandler.getEvent().contentEquals("damagePit")){
            if(Objects.equals(this.direction, "right")){
                this.worldX -=pp.tileSize;
            } else if (Objects.equals(this.direction, "down")){
                this.worldY -=pp.tileSize;
            }
            pp.gameState = pp.playState;
        } else if (actionHandler.getEvent().contentEquals("healingPool")){
            pp.gameState = pp.playState;
        }
    }

    /**
     * Checks for a valid NPC id
     * and calls the interact method if so.
     * */
    public void handleInteractWithNPC() {
        int npcId = pp.collision.checkEntityCollision(this, pp.NPC);
        if(npcId != -1) {
            actionHandler.interactNPC(npcId);
            pp.NPC[npcId].curDialogue++;
        }
    }

    public String setDirection() {
        if (inputHandler.upInput) {
            return "up";
        } else if (inputHandler.downInput) {
            return "down";
        } else if (inputHandler.leftInput) {
             return "left";
        } else if (inputHandler.rightInput) {
            return "right";
        }
        return "";
    }


    /**
     * If invincibility frames are on, draw with transparency parameter.
     * Else draw normaly.
     * */
    public void drawEntity(Graphics2D g2) {

        //Draws character in the center of the screen
        if(invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        if(!attacking) {
            g2.drawImage(actionHandler.getNextWalkAnimImage(this.walkAnim, spriteNum, direction), screenX, screenY, null);
        } else {
            int shiftedScreenX = screenX;
            int shiftedScreenY = screenY;
            if(Objects.equals(direction, "up")){
                shiftedScreenY = screenY - pp.tileSize;
            }else if (Objects.equals(direction, "left")){
                shiftedScreenX = screenX - pp.tileSize;
            }
            g2.drawImage(actionHandler.getNextAttackAnimImage(this.attackAnim, spriteNum, direction), shiftedScreenX, shiftedScreenY, null);
        }
    }

    public void getPlayerImage() {
        walkAnim[0] = setup("/PlayerAnim/Walking sprites/boy_up_1.png", pp.tileSize, pp.tileSize);
        walkAnim[1] = setup("/PlayerAnim/Walking sprites/boy_up_2.png", pp.tileSize, pp.tileSize);
        walkAnim[2] = setup("/PlayerAnim/Walking sprites/boy_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[3] = setup("/PlayerAnim/Walking sprites/boy_down_2.png", pp.tileSize, pp.tileSize);
        walkAnim[4] = setup("/PlayerAnim/Walking sprites/boy_left_1.png", pp.tileSize, pp.tileSize);
        walkAnim[5] = setup("/PlayerAnim/Walking sprites/boy_left_2.png", pp.tileSize, pp.tileSize);
        walkAnim[6] = setup("/PlayerAnim/Walking sprites/boy_right_1.png", pp.tileSize, pp.tileSize);
        walkAnim[7] = setup("/PlayerAnim/Walking sprites/boy_right_2.png", pp.tileSize, pp.tileSize);
    }
    public void getPlayerSwordAttackImage() {
        attackAnim[0] = setup("/Attackingsprites/Attackingsprites/boy_attack_up_1.png", pp.tileSize, pp.tileSize*2);
        attackAnim[1] = setup("/Attackingsprites/Attackingsprites/boy_attack_up_2.png", pp.tileSize, pp.tileSize*2);
        attackAnim[2] = setup("/Attackingsprites/Attackingsprites/boy_attack_down_1.png", pp.tileSize, pp.tileSize*2);
        attackAnim[3] = setup("/Attackingsprites/Attackingsprites/boy_attack_down_2.png", pp.tileSize, pp.tileSize*2);
        attackAnim[4] = setup("/Attackingsprites/Attackingsprites/boy_attack_left_1.png", pp.tileSize*2, pp.tileSize);
        attackAnim[5] = setup("/Attackingsprites/Attackingsprites/boy_attack_left_2.png", pp.tileSize*2, pp.tileSize);
        attackAnim[6] = setup("/Attackingsprites/Attackingsprites/boy_attack_right_1.png", pp.tileSize*2, pp.tileSize);
        attackAnim[7] = setup("/Attackingsprites/Attackingsprites/boy_attack_right_2.png", pp.tileSize*2, pp.tileSize);
    }
}
