package sample;

/*import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class GameGridTest {

    GameGrid gameGrid;
    @Before
    public void setUp() throws Exception {
        gameGrid=new GameGrid(50, 60);
    }



    @Test
    public void testTranslatePoint() {
        Point point=new Point(10, 10);

        assertEquals(gameGrid.translatePoint(point, new Point(-1, 0)), new Point(9, 10));
    }

    @Test
    public void testGetDimension() {
        assertEquals(gameGrid.getDimension(), new Dimension(50, 60));
    }

    @Test
    public void testGetTargetFromSource() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 60; j++) {
                gameGrid.putGameObjectAt(GameObject.WALL, i, j);
            }
        }
        assertTrue(gameGrid.getGameObjectAt(10, 10)==GameObject.WALL);
    }

    @Test
    public void testGetGameObjectAtIntInt() {
        assertTrue(gameGrid.getGameObjectAt(10, 10)==null);
    }



    @Test
    public void testRemoveGameObjectAt() {
        gameGrid.putGameObjectAt(GameObject.CRATE, 10, 10);
        assertTrue(gameGrid.getGameObjectAt(10, 10)==GameObject.CRATE);
        gameGrid.removeGameObjectAt(new Point(10, 10));
        assertTrue(gameGrid.getGameObjectAt(10, 10)==null);
    }

    @Test
    public void testPutGameObjectAtGameObjectIntInt() {
        gameGrid.putGameObjectAt(GameObject.CRATE, 10, 10);
        assertTrue(gameGrid.getGameObjectAt(10, 10)==GameObject.CRATE);
    }

    @Test
    public void testPutGameObjectAtGameObjectPoint() {
        gameGrid.putGameObjectAt(GameObject.CRATE, new Point(10, 10));
        assertTrue(gameGrid.getGameObjectAt(new Point(10, 10))==GameObject.CRATE);
    }



}*/
