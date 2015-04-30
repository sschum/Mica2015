package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.List;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Maze;

/**
 * This class is responsible for finding a route from a start
 * position to a destination.
 *
 * @author rainu
 */
public class PathFinder {
	private final Maze maze;
	private final Graph graph;

	public PathFinder(Maze maze) {
		this.maze = maze;
		this.graph = new Graph(maze);
	}

	public List<Coord> getRoute(final Coord start, final Coord dest){
		return graph.getShortestWay(start, dest);
	}
}
