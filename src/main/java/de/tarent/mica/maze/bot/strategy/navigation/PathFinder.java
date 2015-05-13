package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.WorldDimension;

/**
 * This class is responsible for finding a route from a start
 * position to a destination.
 *
 * @author rainu
 */
public class PathFinder {
	private static final Logger log = Logger.getLogger(PathFinder.class);

	private final Maze maze;

	private static Map<String, Graph> preCalculatedGraph = new HashMap<String, Graph>();

	public PathFinder(Maze maze) {
		this.maze = maze;
	}

	public List<Coord> getRoute(final Coord start, final Coord dest){
		if(isPoorRoom(start, dest)){
			log.debug("It's a poor room. Use the recursive finder.");
			RecursiveFinder recursiveFinder = new RecursiveFinder(maze, start, dest, TimeUnit.SECONDS, 1);
			List<List<Coord>> routes = recursiveFinder.getRoutes();

			if(routes != null && !routes.isEmpty()){
				return routes.get(0);
			}

			return Collections.EMPTY_LIST;
		}

		Graph graph = getGraph();
		return graph.getShortestWay(start, dest);
	}

	private Graph getGraph() {
		final String mazeHash = getMazeHash(maze);

		if(preCalculatedGraph.containsKey(mazeHash)){
			return preCalculatedGraph.get(mazeHash);
		}else{
			preCalculatedGraph.clear();
		}

		Graph graph = new Graph(maze);
		preCalculatedGraph.put(mazeHash, graph);

		return graph;
	}

	private boolean isPoorRoom(final Coord start, final Coord dest) {
		final WorldDimension dim = maze.getDimension();
		final int minX = dim.getMinX();
		final int maxX = dim.getMaxX();
		final int minY = dim.getMinY();
		final int maxY = dim.getMaxY();

		Map<Type, Integer> typeCounter = new HashMap<>();
		for(Type t : Type.values()){
			typeCounter.put(t, 0);
		}

		for(int x=minX; x <= maxX; x++){
			for(int y=minY; y <= maxY; y++){
				final Coord coord = new Coord(x, y);
				final Field field = maze.getField(coord);

				if(field != null){
					for(Type t : field.getTypes()){
						typeCounter.put(t, typeCounter.get(t) + 1);
					}
				}else{
					typeCounter.put(Type.UNKNOWN, typeCounter.get(Type.UNKNOWN) + 1);
				}
			}
		}

		final int total = dim.getHeight() * dim.getWidth();
		final int impassable = typeCounter.get(Type.UNKNOWN) + typeCounter.get(Type.WALL);
		final double impassableInPercent = (impassable * 100) / total;

		return impassableInPercent <= 25;
	}

	private static String getMazeHash(Maze maze){
		final WorldDimension dim = maze.getDimension();

		return dim.getHeight() + "_" + dim.getWidth() + "_" + maze.getWayFields().hashCode();
	}
}
