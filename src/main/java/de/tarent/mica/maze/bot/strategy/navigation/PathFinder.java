package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	public List<List<Coord>> getRoutes(final Coord start, final List<Coord> dest){
		final List<List<Coord>> routes = Collections.synchronizedList(new ArrayList<List<Coord>>(dest.size()));

		final ExecutorService tp = getThreadPool();
		List<Callable<Object>> todos = new LinkedList<>();

		for(Coord c : dest){
			final Coord coord = c;
			todos.add(Executors.callable(new Runnable() {
				@Override
				public void run() {
					List<Coord> path = getRoute(start, coord);
					routes.add(path);
				}
			}));
		}

		try {
			tp.invokeAll(todos);
		} catch (InterruptedException e) {}

		return routes;
	}

	private ExecutorService threadPool;
	private ExecutorService getThreadPool() {
		if(threadPool == null){
			threadPool = Executors.newCachedThreadPool();
		}

		return threadPool;
	}

	public List<Coord> getRoute(final Coord start, final Coord dest){
		final Graph graph = getGraph();

		if(isPoorRoom(graph)){
			log.debug("It's a poor room. Use the recursive finder.");
			RecursiveFinder recursiveFinder = new RecursiveFinder(maze, start, dest, TimeUnit.MILLISECONDS, 100);
			List<List<Coord>> routes = recursiveFinder.getRoutes();

			if(routes != null && !routes.isEmpty()){
				return routes.get(0);
			}

			return Collections.EMPTY_LIST;
		}

		return graph.getShortestWay(start, dest);
	}

	private synchronized Graph getGraph() {
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

	private boolean isPoorRoom(Graph graph) {
		int zeroWeightEdges = 0;
		for(Edge edge : graph.nodes.values()){
			if(edge.getWeight() == 1){
				zeroWeightEdges++;
			}
		}

		int zeroWeightInPercent = (100 * zeroWeightEdges) / graph.nodes.size();

		return zeroWeightInPercent >= 75;
	}

	private static String getMazeHash(Maze maze){
		final WorldDimension dim = maze.getDimension();

		return dim.getHeight() + "_" + dim.getWidth() + "_" + maze.getWayFields().hashCode();
	}
}
