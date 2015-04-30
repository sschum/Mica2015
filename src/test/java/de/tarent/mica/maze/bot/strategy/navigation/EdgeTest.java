package de.tarent.mica.maze.bot.strategy.navigation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tarent.mica.maze.model.Coord;

public class EdgeTest {

	@Test
	public void testWeight(){
		Edge e1 = new Edge(new Route(new Coord(3, -1), new Coord(1, -2)), new Coord(2, -1), new Coord(1, -1));
		Edge e2 = new Edge(new Route(new Coord(1, -2), new Coord(3, -1)), new Coord(1, -1), new Coord(2, -1));

		assertEquals(e1.getWeight(), e2.getWeight());
	}
}
