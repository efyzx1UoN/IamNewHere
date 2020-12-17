package sample;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import sample.Game.GameObject;
import sample.Level;
import sample.StartMeUp;

/**
 *  @author Zihui xu - Modified
 *   @version 1.0
 *   Class is testing Level Class Methods
 */
public class LevelTest {

	Level m_level;
	StartMeUp m_startMeUp;

	/**
	 * Junit Test level get source
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		InputStream in = getClass().getClassLoader().
				getResourceAsStream("SampleGame.skb");
		m_startMeUp=new StartMeUp(in, true);
		m_level=m_startMeUp.getCurrentLevel();
	}


	/**
	 * Junit Test isComplete
	 */
	@Test
	public void testIsComplete() {
		assertFalse(m_level.isComplete());
	}


	/**
	 * Junit Test GetIndex
	 */
	@Test
	public void testGetIndex() {
		assertTrue(m_level.getIndex()==1);
	}

	/**
	 * Junit Test GetKeeperPosition
	 */
	@Test
	public void testGetKeeperPosition() {
		
		assertEquals(m_level.getKeeperPosition(), new Point(18,10));
	}


	/**
	 * Junit Test GetObjectAt
	 */
	@Test
	public void testGetObjectAt() {
		assertTrue(m_level.getObjectAt(
				new Point(0,0))== GameObject.WALL);
	}

	/**
	 * Junit Test MoveGameObjectTo
	 */
	@Test
	public void testMoveGameObjectTo() {
		m_startMeUp.move(new Point(0, -1));
		assertEquals(m_level.getKeeperPosition(), new Point(18,9));
	}

	/**
	 * Junit Test LogMove
	 */
	@Test
	public void testLogMove() {
		m_startMeUp.move(new Point(0, -1));
		assertEquals(m_level.getKeeperPosition(), new Point(18,9));
		m_startMeUp.undo();
		assertEquals(m_level.getKeeperPosition(), new Point(18,10));
	}

	

}
