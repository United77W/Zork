import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    public static void playDoorChime() {
        play("sounds/door_chime.wav");
    }

    private static void play(String filePath) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.out.println("Failed to play sound.");
            e.printStackTrace();
        }
    }
}