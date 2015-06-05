package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.Map.Entry;

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

		Collection<Coord> unmatched = detectUnmatchedFields(maze);
		for(Coord coord : unmatched){
			Map<Coord, List<Coord>> neighbors = detectNeighborCrossings(maze, coord);
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

	private Collection<Coord> detectUnmatchedFields(Maze maze) {
		Set<Coord> unmatched = new HashSet<Coord>();
		for(Field f : maze.getWayFields()){
			unmatched.add(f.getCoord());
		}

		for(Entry<Route, Edge> entry : nodes.entrySet()){
			remove(unmatched, entry.getValue().getStart());
			remove(unmatched, entry.getValue().getEnd());
			for(Coord coord : entry.getValue().getEdge()){
				remove(unmatched, coord);
			}
		}

		return unmatched;
	}

	private void remove(Set<Coord> unmatched, Coord c) {
		Iterator<Coord> iter = unmatched.iterator();
		while(iter.hasNext()){
			if(iter.next().equals(c)){
				iter.remove();
			}
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
				if(maze.isCrossing(coord)){
					return;
				}

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
		List<Coord> way = _getShortestWay(start, destination);

		if(way != null){
			if(!way.get(0).equals(start)){
				way.add(0, start);
			}
			if(!way.get(way.size() - 1).equals(destination)){
				way.add(destination);
			}
		}

		way = smoothWay(way);
		return way;
	}

	private List<Coord> smoothWay(List<Coord> way) {
		final Coord start = way.get(0);
		final Coord dest = way.get(way.size() - 1);

		int fromIndex = 0;
		int toIndex = way.size();

		for(int i=0; i < way.size(); i++){
			Coord c = way.get(i);

			if(c.equals(start)){
				fromIndex = i;
			}
			if(c.equals(dest)){
				toIndex = i+1;
				break;
			}
		}

		return way.subList(fromIndex, toIndex);
	}

	private List<Coord> _getShortestWay(final Coord start, final Coord destination){
		if(start.equals(destination)){
			return new ArrayList<>(Arrays.asList(start));
		}
		if(	start.north().equals(destination) ||
			start.east().equals(destination) ||
			start.south().equals(destination) ||
			start.west().equals(destination)){

			return new ArrayList<>(Arrays.asList(start, destination));
		}

		if(isNode(start)){
			Dijkstra d = buildDijkstra(start);

			//easiest condition.. (crossing to crossing)
			if(isNode(destination)){
				return d.getShortestWay(destination);
			}

			//crossing to edge
			return getShortestWayCrossingToEdge(destination, d);
		}

		//edge to crossing
		if(isNode(destination)){
			return getShortestWayEdgeToCrossing(start, destination);
		}

		for(Edge edge : nodes.values()){
			if(wayIsInEdge(edge, start, destination)){
				//edge itself
				return getWayInEdge(edge, start, destination);
			}
		}

		//edge to edge
		List<Coord> firstWay = getShortestWayEdgeToEdge(start, destination);
		List<Coord> secoundWay = getShortestWayEdgeToEdge(destination, start);
		List<Coord> finalWay = firstWay;

		if(secoundWay.size() < firstWay.size()){
			Collections.reverse(secoundWay);
			finalWay = secoundWay;
		}

		return finalWay;
	}

	private Map<Coord, Dijkstra> preCalculatedDijkstra = new HashMap<>();

	private synchronized Dijkstra buildDijkstra(Coord start) {
		if(!preCalculatedDijkstra.containsKey(start)){
			preCalculatedDijkstra.put(start, new Dijkstra(start, nodes));
		}

		return preCalculatedDijkstra.get(start);
	}

	private List<Coord> getShortestWayEdgeToEdge(final Coord start, final Coord destination) {
		List<Edge> startEdges = getEdges(start);
		List<Coord> wayToCrossing = null;
		Coord startCrossing = null;
		for(Edge edge : startEdges){
			List<Coord> way = getWayPartUntilEnd(edge, start);

			if(wayToCrossing == null || (!way.isEmpty() && wayToCrossing.size() > way.size())){
				wayToCrossing = way;
				startCrossing = edge.getEnd();
			}
		}

		List<Edge> destEdges = getEdges(destination);
		List<Coord> wayFromCrossing = null;
		Coord destCrossing = null;
		for(Edge edge : destEdges){
			List<Coord> way = getWayPartFromStart(edge, start);

			if(wayFromCrossing == null || (!way.isEmpty() && wayFromCrossing.size() > way.size())){
				wayFromCrossing = way;
				destCrossing = edge.getStart();
			}
		}

		List<Coord> completeWay = new LinkedList<>();
		completeWay.addAll(wayToCrossing);
		completeWay.addAll(getShortestWay(startCrossing, destCrossing));
		completeWay.addAll(wayFromCrossing);

		cutEnd(completeWay, destination);

		return completeWay;
	}

	private List<Coord> getWayPartUntilEnd(Edge edge, Coord start){
		List<Coord> way = new ArrayList<>(edge.getEdge());
		cutStart(way, start);

		return way;
	}

	private List<Coord> getWayPartFromStart(Edge edge, Coord dest){
		List<Coord> way = new ArrayList<>(edge.getEdge());
		cutEnd(way, dest);

		return way;
	}

	private void cutStart(List<Coord> list, Coord start) {
		Iterator<Coord> iter = list.iterator();
		boolean found = false;

		while(iter.hasNext()){
			final Coord c = iter.next();

			if(!found) iter.remove();
			if(c.equals(start)) found = true;
		}
	}

	private void cutEnd(List<Coord> list, Coord dest) {
		Iterator<Coord> iter = list.iterator();
		boolean found = false;

		while(iter.hasNext()){
			final Coord c = iter.next();

			if(found) iter.remove();
			if(c.equals(dest)) found = true;
		}
	}

	private boolean wayIsInEdge(Edge edge, Coord start, Coord dest) {
		Set<Coord> completeWay = new HashSet<Coord>(edge.getEdge().size() + 2);
		completeWay.add(edge.getStart());
		completeWay.addAll(edge.getEdge());
		completeWay.add(edge.getEnd());

		return completeWay.contains(start) && completeWay.contains(dest);
	}

	private List<Coord> getWayInEdge(Edge edge, Coord start, Coord destination) {
		List<Coord> way = new ArrayList<Coord>();
		way.add(edge.getStart());
		way.addAll(edge.getEdge());
		way.add(edge.getEnd());

		boolean reverse = false;

		for(Coord c : way){
			if(c.equals(start) || c.equals(destination)){
				if(c.equals(destination)){
					reverse = true;
				}

				break;
			}
		}

		if(reverse) Collections.reverse(way);

		cutStart(way, start);
		cutEnd(way, destination);

		return way;
	}

	private List<Coord> getShortestWayCrossingToEdge(final Coord destination, Dijkstra d) {
		List<Edge> destEdges = getEdges(destination);
		List<Coord> wayFromCrossing = null;
		Coord destCrossing = null;
		for(Edge edge : destEdges){
			List<Coord> way = getWayPartFromStart(edge, edge.getStart());

			if(wayFromCrossing == null || wayFromCrossing.size() > way.size()){
				wayFromCrossing = way;
				destCrossing = edge.getStart();
			}
		}

		List<Coord> completeWay = new LinkedList<>();
		List<Coord> c2c = d.getShortestWay(destCrossing);

		completeWay.addAll(c2c);
		completeWay.addAll(wayFromCrossing);

		return completeWay;
	}

	private List<Coord> getShortestWayEdgeToCrossing(final Coord start, final Coord destination) {
		List<Edge> startEdges = getEdges(start);
		List<Coord> wayToCrossing = null;
		Coord startCrossing = null;
		for(Edge edge : startEdges){
			List<Coord> way = getWayPartUntilEnd(edge, start);

			if(wayToCrossing == null || wayToCrossing.size() > way.size()){
				wayToCrossing = way;
				startCrossing = edge.getEnd();
			}
		}

		Dijkstra d = new Dijkstra(destination, nodes, startCrossing);

		List<Coord> completeWay = new ArrayList<>();
		List<Coord> c2c = d.getShortestWay(startCrossing);
		Collections.reverse(c2c);

		completeWay.addAll(wayToCrossing);
		completeWay.addAll(c2c);

		return completeWay;
	}

	private boolean isNode(Coord coord) {
		for(Route r : nodes.keySet()){
			if(r.getStart().equals(coord) || r.getEnd().equals(coord)){
				return true;
			}
		}

		return false;
	}

	private List<Edge> getEdges(final Coord coord) {
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
		return edges;
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
