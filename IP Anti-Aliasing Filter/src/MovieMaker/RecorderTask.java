package MovieMaker;

import Framework.GUI;
import org.jcodec.api.awt.AWTSequenceEncoder;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class RecorderTask extends TimerTask {

    private AWTSequenceEncoder encoder;
    private Robot robot;
    private Rectangle screenDimension;
    private final GUI app;
    private JFrame frame;



    RecorderTask(AWTSequenceEncoder sequenceEncoder, GUI app) {
        this.app = app;
        frame = app.frame;
        encoder = sequenceEncoder;
        screenDimension = new Rectangle();
        screenDimension.height = frame.getHeight() -134;
        screenDimension.width = frame.getWidth() - 8;



        try {
            robot = new Robot();
        } catch (AWTException ignored) {
        }

    }

    @Override
    public void run() {

        System.out.println("Recording...");

        screenDimension.x = frame.getX() + 4;
        screenDimension.y = frame.getY() + 88;
        BufferedImage image = robot.createScreenCapture(screenDimension);
        try {
                encoder.encodeImage(image);
        } catch (Exception ignored) {
        }
        if (MainRecorder.stop){
            app.start.setEnabled(true);
            app.start.doClick();
            app.download.setEnabled(true);
        }

    }

}
