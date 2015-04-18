package de.tarent.mica.maze.net;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.tarent.mica.maze.bot.action.Drop;
import de.tarent.mica.maze.bot.action.Get;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.Push;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;

public class ConverterActionTest {

	@Test
	public void testTurnLeft() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new TurnLeft());

		assertEquals("{\"action\":\"left\"}", result);
	}

	@Test
	public void testTurnRight() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new TurnRight());

		assertEquals("{\"action\":\"right\"}", result);
	}

	@Test
	public void testWalk() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new Walk());

		assertEquals("{\"action\":\"walk\"}", result);
	}

	@Test
	public void testLook() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new Look());

		assertEquals("{\"action\":\"look\"}", result);
	}

	@Test
	public void testGet() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new Get());

		assertEquals("{\"action\":\"get\"}", result);
	}

	@Test
	public void testDrop() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new Drop());

		assertEquals("{\"action\":\"drop\"}", result);
	}

	@Test
	public void testPush() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new Push());

		assertEquals("{\"action\":\"push\"}", result);
	}

	@Test
	public void testStartGame() throws IOException{
		final String result = Converter.getInstance().convertToMessage(new StartGame("rainu"));

		assertEquals("{\"name\":\"rainu\",\"maze\":\"\"}", result);
	}
}
