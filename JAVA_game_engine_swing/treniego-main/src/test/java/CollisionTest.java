import cz.cvut.fel.pjv.InputHandler;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.collision.Collision;
import cz.cvut.fel.pjv.entity.enemy.Enemy;
import cz.cvut.fel.pjv.entity.player.Player;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollisionTest {

    private static Collision collision;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Collision test in process...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Collision test complete.");
    }

    @Test
    public void tileCollision(){
        PlayPanel pp = new PlayPanel();
        Player player = new Player(pp, new InputHandler(pp, new UI(pp)));
        collision = new Collision(pp);

        player.spawn(2, 2);

        boolean expectedRes = true;
        boolean actualRes = collision.checkTiles(player);
        assertEquals(expectedRes,actualRes);

        player.direction = "up";
        actualRes = collision.checkTiles(player);
        assertEquals(expectedRes,actualRes);

        player.direction = "left";
        actualRes = collision.checkTiles(player);
        assertEquals(expectedRes,actualRes);

        player.direction = "right";
        actualRes = collision.checkTiles(player);
        assertEquals(expectedRes,actualRes);

        player.direction = "down";
        player.worldX = 24*pp.tileSize;
        player.worldY = 37*pp.tileSize;
        expectedRes = false;
        actualRes = collision.checkTiles(player);
        assertEquals(expectedRes,actualRes);

    }

    @Test
    public void entityCollision(){
        PlayPanel pp = new PlayPanel();
        Player player = new Player(pp, new InputHandler(pp, new UI(pp)));
        Enemy slime = new Enemy(pp);
        collision = new Collision(pp);

        player.spawn(23,23);
        slime.spawn(22,23);

        player.direction = "left";
        boolean actualRes = collision.checkSingleEntityCollision(player, slime);
        boolean expectedRes = true;
        assertEquals(expectedRes, actualRes);

        player.worldX = 25*pp.tileSize;
        actualRes = collision.checkSingleEntityCollision(player, slime);
        expectedRes = false;
        assertEquals(expectedRes, actualRes);
    }

}
