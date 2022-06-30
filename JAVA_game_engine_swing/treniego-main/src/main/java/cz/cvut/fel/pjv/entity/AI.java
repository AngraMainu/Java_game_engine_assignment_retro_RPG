package cz.cvut.fel.pjv.entity;

import java.util.Random;

public class AI {

    private int actionLock = 0;

    public AI(){

    }

    /**
     * Makes an entity walk in a random direction every two seconds.
     * */
    public void randomWalk(Entity entity){
        actionLock++;
        if(actionLock > 120) {
            Random random = new Random();
            int i = random.nextInt(100);

            if (i <= 25) {
                entity.direction = "up";
            } else if (i <= 50) {
                entity.direction = "down";
            } else if (i <= 75) {
                entity.direction = "left";
            } else {
                entity.direction = "right";
            }
            actionLock = 0;
        }
    }

}
