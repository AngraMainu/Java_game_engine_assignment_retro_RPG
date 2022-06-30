package cz.cvut.fel.pjv;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;


public class InputHandler implements KeyListener {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );
    PlayPanel pp;
    UI ui;

    public boolean upInput, downInput, leftInput, rightInput, interactInput;

    @Override
    public void keyTyped(KeyEvent e) {
        // UNUSED
    }

    public InputHandler(PlayPanel pp, UI ui){
        if(pp == null || ui == null) {
            LOGGER.severe("Couldn't pass Play Panel or/and UI!");
        }
        this.pp = pp;
        this.ui = ui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upInput = true;
            if(pp.gameState == pp.titleScreen){
                if(ui.curCommand == 0){
                    ui.curCommand = 2;
                } else {
                    ui.curCommand--;
                }
                pp.playSE(1);
            }
        }
        if(code == KeyEvent.VK_S){
            downInput = true;
            if(pp.gameState == pp.titleScreen){
                if(ui.curCommand == 2){
                    ui.curCommand = 0;
                } else {
                    ui.curCommand++;
                }
                pp.playSE(1);
            }
        }
        if(code == KeyEvent.VK_A){
            leftInput = true;
        }
        if(code == KeyEvent.VK_D){
            rightInput = true;
        }
        if(code == KeyEvent.VK_E){
            if(pp.gameState == pp.titleScreen){
                if(ui.curCommand == 0){
                    pp.gameState = pp.playState;
                    pp.playSE(2);
                } else if (ui.curCommand == 1){
                } else if (ui.curCommand == 2){
                    pp.playSE(3);
                    System.exit(0);
                }
            } else if (pp.gameState == pp.pauseState){
                LOGGER.info("Game state is switched.");
                pp.gameState = pp.titleScreen;
            } else {
                interactInput = true;
            }
        }
        if(code == KeyEvent.VK_ESCAPE){
            if(pp.gameState == pp.playState) {
                LOGGER.info("Game state is switched.");
                pp.gameState = pp.pauseState;
            } else if (pp.gameState == pp.pauseState || pp.gameState == pp.dialogState) {
                LOGGER.info("Game state is switched.");
                pp.gameState = pp.playState;
            }
        }
        if(code == KeyEvent.VK_C){
            if(pp.gameState == pp.playState){
                LOGGER.info("Game state is switched.");
                pp.gameState = pp.characterState;
            }else if(pp.gameState == pp.characterState){
                LOGGER.info("Game state is switched.");
                pp.gameState = pp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upInput = false;
        }
        if(code == KeyEvent.VK_S){
            downInput = false;
        }
        if(code == KeyEvent.VK_A){
            leftInput = false;
        }
        if(code == KeyEvent.VK_D){
            rightInput = false;
        }
        if(code == KeyEvent.VK_E){
            interactInput = false;
        }
    }

    public boolean anyDirectionInput() {
        return upInput || downInput || leftInput || rightInput;
    }
}
