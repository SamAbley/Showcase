package Tests;

import Framework.GUI;
import MovieMaker.MainRecorder;
import org.junit.Assert;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Test;

import static org.mockito.Mockito.*;

// Class to test the methods in MainRecorder

public class MainRecorderTest {

    private ActionEvent ActionEvent;

    @Test
    public void The_Main_Recorder_Test_Runner() {
        MainRecorder the_test = Mockito.mock(MainRecorder.class);
        String sound = "Ffmpeg/soundtracks/sound2";

        Mockito.doCallRealMethod().when(the_test).actionPerformed(ActionEvent);
        Mockito.doCallRealMethod().when(the_test).initScreenRecorderObjects();
        Mockito.doCallRealMethod().when(the_test).scheduleTimerTasks();
        Mockito.doCallRealMethod().when(the_test).stopScreenRecording();
        Mockito.doCallRealMethod().when(the_test).combine(sound);
        verify(the_test, atLeastOnce());
    }

    // Testing the mp4 output process
    @Test
    public void The_Main_Recorder_Output_Test() throws InterruptedException {
        GUI g = new GUI();
        MainRecorder test = new MainRecorder(g);
        File output = new File("Ffmpeg/final_output.mp4");

        test.actionPerformed(ActionEvent);
        Thread.sleep(2000);
        test.actionPerformed(ActionEvent);
        g.frame.dispose();
        Assert.assertEquals(output, test.getOutput());
    }

    @Test
    public void initScreenRecorderObjectTest (){
        File output = new File("Ffmpeg/final_output.mp4");
        output.delete();
    }
}
