package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.lvl.object.ObjectHeart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.lvl.object.Object;

/**
 * UI Class that calls different methods depending on game state.
 * */
public class UI {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );
    public PlayPanel pp;
    public Graphics2D g2;
    Util util = new Util();
    Font arial_20, arial_40, arial_80B;
    public int curCommand = 0;

    //Prepare images
    BufferedImage heartFull, heartHalf, heartBlank;

    //Dialogue frame settings
    final public int x;
    final public int y;
    final public int w;
    final public int h;

    public UI(PlayPanel pp) {
        if(pp == null){
            LOGGER.severe("Couldn't pass PLay Panel!");
        }
        this.pp = pp;

        //Dialogue font
        this.arial_20 = new Font("Arial", Font.PLAIN, 20);
        //In-game font
        this.arial_40 = new Font("Arial", Font.PLAIN, 40);
        //System font
        this.arial_80B = new Font("Arial", Font.BOLD, 80);

        // Dialogue frame setting
        x = pp.tileSize*2;
        y = pp.tileSize/2;
        w = pp.tileSize*21;
        h = pp.tileSize*5;

        //Create HUD object
        Object heart = new ObjectHeart(pp);
        heartBlank = heart.image;
        heartHalf = heart.image2;
        heartFull = heart.image3;

    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        if(g2 == null){
            LOGGER.severe("Couldn't load graphics component for UI!");
        }

        assert g2 != null;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //Play State
        if(pp.gameState == pp.playState) {
            drawPlayState(g2);

        //Pause State
        } else if (pp.gameState == pp.pauseState) {
            drawPauseState(g2);

        // Dialog State
        } else if (pp.gameState == pp.dialogState){
            drawDialogueState(g2);

        //Title screen
        } else if (pp.gameState == pp.titleScreen){
            drawTitleScreen(g2);

        //Char screen
        } else if (pp.gameState == pp.characterState){
            drawCharScreen(g2);
        }
    }

    /**
     * Draws a window with charater hp, atk, def, key holded, weapon and shield name
     * */
    public void drawCharScreen(Graphics2D g2){
        //Window size
        int x = pp.tileSize*2;
        int y = pp.tileSize;
        int w = pp.tileSize*10;
        int h = pp.tileSize*12;
        drawSubWindow(new Color(0,0,0,200) ,x, y, w, h);

        //Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int txtX = x+pp.tileSize/2;
        int txtY = y*2;
        int lineH = pp.tileSize*2;
        g2.drawString("HP: ", txtX, txtY);
        txtY += lineH;
        g2.drawString("ATK: ", txtX, txtY);
        txtY += lineH;
        g2.drawString("DEF: ", txtX, txtY);
        txtY += lineH;
        g2.drawString("KEY: ", txtX, txtY);
        txtY += lineH;
        g2.drawString("WEP: ", txtX, txtY);
        txtY += lineH;
        g2.drawString("SHLD: ", txtX, txtY);

        int tailX = x+w;
        txtY = x;
        txtX = util.getTextEnd(g2 ,""+pp.player.curHP, tailX) - pp.tileSize/2;
        g2.drawString(""+pp.player.curHP, txtX, txtY);
        txtY+=lineH;
        g2.drawString(""+pp.player.getAtk(), txtX, txtY);
        txtY+=lineH;
        g2.drawString(""+pp.player.getDef(), txtX, txtY);
        txtY+=lineH;
        g2.drawString(""+pp.player.keys, txtX, txtY);
        txtY+=lineH;
        txtX = util.getTextEnd(g2 ,pp.player.getWepname(), tailX) - pp.tileSize/2;
        g2.drawString(pp.player.getWepname(), txtX, txtY);
        txtY+=lineH;
        txtX = util.getTextEnd(g2 ,pp.player.getShldName(), tailX) - pp.tileSize/2;
        g2.drawString(pp.player.getShldName(), txtX, txtY);

    }

    /**
     * In play state, only HP and coordinates are shown on screen
     * and dynamically updated.
     * */
    public void drawPlayState(Graphics2D g2){
        drawPlayerHP(g2, pp.player.curHP);
        g2.setFont(arial_20);
        g2.setColor(Color.white);
        String x = "X: " + pp.player.worldX / pp.tileSize;
        String y = "Y: " + (pp.player.worldY + pp.tileSize) / pp.tileSize;
        g2.drawString(x+" "+y, pp.tileSize/4, pp.tileSize*15);
    }

    /**
     * Pause state UI is text.
     * */
    public void drawPauseState(Graphics2D g2){
        g2.setFont(arial_80B);
        g2.setColor(Color.white);
        drawPauseScreen(g2);
    }

    /**
     * Dialogue state boots up dialogue window and text that's drawn on the window.
     * */
    public void drawDialogueState(Graphics2D g2){
        int npcId = pp.collision.checkEntityCollision(pp.player, pp.NPC);
        if(npcId != -1) {
            drawDialogueScreen(pp.NPC[npcId].dialogueString[pp.NPC[npcId].curDialogue]);
        } else if(pp.actionHandler.getEvent().contentEquals("damagePit")){
            drawDialogueScreen(pp.actionHandler.damagePitMessage);
        } else if(pp.actionHandler.getEvent().contentEquals("healingPool")){
            drawDialogueScreen(pp.actionHandler.healingPoolMessage);
        }
    }

    /**
     * Draws background image, menu options and title name.
     * */
    public void drawTitleScreen(Graphics2D g2){
        //Background
        try {
            BufferedImage background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background/background.jpg")));
            background = util.scaleImage(background, pp.screenWidth, pp.screenHeight);
            g2.drawImage(background, 0, 0, null);
        } catch (IOException e){
            LOGGER.severe("Couldn't find path for title screen background image!");
        }

        //TitleName
        g2.setFont(arial_80B);
        g2.setColor(Color.orange);
        String text = "Java Course Assignment";
        g2.drawString(text, pp.screenWidth/2 - util.getTextSize(text, g2), pp.tileSize*3);

        //Menu
        g2.setColor(Color.orange);
        g2.setFont(arial_40);

        text = "NEW GAME";

        g2.drawString(text,pp.screenWidth/2 - util.getTextSize(text, g2), pp.tileSize*5);
        if(curCommand == 0){
            g2.drawString(">", pp.screenWidth/2 - util.getTextSize(text, g2) - pp.tileSize, pp.tileSize*5);
        }

        text = "LOAD GAME";
        g2.drawString(text,pp.screenWidth/2 - util.getTextSize(text, g2), pp.tileSize*6);
        if(curCommand == 1){
            g2.drawString(">", pp.screenWidth/2 - util.getTextSize(text, g2) - pp.tileSize, pp.tileSize*6);
        }

        text = "QUIT";
        g2.drawString(text,pp.screenWidth/2 - util.getTextSize(text, g2), pp.tileSize*7);
        if(curCommand == 2){
            g2.drawString(">", pp.screenWidth/2 - util.getTextSize(text, g2) - pp.tileSize, pp.tileSize*7);
        }
    }


    /**
     * Depending on player's current health, draws hearts.
     * */
    public void drawPlayerHP(Graphics2D g2, int HP){
        int x = pp.tileSize/4;
        int y = x;
        for (int i = 0; i < 3; i++) {
            if(HP>=2) {
                g2.drawImage(heartFull, x, y, null);
                x += pp.tileSize;
                HP-=2;
            } else if (HP==1){
                g2.drawImage(heartHalf, x, y, null);
                x += pp.tileSize;
                HP-=1;
            } else {
                g2.drawImage(heartBlank, x, y, null);
                x += pp.tileSize;
            }
        }
    }

    /**
     * Dialogue screen gets booten when interacting with an NPC.
     * Draws dialogue window and NPC's speech.
     * */
    public void drawDialogueScreen(String dialogueString){

        Color c = new Color(0,0,0, 200);

        drawSubWindow(c ,x, y, w, h);
        printDialogueString(dialogueString, x, y);

        g2.setColor(Color.white);
        g2.setFont(arial_20);
        g2.drawString("E to continue", x + pp.tileSize*18, y+pp.tileSize*4+pp.tileSize/2);
    }

    /**
     * Draws black window with white frame, with transparency parameter.
     * */
    public void drawSubWindow(Color c, int x, int y, int w, int h){
        g2.setColor(c);
        g2.fillRoundRect(x, y, w, h, 20, 20);

        c = new Color(255, 255, 255, 200);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, w-10, h-10, 20, 20);
    }

    /**
     * Prints a string of white color and arial 20 font on certain x and y.
     * */
    public void printDialogueString(String dialogueString, int x, int y){
        g2.setFont(arial_20);
        Color c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.drawString(dialogueString, x + pp.tileSize/2, y+pp.tileSize);
    }
    /**
     * Draws a string that says "PAUSED" at the center of the screen.
     * *  */
    public void drawPauseScreen(Graphics2D g2) {
        g2.drawString("PAUSED",pp.screenWidth/2 - util.getTextSize("PAUSED", g2) , pp.screenHeight/2);
        g2.setFont(arial_20);
        g2.drawString("Press E to go to main menu",pp.screenWidth/2 - util.getTextSize("Press E to go to main menu", g2) , pp.screenHeight/2+pp.tileSize*2);
    }
}
