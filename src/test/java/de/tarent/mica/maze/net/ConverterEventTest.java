package de.tarent.mica.maze.net;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.ButtonActionSuccess;
import de.tarent.mica.maze.bot.event.Event;
import de.tarent.mica.maze.bot.event.LookActionSuccess;
import de.tarent.mica.maze.bot.event.LookActionSuccess.Field;

public class ConverterEventTest {

	@Test
	public void testActionSuccess() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\" }");

		assertTrue(result instanceof ActionSuccess);
	}

	@Test
	public void testActionSuccess_withinAMessage() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\", \"message\" : \"hello\" }");

		assertTrue(result instanceof ActionSuccess);
		assertEquals("hello", result.getMessage());
	}

	@Test
	public void testButtonActionSuccess() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\", \"button\" : \"1\" }");

		assertTrue(result instanceof ButtonActionSuccess);
		assertTrue(1 == ((ButtonActionSuccess)result).getButtonNumber());
	}

	@Test
	public void testButtonActionSuccess_withinAMessage() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\", \"button\" : \"1\", \"message\" : \"hello\" }");

		assertTrue(result instanceof ButtonActionSuccess);
		assertTrue(1 == ((ButtonActionSuccess)result).getButtonNumber());
		assertEquals("hello", result.getMessage());
	}

	@Test
	public void testButtonActionSuccess_noNumber() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\", \"button\" : \"\" }");

		assertTrue(result instanceof ButtonActionSuccess);
		assertNull(((ButtonActionSuccess)result).getButtonNumber());
	}

	@Test
	public void testButtonActionSuccess_noNumber_withinAMessage() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"ok\", \"button\" : \"\", \"message\" : \"hello\" }");

		assertTrue(result instanceof ButtonActionSuccess);
		assertNull(((ButtonActionSuccess)result).getButtonNumber());
		assertEquals("hello", result.getMessage());
	}

	@Test
	public void testActionFail() throws IOException{
		final Event result = Converter.getInstance().convertToEvent(
				"{ \"result\" : \"fail\", \"message\" : \"error description\" }");

		assertTrue(result instanceof ActionFail);
		assertEquals("error description", ((ActionFail)result).getMessage());
	}

	@Test
	public void testLookActionSuccess() throws IOException {
		final Event result = Converter.getInstance().convertToEvent(
			"{ \"result\" : \"ok\", \"1\" : \" \",  \"2\" : \"l1r\", \"3\" : \"l\" }");

		assertTrue(result instanceof LookActionSuccess);
		assertTrue(4 == ((LookActionSuccess)result).getFields().size());

		Field field = ((LookActionSuccess)result).getFields().get(0);
		assertFalse(field.isWall());
		assertFalse(field.hasLeftBranch());
		assertFalse(field.hasRightBranch());
		assertNull(field.getButtonNumber());

		field = ((LookActionSuccess)result).getFields().get(1);
		assertFalse(field.isWall());
		assertTrue(field.hasLeftBranch());
		assertTrue(field.hasRightBranch());
		assertTrue(1 == field.getButtonNumber());

		field = ((LookActionSuccess)result).getFields().get(2);
		assertFalse(field.isWall());
		assertTrue(field.hasLeftBranch());
		assertFalse(field.hasRightBranch());
		assertNull(field.getButtonNumber());

		field = ((LookActionSuccess)result).getFields().get(3);
		assertTrue(field.isWall());
		assertFalse(field.hasLeftBranch());
		assertFalse(field.hasRightBranch());
		assertNull(field.getButtonNumber());
	}
}
