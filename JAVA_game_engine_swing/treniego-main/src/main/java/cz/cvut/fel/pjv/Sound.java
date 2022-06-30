package cz.cvut.fel.pjv;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Class that loads sounds files into Clip class format.
 * Methods allow for play, stop or loop for a chosen sound file.*/
public class Sound {

    private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/Sound prototype/Sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/Sound prototype/Sound/coin.wav");
        soundURL[2] = getClass().getResource("/Sound prototype/Sound/powerup.wav");
        soundURL[3] = getClass().getResource("/Sound prototype/Sound/unlock.wav");
        soundURL[4] = getClass().getResource("/Sound prototype/Sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/Sound prototype/Sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/Sound prototype/Sound/receivedamage.wav");

        for (int i = 0; i < 7; i++) {
            if(soundURL[i] == null){
                LOGGER.severe("Couldn't find path for the "+i+" sound file!");
            }
        }
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception ignored){
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
