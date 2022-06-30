package cz.cvut.fel.pjv.lvl.object.tile;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.PlayPanel;
import cz.cvut.fel.pjv.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

public class TileManager {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    PlayPanel pp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(PlayPanel pp) {
        if(pp == null){
            LOGGER.severe("Couldn't pass Play Panel!");
        }
        this.pp = pp;

        tile = new Tile[10];
        assert pp != null;
        mapTileNum = new int[pp.maxWorldXSize][pp.maxWorldYSize];

        getTileImage();
        loadMap("/Maps/map.txt");
    }

    public void getTileImage() {
            setup(0, "/Tiles/New version/grass00.png", false);
            setup(1, "/Tiles/New version/wall.png", true);
            setup(2, "/Tiles/New version/water00.png", true);
            setup(3, "/Tiles/New version/earth.png", false);
            setup(4, "/Tiles/New version/tree.png", true);
            setup(5, "/Tiles/New version/road00.png", false);
    }

    /**
     * Fully sets up tile's settings.
     * */
    public void setup(int idx, String imagePath, boolean collision){

        Util util = new Util();

        try {
            tile[idx] = new Tile();
            tile[idx].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            tile[idx].image = util.scaleImage(tile[idx].image, pp.tileSize, pp.tileSize);
            tile[idx].collision = collision;
        }catch(IOException e){
            LOGGER.severe("Couldn't find path of the image!");
        }
    }


    /**
     * For a scpecific given tile, if one's coordinates lie on player's screen,
     * draw tile. Move tiles accordingly to player's position at the center of the screen.
     * */
    public void draw(Graphics2D g2) {

        for (int i = 0; i < pp.maxWorldXSize; i++) {
            for (int j = 0; j < pp.maxWorldYSize; j++) {
                int tileNum = mapTileNum[i][j];
                int curX = i * pp.tileSize;
                int curY = j * pp.tileSize;
                int screenX = curX - pp.player.worldX + pp.player.screenX;
                int screenY = curY - pp.player.worldY + pp.player.screenY;
                if(ifOnScreen(curX, curY)){
                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                }
            }
        }
    }

    public boolean ifOnScreen(int curX, int curY) {
        return (curX + pp.tileSize > pp.player.worldX - pp.player.screenX &&
                curX - pp.tileSize < pp.player.worldX + pp.player.screenX &&
                curY + pp.tileSize > pp.player.worldY - pp.player.screenY &&
                curY - pp.tileSize < pp.player.worldY + pp.player.screenY);
    }


    /**
     * For a given TXT file with specific numbers for tiles, boots it into a 2D array
     * and uses it to create the game map.
     * */
    public void loadMap(String mapPath) {
        try{
            InputStream is = getClass().getResourceAsStream(mapPath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (int i = 0; i < pp.maxWorldXSize; i++) {
                String line = br.readLine();
                String[] tileCode = line.split(" ");
                for (int j = 0; j < pp.maxWorldYSize; j++) {
                    int tileDecode = Integer.parseInt(tileCode[j]);
                    mapTileNum[j][i] = tileDecode;
                }
            }
            br.close();
        }catch(Exception ignored){
            LOGGER.severe("Couldn't find path of the map file!");
        }
    }

}
