/*package sample;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class LevelTest {

    Level level;
    StartMeUp startMeUp;
    @Before
    public void setUp() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("sample/SampleGame.skb");
        startMeUp=new StartMeUp(in, true);
        level=startMeUp.getCurrentLevel();
    }



    @Test
    public void testIsComplete() {
        assertFalse(level.isComplete());
    }



    @Test
    public void testGetIndex() {
        assertTrue(level.getIndex()==1);
    }

    @Test
    public void testGetKeeperPosition() {

        assertEquals(level.getKeeperPosition(), new Point(18,10));
    }



    @Test
    public void testGetObjectAt() {
        assertTrue(level.getObjectAt(new Point(0,0))==GameObject.WALL);
    }


    @Test
    public void testMoveGameObjectTo() {
        startMeUp.move(new Point(0, -1));
        assertEquals(level.getKeeperPosition(), new Point(18,9));
    }

    @Test
    public void testLogMove() {
        startMeUp.move(new Point(0, -1));
        assertEquals(level.getKeeperPosition(), new Point(18,9));
        startMeUp.undo();
        assertEquals(level.getKeeperPosition(), new Point(18,10));
    }



}*/
