package cz.cvut.fel.pjv;

import javax.swing.*;
import java.util.logging.Logger;

public class Game {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );

    /** Main function.
     * Sets parameters to the window
     * makes it visible, sets coordinates to appear at center
     * makes it closable*/
    public static  void  main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Prototype_treniego");

        PlayPanel playPanel = new PlayPanel();
        window.add(playPanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        playPanel.setupGame();
        playPanel.startGameThread();
    }
}
