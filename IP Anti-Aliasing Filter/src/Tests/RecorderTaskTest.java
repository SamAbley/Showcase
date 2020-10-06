package Tests;

import MovieMaker.RecorderTask;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

// Class to test the run method in the RecorderTask class

public class RecorderTaskTest {



    @Test
    public void TestRun() {

       RecorderTask test = mock(RecorderTask.class);

        doCallRealMethod().when(test).run();

        verify(test, atLeastOnce());


    }


}
