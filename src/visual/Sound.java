package visual;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 * An extra class that allows us to play audio files to spice up the program a bit more.
 */
public class Sound {
    private Clip clip;
    /*
     * Finds and sets up clip
     */
    public Sound() {
        try {
            File f = new File("res/sound/banger_of_a_song.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Starts playing the clip from the beginning
     */
    public void start() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
