package cz.cvut.fel.pjv.collision;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.entity.Entity;

import java.util.logging.Logger;

/**
 * Collision class provides collision checks for tiles, entities and objects.
 * Can check for a single or an array of entities.
 * */
public class Collision {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    private final PlayPanel pp;

    public Collision(PlayPanel pp) {
        this.pp = pp;
    }


    /**
     * Depending on player's direction, checks scpecific side for better performance.
     * */
    public boolean checkTiles(Entity entity) {
        boolean ret = false;
        switch (entity.direction) {
            case "up" -> ret = checkTileAbove(entity);
            case "down" -> ret = checkTileBelow(entity);
            case "left" -> ret = checkTileLeft(entity);
            case "right" -> ret = checkTileRight(entity);
            default -> {
            }
        }
        return ret;
    }


    /**
     * Checks collision above player's sprite.
     *
     * Calculates two points from player's current position in the world:
     * left top and right top corners of player's hitbox.
     * Predicts players next position.
     * If a tile above either of two predicted points is collidable, sets collision to true
     * and prevents player from moving forward.
     * */
    public boolean checkTileAbove(Entity entity) {
        boolean upContact = false;
        int topWorldY = entity.worldY + entity.solidArea.y;
        int leftWorldX = entity.worldX + entity.solidArea.x;
        int rightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int leftCol = leftWorldX/pp.tileSize;
        int rightCol = rightWorldX/pp.tileSize;

        int tileNum1, tileNum2;
        // Checking top right and left corners of entity's hitbox
        int topRow = (topWorldY - entity.speed) / pp.tileSize;
        tileNum1 = pp.tileM.mapTileNum[leftCol][topRow];
        tileNum2 = pp.tileM.mapTileNum[rightCol][topRow];

        // If a tile above me is collidable, then collision = true, movement stop
        if (pp.tileM.tile[tileNum1].collision || pp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
            upContact = true;
        }
        return upContact;
    }

    /**
     * Checks collision above player's sprite.
     *
     * Calculates two points from player's current position in the world:
     * left bottom and right bottom corners of player's hitbox.
     * Predicts players next position.
     * If a tile below either of two predicted points is collidable, sets collision to true
     * and prevents player from moving forward.
     * */
    public boolean checkTileBelow(Entity entity) {
        boolean downContact = false;
        int leftWorldX = entity.worldX + entity.solidArea.x;
        int rightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int bottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        int leftCol = leftWorldX/pp.tileSize;
        int rightCol = rightWorldX/pp.tileSize;

        int tileNum1, tileNum2;
        // Checking bot right and left corners of entity's hitbox
        int bottowRow = (bottomWorldY + entity.speed) / pp.tileSize;
        tileNum1 = pp.tileM.mapTileNum[leftCol][bottowRow];
        tileNum2 = pp.tileM.mapTileNum[rightCol][bottowRow];
        // If a tile below me is collidable, then collision = true, movement stop
        if (pp.tileM.tile[tileNum1].collision || pp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
            downContact = true;
        }
        return downContact;
    }

    /**
     * Checks collision above player's sprite.
     *
     * Calculates two points from player's current position in the world:
     * left top and left bottom corners of player's hitbox.
     * Predicts players next position.
     * If a tile to the left either of two predicted points is collidable, sets collision to true
     * and prevents player from moving forward.
     * */
    public boolean checkTileLeft(Entity entity) {
        boolean leftContact = false;
        int leftWorldX = entity.worldX + entity.solidArea.x;
        int topWorldY = entity.worldY + entity.solidArea.y;
        int bottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        int topRow = topWorldY/pp.tileSize;
        int bottowRow = bottomWorldY/pp.tileSize;

        int tileNum1, tileNum2;
        // Checking top left and bot left corners of entity's hitbox
        int leftCol = (leftWorldX - entity.speed) / pp.tileSize;
        tileNum1 = pp.tileM.mapTileNum[leftCol][topRow];
        tileNum2 = pp.tileM.mapTileNum[leftCol][bottowRow];
        // If a tile to the left is collidable, then collision = true, movement stop
        if (pp.tileM.tile[tileNum1].collision || pp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
            leftContact = true;
        }
        return leftContact;
    }

    /**
     * Checks collision above player's sprite.
     *
     * Calculates two points from player's current position in the world:
     * right bottom and right top corners of player's hitbox.
     * Predicts players next position.
     * If a tile to the right either of two predicted points is collidable, sets collision to true
     * and prevents player from moving forward.
     * */
    public boolean checkTileRight(Entity entity) {
        boolean rightContact = false;
        int rightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int topWorldY = entity.worldY + entity.solidArea.y;
        int bottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        int topRow = topWorldY/pp.tileSize;
        int bottowRow = bottomWorldY/pp.tileSize;

        int tileNum1, tileNum2;
        // Checking top right and bot right corners of entity's hitbox
        int rightCol = (rightWorldX + entity.speed) / pp.tileSize;
        tileNum1 = pp.tileM.mapTileNum[rightCol][topRow];
        tileNum2 = pp.tileM.mapTileNum[rightCol][bottowRow];
        // If a tile to the right is collidable, then collision = true, movement stop
        if (pp.tileM.tile[tileNum1].collision || pp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
            rightContact = true;
        }
        return rightContact;
    }


    /**
     * Predicts entity's next position.
     * If entity's hitbox rectangle is going to intersect
     * object's hitbox rectangle, sets collision o true and prevents entity from moving.
     * */
    public int checkObject(Entity entity) {
        int idx = -1;

        for (int i = 0; i < pp.obj.length; i++) {
            if(pp.obj[i] != null){

                // Get entity's current solid/interactable area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's current solid area position
                pp.obj[i].solidArea.x = pp.obj[i].worldX + pp.obj[i].solidArea.x;
                pp.obj[i].solidArea.y = pp.obj[i].worldY + pp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up" -> entity.solidArea.y -= entity.speed;
                    case "down" -> entity.solidArea.y += entity.speed;
                    case "left" -> entity.solidArea.x -= entity.speed;
                    case "right" -> entity.solidArea.x += entity.speed;
                    default -> {}
                }
                if (entity.solidArea.intersects(pp.obj[i].solidArea) && pp.obj[i].collision) {
                    entity.collisionOn = true;
                    pp.obj[i].interactable = true;
                    idx = i;
                } else {
                    pp.obj[i].interactable = false;
                }
                //Resetting values
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                pp.obj[i].solidArea.x = 0;
                pp.obj[i].solidArea.y = 0;
            }
        }
        return idx;
    }


    /**
     * Predicts entity's next position.
     * If entity's hitbox rectangle is going to intersect
     * entities's hitboxes rectangles, sets collision o true and prevents entity from moving.
     * */
    public int checkEntityCollision(Entity entity, Entity[] targetEntities) {
        int idx = -1;
        for (int i = 0; i < targetEntities.length; i++) {
            if(targetEntities[i] != null){

                // Get entity's current solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's current solid area position
                targetEntities[i].solidArea.x = targetEntities[i].worldX + targetEntities[i].solidArea.x;
                targetEntities[i].solidArea.y = targetEntities[i].worldY + targetEntities[i].solidArea.y;

                switch (entity.direction) {
                    case "up" -> entity.solidArea.y -= entity.speed;
                    case "down" -> entity.solidArea.y += entity.speed;
                    case "left" -> entity.solidArea.x -= entity.speed;
                    case "right" -> entity.solidArea.x += entity.speed;
                    default -> {}
                }
                if (entity.solidArea.intersects(targetEntities[i].solidArea)) {
                    entity.collisionOn = true;
                    idx = i;
                }
                //Resetting values
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                targetEntities[i].solidArea.x = targetEntities[i].solidAreaDefaultX;
                targetEntities[i].solidArea.y = targetEntities[i].solidAreaDefaultY;
            }
        }
        return idx;
    }

    /**
     * Predicts entity's next position.
     * If entity's hitbox rectangle is going to intersect
     * entity's hitboxes rectangles, sets collision o true and prevents entity from moving.
     * */
    public boolean checkSingleEntityCollision(Entity entity, Entity targetEntity) {
        boolean contact = false;

        // Get entity's current solid/interactable area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the object's current solid area position
        targetEntity.solidArea.x = targetEntity.worldX + targetEntity.solidArea.x;
        targetEntity.solidArea.y = targetEntity.worldY + targetEntity.solidArea.y;

        switch (entity.direction) {
            case "up" -> entity.solidArea.y -= entity.speed;
            case "down" -> entity.solidArea.y += entity.speed;
            case "left" -> entity.solidArea.x -= entity.speed;
            case "right" -> entity.solidArea.x += entity.speed;
            default -> {}
        }
        if (entity.solidArea.intersects(targetEntity.solidArea)) {
            entity.collisionOn = true;
            contact = true;
        }
        //Resetting values
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        targetEntity.solidArea.x = targetEntity.solidAreaDefaultX;
        targetEntity.solidArea.y = targetEntity.solidAreaDefaultY;
        return contact;
    }

}
