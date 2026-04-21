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

    public static void playAnnouncement(String stationId, String line) {
        switch (stationId) {
            case "Cedarvale Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/cedarvale_line1.wav");
                } else if (line != null && line.equals("Line 5")) {
                    playBlocking("sounds/cedarvale_line5.wav");
                }
                break;
            case "Finch West Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/finch_west_1.wav");
                } else if (line != null && line.equals("Line 6")) {
                    playBlocking("sounds/finchwest6.wav");
                }
                break;
            case "Union Station":
                playBlocking("sounds/union.wav");
                break;
            case "Jane and Finch Station":
                playBlocking("sounds/janeandfinch.wav");
                break;
            case "Finch Station":
                playBlocking("sounds/finch.wav");
                break;
            case "Bloor-Yonge Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/bloor_yonge_1.wav");
                } else if (line != null && line.equals("Line 2")) {
                    playBlocking("sounds/bloor_yonge_line2.wav");
                }
                break;
            case "TMU Station":
                playBlocking("sounds/tmu.wav");
                break;
            case "Vaughan Metropolitan Centre Station":
                playBlocking("sounds/vmc.wav");
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