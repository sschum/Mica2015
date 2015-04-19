package de.tarent.mica.maze.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;

public class WorldTest {

	@Test
	public void testGetPlayerCoord(){
		final World world = generateTestWorld();

		assertEquals(
				new Coord(0, 1),
				world.getPlayerCoord());
	}

	@Test
	public void testToString(){
		final World world = generateTestWorld();

		assertEquals(
				world.toString(),
				"####\n" +
				"#* #\n" +
				"# ^#\n" +
				"####");
	}

	/**
	 * <pre>
	 * ####
	 * #* #
	 * # ^#
	 * ####
	 * </pre>
	 */
	private World generateTestWorld() {
		final World world = new World(
			new Field(new Coord(-2, -1), Type.WALL), new Field(new Coord(-1, -1), Type.WALL), new Field(new Coord(0, -1), Type.WALL), new Field(new Coord(1, -1), Type.WALL),
			new Field(new Coord(-2, 0), Type.WALL), new Field(new Coord(-1, 0), Type.BUTTON), new Field(new Coord(0, 0), Type.WAY), new Field(new Coord(1, 0), Type.WALL),
			new Field(new Coord(-2, 1), Type.WALL), new Field(new Coord(-1, 1), Type.WAY), new Field(new Coord(0, 1), Type.PLAYER_NORTH), new Field(new Coord(1, 1), Type.WALL),
			new Field(new Coord(-2, 2), Type.WALL), new Field(new Coord(-1, 2), Type.WALL), new Field(new Coord(0, 2), Type.WALL), new Field(new Coord(1, 2), Type.WALL)
		);
		return world;
	}

}
