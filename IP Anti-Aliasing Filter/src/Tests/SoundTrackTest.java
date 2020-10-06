package Tests;


import SoundTrack.SoundTrack;
//import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Test;


//import javax.sound.sampled.Clip;

import static org.mockito.Mockito.*;

// Class to test the methods in SoundTrack

public class SoundTrackTest {

    @Test
    public void The_SoundTrack_Test_Runner() {
        SoundTrack the_test_for_soundtrack = mock(SoundTrack.class);

        Mockito.doCallRealMethod().when(the_test_for_soundtrack).initSoundTrack();
        Mockito.doCallRealMethod().when(the_test_for_soundtrack).playSoundTrack();
        Mockito.doCallRealMethod().when(the_test_for_soundtrack).stopSoundTrack();
        Mockito.doCallRealMethod().when(the_test_for_soundtrack).getFile();

        verify(the_test_for_soundtrack, atLeastOnce());
    }

    // Test for the method which checks the file path

    @Test
    public void checkFilePath(){
        String testFilePath = "Ffmpeg/soundtracks/soundtrack2.wav";
        SoundTrack soundTrack = new SoundTrack(testFilePath);
        soundTrack.getFilePath().equals(testFilePath);
    }

    // Test for the method which gets the file name

    @Test
    public void getFileNameTest() {
        SoundTrack emp = mock(SoundTrack.class);
        emp.getFileName();
        when(emp.getFileName()).thenReturn("soundtrack2.wav");
        verify(emp, atLeastOnce()).getFileName();
    }


}