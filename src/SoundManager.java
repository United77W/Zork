import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {

    public static void playDoorChimeBlocking() {
        try {
            File file = new File("sounds/door_chime.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playAnnouncement(String stationId) {
        switch (stationId) {
            case "Cedarvale Station":
                playBlocking("sounds/cedarvale.wav");
                break;
            case "Finch West Station":
                playBlocking("sounds/finch_west.wav");
                break;
            case "Union Station":
                playBlocking("sounds/union_station.wav");
                break;
            default:
                break;
        }
    }

    private static void playBlocking(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            clip.start();

            // wait until sound finishes
            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}