package cz.cvut.fel.pjv.entity.NPC;

import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.entity.AI;
import cz.cvut.fel.pjv.entity.Entity;
import java.awt.*;

/**
 * NPC class.
 * Holds dialogue and specific update method.
 * */
public class OldMan extends Entity {

    final AI ai = new AI();

    public OldMan(PlayPanel pp){
        super(pp);
        direction = "down";
        speed = 1;
        solidArea = new Rectangle(0, 0, pp.tileSize, pp.tileSize);

        dialogueString = new String[]{"Old Wizard: Wow!", "Old Wizard: Looks like i can speak!", "Old Wizard: This wonderful student gave me a body and a voice...",
                "Old Wizard: I would like to quote something on this wonderful occasion...",
                "Old Wizard: 'Through a simple act of creation, do i become a reflection of my own creator?'"};
        curDialogue = 0;

        maxHP = 10;
        curHP = maxHP;

        getNPCImage();
    }

    public void getNPCImage() {
        walkAnim[0] = setup("/NPC/NPC/oldman_up_1.png", pp.tileSize, pp.tileSize);
        walkAnim[1] = setup("/NPC/NPC/oldman_up_2.png", pp.tileSize, pp.tileSize);
        walkAnim[2] = setup("/NPC/NPC/oldman_down_1.png", pp.tileSize, pp.tileSize);
        walkAnim[3] = setup("/NPC/NPC/oldman_down_2.png", pp.tileSize, pp.tileSize);
        walkAnim[4] = setup("/NPC/NPC/oldman_left_1.png", pp.tileSize, pp.tileSize);
        walkAnim[5] = setup("/NPC/NPC/oldman_left_2.png", pp.tileSize, pp.tileSize);
        walkAnim[6] = setup("/NPC/NPC/oldman_right_1.png", pp.tileSize, pp.tileSize);
        walkAnim[7] = setup("/NPC/NPC/oldman_right_2.png", pp.tileSize, pp.tileSize);
    }


    /**
     * Has collision on tiles and objects.
     * Faces player, if one interacts with this NPC.
     * */
    public void update(){
        ai.randomWalk(this);

        collisionOn = false;

        //Has collision on: Tiles, objects
        pp.collision.checkTiles(this);
        pp.collision.checkObject(this);

        // Face player when talk
        if(pp.collision.checkEntityCollision(pp.player, pp.NPC) != -1 && pp.gameState == pp.dialogState) {
            switch (pp.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }

        if(!collisionOn) {
            actionHandler.walk(direction, this);
            changeSpriteEveryNFrames(12);
        }
    }

}
