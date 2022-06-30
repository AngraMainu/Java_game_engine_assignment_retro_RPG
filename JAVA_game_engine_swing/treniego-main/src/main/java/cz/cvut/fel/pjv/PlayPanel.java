package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.collision.Collision;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.player.Player;
import cz.cvut.fel.pjv.lvl.object.tile.TileManager;
import cz.cvut.fel.pjv.lvl.object.Object;
import cz.cvut.fel.pjv.action.ActionHandler;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;


/**
 * Window of the game.
 * Holds settings for the world and the window such as pixel resolution.
 * Holds all the implemented classes calls update/draw functions
 * using thread.
 * Manages music.*/
public class PlayPanel extends JPanel implements Runnable{

    //Logger
    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    // Decide the screen settings
    public final int originalTileSize = 16; // 16x16 tile size
    public final int scale = 3; // Scaling for modern big screens

    public final int tileSize = originalTileSize * scale; //48x48 tile size
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol; // 1280 pxls
    public final int screenHeight = tileSize * maxScreenRow; // 720 pxls

    //World size settings (50 by 50 tiles)
    public final int maxWorldXSize= 50;
    public final int maxWorldYSize = 50;

    //FPS
    int FPS = 60;



    // System settings
    Thread gameThread;
    public TileManager tileM = new TileManager(this);
    public Sound music = new Sound();
    public Sound soundEffect = new Sound();
    public UI ui = new UI(this);
    InputHandler inputHandler = new InputHandler(this, this.ui);
    public Collision collision = new Collision(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public ActionHandler actionHandler = new ActionHandler(this);

    //Game state
    public int gameState;
    public final int titleScreen = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int characterState = 4;

    //Entity and objects
    public Player player = new Player(this, inputHandler);
    public Object[] obj = new Object[10];
    public Entity[] NPC = new Entity[10];
    public Entity[] enemy = new Entity[10];


    public PlayPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(inputHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObjects();
        aSetter.setNPCs();
        aSetter.setEnemies();
        playMusic(0);
        gameState = titleScreen;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    /** This is the main game loop. This means, that in comparison
     * to regular programs, this is where the time aspect is added
     * and in this while loop it tics, and based on those tics
     * the information on the screen is updated.
     **/
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // 60 times per second
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer+= currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
            if(timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update(){
        if(gameState == playState) {
            //Update player
            player.update();
            //Update NPCs

            for (Entity value : NPC) {
                if (value != null) {
                    value.update();
                }
            }

            //Update Enemies. If dead, enemy = null (gets destroyed and not updated anymore)
            for (int i = 0; i < this.enemy.length; i++) {
                if(this.enemy[i] != null) {
                    if(this.enemy[i].curHP > 0) {
                        this.enemy[i].update();
                    } else {
                        this.enemy[i].kill("enemy", i);
                    }
                }
            }
        } else if(gameState == pauseState) {
            //nothing
        } else if(gameState == dialogState){
            player.update();
        }
    }

    /**
     * Necessary method to update graphics on the screen
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(g2 == null){
            LOGGER.severe("Couldn't pass graphics to Paint Component method!");
        }

        if(gameState == titleScreen){
            ui.draw(g2);
        } else {
            // Tiles
            tileM.draw(g2);
            // Objects
            for (Object object : obj) {
                if (object != null) {
                    object.drawObj(g2, this);
                }
            }
            //Entity
            for (Entity value : NPC) {
                if (value != null) {
                    value.drawEntity(g2);
                }
            }
            //Eтemies
            for (Entity value : enemy) {
                if (value != null) {
                    value.drawEntity(g2);
                }
            }

            // Player
            player.drawEntity(g2);
            //UI
            ui.draw(g2);
        }
        assert g2 != null;
        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void  stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

}
