package SoundTrack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundTrack {
    private String fileName;
    private String filePath;
    private String file;
    private Clip clip;

    public SoundTrack(String filePath) {
        this.file = filePath;
        this.filePath = "Ffmpeg/soundtracks/" + filePath;
        this.fileName = "soundtracks/" + filePath;
    }

    public void initSoundTrack() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            System.out.println("Error occurred while initializing soundtrack.");
        }
    }

    public void playSoundTrack() {
        initSoundTrack();
        clip.start();
    }

    public void stopSoundTrack() {
        clip.stop();
        clip.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFile() {
        return file;
    }
}
