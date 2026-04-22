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
                    playBlocking("sounds/cedarvale5.wav");
                }
                break;
            case "Eglinton Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/eglinton_1.wav");
                } else if (line != null && line.equals("Line 5")) {
                    playBlocking("sounds/eglinton5.wav");
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
            case "Don Valley Station":
                playBlocking("sounds/donvalley.wav");
                break;
            case "Bloor-Yonge Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/bloor_yonge_1.wav");
                } else if (line != null && line.equals("Line 2")) {
                    playBlocking("sounds/yonge.wav");
                }
                break;
            case "Kennedy Station":
                if (line != null && line.equals("Line 2")) {
                    playBlocking("sounds/kennedy2.wav");
                } else if (line != null && line.equals("Line 5")) {
                    playBlocking("sounds/kennedy5.wav");
                }
                break;
            case "Sheppard-Yonge Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/sheppardyonge_1.wav");
                } else if (line != null && line.equals("Line 4")) {
                    playBlocking("sounds/sheppard_yonge_4.wav");
                }
                break;
            case "Spadina Station":
                if (line != null && line.equals("Line 1")) {
                    playBlocking("sounds/spadina1.wav");
                } else if (line != null && line.equals("Line 2")) {
                    playBlocking("sounds/spadina2.wav");
                }
                break;
            case "TMU Station":
                playBlocking("sounds/tmu.wav");
                break;
            case "Mount Dennis Station":
                playBlocking("sounds/mountdennis.wav");
                break;
            case "Vaughan Metropolitan Centre Station":
                playBlocking("sounds/vmc.wav");
                break;
            case "Broadview Station":
                playBlocking("sounds/broadview.wav");
                break;
            case "Dundas West Station":
                playBlocking("sounds/dundaswest.wav");
                break;
            case "Kipling Station":
                playBlocking("sounds/kipling.wav");
                break;
            case "Don Mills Station":
                playBlocking("sounds/donmills.wav");
                break;
            case "Main Street Station":
                playBlocking("sounds/main_street.wav");
                break;
            case "Pape Station":
                playBlocking("sounds/pape.wav");
                break;
            case "Museum Station":
                playBlocking("sounds/museum.wav");
                break;
            case "Humber College Station":
                playBlocking("sounds/humbercollege.wav");
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


            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playLine5RestoredAnnouncement() {
    playBlocking("sounds/line5_restored.wav");
}
}