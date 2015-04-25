package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Direction;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;

/**
 * Ein Graph repräsentiert nur Knoten. Jeder Knoten ist eine
 * Kreuzung oder Sackgasse im Labyrinth. Die Wege zwischen den
 * Kreuzungen (Knoten) werden in den Kanten ({@link Edge}) gespeichert.
 *
 * @author rainu
 */
public class Graph {
	Map<Route, Edge> nodes = new HashMap<Route, Edge>();

	public Graph(Maze maze){
		analyse(maze);
	}

	private void analyse(Maze maze) {
		Collection<Coord> crossings = detectCrossings(maze);
		for(Coord crossing : crossings){
			Map<Coord, List<Coord>> neighbors = detectNeighborCrossings(maze, crossing);
			fillNodes(neighbors);
		}
	}

	private void fillNodes(Map<Coord, List<Coord>> neighbors) {
		for(List<Coord> crossing : neighbors.values()){
			//each crossing contains the hole way (inclusive start and end point)
			final Coord start = crossing.get(0);
			final Coord end = crossing.get(crossing.size() - 1);

			crossing.remove(0);
			crossing.remove(crossing.size() - 1);

			//now crossing contains only the way between start and end (exclusive start and end point)
			final Route forwadRoute = new Route(start, end);
			final Edge forward = new Edge(forwadRoute, crossing);
			nodes.put(forwadRoute, forward);

			Collections.reverse(crossing);
			final Route backwardRoute = new Route(end, start);
			Edge backward = new Edge(backwardRoute, crossing);
			nodes.put(backwardRoute, backward);
		}
	}

	static Collection<Coord> detectCrossings(Maze maze) {
		Set<Coord> result = new HashSet<>();

		for(Field f : maze.getWayFields()){
			final Coord curCoord = f.getCoord();
			if(maze.isCrossing(curCoord)){
				result.add(curCoord);
			}
		}

		return result;
	}

	static Map<Coord, List<Coord>> detectNeighborCrossings(Maze maze, Coord crossing) {
		Map<Coord, List<Coord>> result = new HashMap<Coord, List<Coord>>();

		List<Coord> walkedWay = new Walker(maze, crossing, Direction.NORTH).getWalkedWay();
		collectRoute(result, walkedWay);
		walkedWay = new Walker(maze, crossing, Direction.EAST).getWalkedWay();
		collectRoute(result, walkedWay);
		walkedWay = new Walker(maze, crossing, Direction.SOUTH).getWalkedWay();
		collectRoute(result, walkedWay);
		walkedWay = new Walker(maze, crossing, Direction.WEST).getWalkedWay();
		collectRoute(result, walkedWay);

		return result;
	}

	private static void collectRoute(Map<Coord, List<Coord>> result, List<Coord> walkedWay) {
		if(walkedWay.size() >= 2){
			final Coord end = walkedWay.get(walkedWay.size() - 1);
			result.put(end, walkedWay);
		}
	}

	private static class Walker {
		private final Maze maze;
		private final List<Coord> walkedWay = new LinkedList<Coord>();

		public Walker(Maze maze, Coord start, Direction d) {
			this.maze = maze;
			this.walkedWay.add(start);

			walk(start.neighbor(d));
		}

		private void walk(Coord coord) {
			Field f = maze.getField(coord);
			if(f == null || f.isWall()) return;

			outerLoop: while(true){
				walkedWay.add(coord);

				for(Direction d : Direction.values()){
					Coord neighbor = coord.neighbor(d);
					f = maze.getField(neighbor);

					if(f != null && !f.isWall() && !walkedWay.contains(neighbor)){
						if(maze.isCrossing(neighbor)){
							walkedWay.add(neighbor);
							return;
						}else{
							coord = neighbor;
							continue outerLoop;
						}
					}
				}

				break;
			}
		}

		public List<Coord> getWalkedWay() {
			return walkedWay;
		}
	}

	public List<Coord> getShortestWay(final Coord start, final Coord destination){
		if(isNode(start)){
			Dijkstra d = new Dijkstra(start, nodes);

			//easiest condition.. (crossing to crossing)
			if(isNode(destination)){
				return d.getShortestWay(destination);
			}

			//crossing to edge
			Coord nearest = getNearestNode(destination);
			List<Coord> way = d.getShortestWay(nearest);
			Iterator<Coord> iter = way.iterator();
			boolean found = false;

			while(iter.hasNext()){
				final Coord c = iter.next();

				if(found) iter.remove();
				if(c.equals(destination)) found = true;
			}

			return way;
		}

		Coord nearestStart = getNearestNode(start);
		Dijkstra d = new Dijkstra(nearestStart, nodes);

		//edge to crossing
		if(isNode(destination)){
			List<Coord> way = d.getShortestWay(destination);
			Iterator<Coord> iter = way.iterator();

			while(iter.hasNext()){
				if(iter.next().equals(start)) break;
				else iter.remove();
			}

			return way;
		}

		//edge to edge
		Coord nearestDest = getNearestNode(destination);
		List<Coord> way = d.getShortestWay(nearestDest);

		Iterator<Coord> iter = way.iterator();
		boolean foundStart = false;
		boolean foundDest = false;

		while(iter.hasNext()){
			final Coord c = iter.next();

			if(c.equals(start)) foundStart = true;
			if(!foundStart || foundDest) iter.remove();
			if(c.equals(destination)) foundDest = true;
		}

		return way;
	}

	private boolean isNode(Coord coord) {
		for(Route r : nodes.keySet()){
			if(r.getStart().equals(coord) || r.getEnd().equals(coord)){
				return true;
			}
		}

		return false;
	}

	private Coord getNearestNode(final Coord coord) {
		List<Edge> edges = new ArrayList<>();

		for(Edge edge : nodes.values()){
			if(edge.getEdge().contains(coord)){
				edges.add(edge);
			}
		}

		Collections.sort(edges, new Comparator<Edge>() {
			@Override
			public int compare(Edge o1, Edge o2) {
				return Integer.compare(wayToStart(o1), wayToStart(o2));
			}

			private int wayToStart(Edge edge) {
				for(int i=0; i < edge.getEdge().size(); i++){
					if(edge.getEdge().get(i).equals(coord)){
						return i;
					}
				}

				return -1;
			}
		});

		return edges.get(0).getStart();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(Edge edge : nodes.values()){
			sb.append(edge);
			sb.append("\n");
		}

		return sb.toString();
	}
}
