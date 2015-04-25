package de.tarent.mica.maze.bot.strategy.navigation;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.test.MazeBuilder;

public class GraphTest {

	private Maze testMaze() {
		final Maze m = MazeBuilder.fromString(
				"######\n" +
				"#    ###\n" +
				"#### # #\n" +
				"# 1 0  #\n" +
				"#  # ###\n" +
				"######\n");
		return m;
	}

	@Test
	public void detectCrossings(){
		final Maze m = MazeBuilder.testMaze();

		System.out.println(m);

		final Maze m2 = new Maze();

		System.out.println(Graph.detectCrossings(m));
		for(Coord c : Graph.detectCrossings(m)){
			m2.putField(c, Type.WAY);
		}

		System.out.println(m2);
	}

	@Test
	public void detectNeighborCrossings(){
		final Maze m = testMaze();

		/*
		 *
		 * ######
		 * #<<<<###
		 * ####^#^#
		 * # <<*>^#
		 * #  #v###
		 * ######
		 *
		 */
		Coord start = m.getButtonField(Type.BUTTON0).getCoord();
		Map<Coord, List<Coord>> crossings = Graph.detectNeighborCrossings(m, start);
		assertTrue(4 == crossings.size());
		assertTrue(crossings.containsKey(new Coord(4, -4)));
		assertTrue(crossings.containsKey(new Coord(2, -3)));
		assertTrue(crossings.containsKey(new Coord(1, -1)));
		assertTrue(crossings.containsKey(new Coord(6, -2)));

		/*
		 *
		 * ######
		 * #    ###
		 * #### # #
		 * #<*>>  #
		 * #^<# ###
		 * ######
		 *
		 */
		start = m.getButtonField(Type.BUTTON1).getCoord();
		crossings = Graph.detectNeighborCrossings(m, start);
		assertTrue(3 == crossings.size());
		assertTrue(crossings.containsKey(new Coord(2, -4)));
		assertTrue(crossings.containsKey(new Coord(4, -3)));
		assertTrue(crossings.containsKey(new Coord(1, -3)));
	}

	@Test
	public void getShortestWay_CrossingToCrossing(){
		/*
		 *
		 * ######
		 * #*   ###
		 * #### #*#
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*#*#
		 * #   ***#
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(6, -2), new Coord(1, -1));

		assertTrue(9 == way.size());
		assertEquals(new Coord(6, -2), way.get(0));
		assertEquals(new Coord(6, -3), way.get(1));
		assertEquals(new Coord(5, -3), way.get(2));
		assertEquals(new Coord(4, -3), way.get(3));
		assertEquals(new Coord(4, -2), way.get(4));
		assertEquals(new Coord(4, -1), way.get(5));
		assertEquals(new Coord(3, -1), way.get(6));
		assertEquals(new Coord(2, -1), way.get(7));
		assertEquals(new Coord(1, -1), way.get(8));
	}

	@Test
	public void getShortestWay_CrossingToEdge(){
		/*
		 *
		 * ######
		 * #*   ###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(1, -1), new Coord(4, -2));

		assertTrue(5 == way.size());
		assertEquals(new Coord(1, -1), way.get(0));
		assertEquals(new Coord(2, -1), way.get(1));
		assertEquals(new Coord(3, -1), way.get(2));
		assertEquals(new Coord(4, -1), way.get(3));
		assertEquals(new Coord(4, -2), way.get(4));
	}

	@Test
	public void getShortestWay_EdgeToCrossing(){
		/*
		 *
		 * ######
		 * #*   ###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(4, -2), new Coord(1, -1));

		assertTrue(5 == way.size());
		assertEquals(new Coord(4, -2), way.get(0));
		assertEquals(new Coord(4, -1), way.get(1));
		assertEquals(new Coord(3, -1), way.get(2));
		assertEquals(new Coord(2, -1), way.get(3));
		assertEquals(new Coord(1, -1), way.get(4));
	}

	@Test
	public void getShortestWay_EdgeToEdge(){
		/*
		 *
		 * ######
		 * # *  ###
		 * #### # #
		 * #     *#
		 * #  # ###
		 * ######
		 *
		 * ######
		 * # ***###
		 * ####*# #
		 * #   ***#
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(2, -1), new Coord(6, -3));

		assertTrue(7 == way.size());
		assertEquals(new Coord(2, -1), way.get(0));
		assertEquals(new Coord(3, -1), way.get(1));
		assertEquals(new Coord(4, -1), way.get(2));
		assertEquals(new Coord(4, -2), way.get(3));
		assertEquals(new Coord(4, -3), way.get(4));
		assertEquals(new Coord(5, -3), way.get(5));
		assertEquals(new Coord(6, -3), way.get(6));
	}
}
