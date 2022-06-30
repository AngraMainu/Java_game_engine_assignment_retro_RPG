import cz.cvut.fel.pjv.InputHandler;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.Util;
import cz.cvut.fel.pjv.entity.player.Player;

import org.junit.Test;
import org.junit.jupiter.api.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    private static Player player;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Player test in process...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Player test complete.");
    }

    @Test
    public void gearWeapon() throws IOException {
        PlayPanel pp = new PlayPanel();
        player = new Player(pp, new InputHandler(pp, new UI(pp)));

        assertEquals(player.weapon.name, "Worn Sword");
        assertEquals(player.weapon.atkVal, 1);
        assertEquals(player.getAtk(), player.atk+player.weapon.atkVal);

        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/sword_normal.png")));
        Util util = new Util();
        image = util.scaleImage(image, 48, 48);
        byte[] expectedArray = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] actualArray = ((DataBufferByte) player.weapon.image.getData().getDataBuffer()).getData();
        assertEquals(expectedArray[66], actualArray[66]);
        assertEquals(expectedArray[100], actualArray[100]);
        assertEquals(expectedArray[28], actualArray[28]);
    }

    @Test
    public void gearShield() throws IOException {
        PlayPanel pp = new PlayPanel();
        player = new Player(pp, new InputHandler(pp, new UI(pp)));

        assertEquals(player.shield.name, "Wooden Shield");
        assertEquals(player.shield.defVal, 1);
        assertEquals(player.getDef(), player.def+player.shield.defVal);

        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Objects/Objects prototype/shield_wood.png")));
        Util util = new Util();
        image = util.scaleImage(image, 48, 48);
        byte[] expectedArray = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] actualArray = ((DataBufferByte) player.shield.image.getData().getDataBuffer()).getData();
        assertEquals(expectedArray[66], actualArray[66]);
        assertEquals(expectedArray[100], actualArray[100]);
        assertEquals(expectedArray[28], actualArray[28]);
    }


}
