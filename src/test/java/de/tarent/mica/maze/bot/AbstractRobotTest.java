package de.tarent.mica.maze.bot;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Drop;
import de.tarent.mica.maze.bot.action.Get;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.Push;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;

public class AbstractRobotTest {

	private AbstractRobot toTest;
	private AbstractRobot toTestSpy;

	@Before
	public void setup(){
		toTest = new TestRobot();
		toTestSpy = spy(toTest);
	}

	private Action getLastAction() {
		return toTest.history.get(toTest.history.size() - 1);
	}

	@Test
	public void testStartGame(){
		toTestSpy.handleEvent(null);

		verify(toTestSpy).handleStartEvent();
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testGameStarted(){
		toTest.history.add(new StartGame(""));
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleGameStarted(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testWalked(){
		toTest.history.add(new Walk());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleWalked(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testTurnedLeft(){
		toTest.history.add(new TurnLeft());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleTurnedLeft(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testTurnedRight(){
		toTest.history.add(new TurnRight());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleTurnedRight(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testPushed(){
		toTest.history.add(new Push());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handlePushed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testGot(){
		toTest.history.add(new Get());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleGot(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testDroped(){
		toTest.history.add(new Drop());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleDroped(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testLooked(){
		toTest.history.add(new Look());
		final ActionSuccess event = new ActionSuccess();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleLooked(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testGameStartFail(){
		toTest.history.add(new StartGame(""));
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleGameStartFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testWalkFail(){
		toTest.history.add(new Walk());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleWalkFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testTurnLeftFail(){
		toTest.history.add(new TurnLeft());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleTurnLeftFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testTurnRightFail(){
		toTest.history.add(new TurnRight());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleTurnRightFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testPushFail(){
		toTest.history.add(new Push());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handlePushFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testGetFail(){
		toTest.history.add(new Get());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleGetFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testDropFail(){
		toTest.history.add(new Drop());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleDropFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}

	@Test
	public void testLookFail(){
		toTest.history.add(new Look());
		final ActionFail event = new ActionFail();

		toTestSpy.handleEvent(event);

		verify(toTestSpy).handleLookFailed(same(event));
		assertSame(TestRobot.returnAction, getLastAction());
	}
}
