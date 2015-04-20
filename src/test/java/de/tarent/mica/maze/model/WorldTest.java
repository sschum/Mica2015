package de.tarent.mica.maze.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorldTest {

	@Test
	public void testPushButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH, Type.BUTTON0)));
		world.pushButton();

		assertTrue(0 == world.getLastPushedButton());
	}

	@Test
	public void testPushButton_notOnButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH)));
		world.pushButton();

		assertNull(world.getLastPushedButton());
	}

	@Test
	public void testPushButton_wrongButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH, Type.BUTTON1)));
		world.pushButton();

		assertNull(world.getLastPushedButton());
	}

	@Test
	public void testPutButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH, Type.BUTTON0)));
		world.putButton();

		assertTrue(0 == world.getInventarButton());
		assertNull(world.getMaze().getPlayerField().getButtonType());
	}

	@Test
	public void testPutButton_notOnButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH)));
		world.putButton();

		assertNull(world.getInventarButton());
	}

	@Test
	public void testDropButton(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH)));
		world.inventarButton = 0;

		world.dropButton();
		assertNull(world.getInventarButton());
		assertEquals(Type.BUTTON0, world.getMaze().getPlayerField().getButtonType());
	}

	@Test
	public void testDropButton_noInventory(){
		World world = new World(new Maze(new Field(new Coord(0, 0), Type.PLAYER_NORTH)));

		world.dropButton();
		assertNull(world.getInventarButton());
		assertNull(world.getMaze().getPlayerField().getButtonType());
	}
}
