package Assignment;

import org.junit.Before;
import org.junit.Test;

public class actionTest {

    notePad note;

    @Before
    public void initTest(){
        note = new notePad();
        note.init();
    }

    @Test
    public void testInit(){
        action.main(null);
    }

    @Test
    public void testOpen(){
        note.openListen();
    }

    @Test
    public void testSave(){
        note.saveListen();
    }

    @Test
    public void testSearch(){
        note.searchListen();
    }

    @Test
    public void testCopy(){
        note.copyListen();
    }

}
