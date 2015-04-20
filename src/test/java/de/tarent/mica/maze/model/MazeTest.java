package de.tarent.mica.maze.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.Maze;

public class MazeTest {

	@Test
	public void testGetPlayerField(){
		final Maze world = generateTestMaze();

		final Field playerField = world.getPlayerField();

		assertEquals(
				new Coord(0, 0),
				playerField.getCoord());
		assertEquals(
				Type.PLAYER_NORTH,
				playerField.getPlayerType());
	}

	@Test
	public void testToString(){
		final Maze world = generateTestMaze();

		assertEquals(
				world.toString(),
				"####\n" +
				"#0 #\n" +
				"# ^#\n" +
				"####");
	}

	@Test
	public void testToString_withUnknown(){
		final Maze world = generateTestMaze();
		world.putField(new Field(new Coord(0, -2), Type.WALL));

		assertEquals(
				world.toString(),
				"####\n" +
				"#0 #\n" +
				"# ^#\n" +
				"####\n" +
				"??#?");
	}

	/**
	 * <pre>
	 * ###|#
	 * #0 |#
	 * # ^|#
	 * -----
	 * ###|#
	 * </pre>
	 */
	private Maze generateTestMaze() {
		final Maze world = new Maze(
			new Field(new Coord(-2, 2), Type.WALL), new Field(new Coord(-1, 2), Type.WALL), new Field(new Coord(0, 2), Type.WALL), new Field(new Coord(1, 2), Type.WALL),
			new Field(new Coord(-2, 1), Type.WALL), new Field(new Coord(-1, 1), Type.BUTTON0), new Field(new Coord(0, 1), Type.WAY), new Field(new Coord(1, 1), Type.WALL),
			new Field(new Coord(-2, 0), Type.WALL), new Field(new Coord(-1, 0), Type.WAY), new Field(new Coord(0, 0), Type.PLAYER_NORTH), new Field(new Coord(1, 0), Type.WALL),
			new Field(new Coord(-2, -1), Type.WALL), new Field(new Coord(-1, -1), Type.WALL), new Field(new Coord(0, -1), Type.WALL), new Field(new Coord(1, -1), Type.WALL)
		);

		return world;
	}

}
