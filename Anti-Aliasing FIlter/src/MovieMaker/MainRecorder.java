package MovieMaker;

import Framework.GUI;
import SoundTrack.SoundTrack;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainRecorder implements ActionListener {

    static boolean stop = false;
    private static Timer timerRecord;
    private final GUI currGUI;
    private SoundTrack soundTrack;
    private JButton start;
    private AWTSequenceEncoder encoder;
    private RecorderTask recorderTask;
    private boolean isRecording = false;
    private File video, output;


    public MainRecorder(GUI gui) {
        this.currGUI = gui;
        this.start = gui.start;

    }

    private void blurImageVideo() {
        int c = currGUI.getBlurStrength().getValue() * 2 + 1;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stop = true;
                timer.cancel();
            }
        }, (currGUI.getBlurStrength().getValue() * 1000) + 1000);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String opencvpath = (System.getProperty("user.dir") + "\\JDK\\opencv_java340.dll");
                    System.load(opencvpath);
                    Mat src = Imgcodecs.imread(currGUI.getImagePath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
                    Mat imageResultMatrix = new Mat(src.rows(), src.cols(), src.type());
                    Imgproc.GaussianBlur(src, imageResultMatrix, new Size(c, c), 0);
                    Imgcodecs.imwrite("assets/Result/" + currGUI.getImageName(), imageResultMatrix);
                    Image fileImage = ImageIO.read(new File("assets/Result/" + currGUI.getImageName()));
                    currGUI.setPicture(fileImage);
                    currGUI.refresh();
                } catch (Exception e) {
                    System.out.println("error:" + e.getLocalizedMessage());
                }
            }
        }, 1000, 1000);


    }

    public File getOutput() {
        return output;
    }

    public void initScreenRecorderObjects() {

        video = new File("Ffmpeg/video_output.mp4");
        output = new File("Ffmpeg/final_output.mp4");
        if (output.delete()) {
            System.out.println("output deleted");
        }

        try {
            encoder = AWTSequenceEncoder.createSequenceEncoder(video, 20);
        } catch (IOException ignored) {

        }

    }

    public void scheduleTimerTasks() {
        int delay = 1000 / 24;
        timerRecord = new Timer("Thread TimerRecord");
        recorderTask = new RecorderTask(encoder, currGUI);
        timerRecord.scheduleAtFixedRate(recorderTask, 0, delay);
    }

    public void stopScreenRecording() {

        timerRecord.cancel();
        timerRecord.purge();
        recorderTask.cancel();

        try {
            encoder.finish();
            System.out.println("Recording finish");
        } catch (IOException ignored) {
        }

    }

    public void combine(String sound) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("cmd /c start cmd.exe /K \"cd Ffmpeg && ffmpeg -i video_output.mp4 -i " + sound + " -c:v copy -c:a aac -shortest -strict  experimental -map 0:v:0 -map 1:a:0 final_output.mp4 && exit\"");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (video.delete()) {
                        System.out.println("video deleted");
                    }
                }
            }, 10000);


        } catch (Exception ignored) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currGUI.isCheckboxValue()) {
            if (isRecording) {
                isRecording = false;
                stopScreenRecording();

                if (!soundTrack.getFile().equals("None")) {
                    soundTrack.stopSoundTrack();
                    combine(soundTrack.getFileName());
                } else if (
                        video.renameTo(output)) {
                    System.out.println("video renamed");
                }

            } else {
                stop = false;
                isRecording = true;
                start.setEnabled(false);
                initScreenRecorderObjects();
                scheduleTimerTasks();
                blurImageVideo();
                soundTrack = new SoundTrack(this.currGUI.getSoundTrackFile());
                if (!soundTrack.getFile().equals("None"))
                    soundTrack.playSoundTrack();

            }
        } else {
            start.setEnabled(false);
            blurImage();
        }
    }

    private void blurImage() {
        int c;
        if(currGUI.getBlurStrength().getValue() % 2 == 1){
            c = currGUI.getBlurStrength().getValue();
        }else
            c = currGUI.getBlurStrength().getValue()+1;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stop = true;
                timer.cancel();
            }
        },(currGUI.getBlurStrength().getValue()*1000)+1000);
        int finalC = c;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String opencvpath = (System.getProperty("user.dir") + "\\JDK\\opencv_java340.dll");
                    System.load(opencvpath);
                    Mat src = Imgcodecs.imread(currGUI.getImagePath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
                    Mat imageResultMatrix = new Mat(src.rows(), src.cols(), src.type());
                    Imgproc.GaussianBlur(src, imageResultMatrix, new Size(finalC, finalC), 0); // change to 11
                    Imgcodecs.imwrite("assets/Result/" + currGUI.getImageName(), imageResultMatrix);
                    Image fileImage = ImageIO.read(new File("assets/Result/" + currGUI.getImageName()));
                    currGUI.setPicture(fileImage);
                    currGUI.setImagePath("assets/Result/" + currGUI.getImageName());


                    currGUI.refresh();
                } catch (Exception e) {
                    System.out.println("error:" + e.getLocalizedMessage());
                }
            }
        },1000,1000);






    }
}
