package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.*;
import java.util.Map.Entry;

import de.tarent.mica.maze.model.Coord;

public class Dijkstra {
	static class TableEntry {
		boolean visited = false;
		Integer distance;
		Coord prenode;
	}

	private final Coord start;
	private final Coord end;
	private final Map<Route, Edge> nodes;
	Map<Coord, TableEntry> table = new HashMap<Coord, TableEntry>();

	public Dijkstra(Coord start, Map<Route, Edge> nodes){
		this(start, nodes, null);
	}

	public Dijkstra(Coord start, Map<Route, Edge> nodes, Coord end){
		this.start = start;
		this.nodes = nodes;
		this.end = end;

		buildRouteTable();
	}

	private void buildRouteTable() {
		initTable();

		Coord curNode = start;
		final TableEntry startEntry = table.get(start);
		startEntry.distance = 0;
		startEntry.prenode = start;

		while(!allVisited()){
			curNode = getShortestUnvisitedNode(curNode);
			markAsVisited(curNode);

			if(end != null && end.equals(curNode)){
				break;
			}

			Set<Edge> neighbors = getUnvisitedNeighbors(curNode);
			TableEntry curEntry = table.get(curNode);
			for(Edge neighbor : neighbors){
				TableEntry neighborEntry = table.get(neighbor.getEnd());
				int sumDistance = curEntry.distance + getWeight(curEntry.prenode, curNode, neighbor);
				if(neighborEntry.distance == null || sumDistance < neighborEntry.distance){
					neighborEntry.distance = sumDistance;
					neighborEntry.prenode = curNode;
				}
			}
		}
	}

	private void initTable() {
		for(Route r : nodes.keySet()){
			table.put(r.getStart(), new TableEntry());
		}
	}

	private boolean allVisited() {
		for(TableEntry entry : table.values()){
			if(!entry.visited){
				return false;
			}
		}
		return true;
	}

	private Coord getShortestUnvisitedNode(final Coord node) {
		final Comparator<Coord> comparator = new Comparator<Coord>() {
			@Override
			public int compare(Coord o1, Coord o2) {
				return Integer.compare(getDistance(o1), getDistance(o2));
			}

			private int getDistance(Coord c) {
				TableEntry te = table.get(c);
				if(te.distance != null) return te.distance;

				final Route r = new Route(node, c);
				if(nodes.containsKey(r)){
					return nodes.get(r).getWeight();
				}

				return Integer.MAX_VALUE;
			}
		};

		Coord shortest = null;

		for(Entry<Route, Edge> entry : nodes.entrySet()){
			final Coord c = entry.getKey().getEnd();
			if(!table.get(c).visited){
				if(shortest == null){
					shortest = c;
				}else{
					final int comp = comparator.compare(shortest, c);
					if(comp > 0){
						shortest = c;
					}
				}
			}
		}

		return shortest;
	}

	private Set<Edge> getUnvisitedNeighbors(Coord node) {
		Set<Edge> result = new HashSet<Edge>();

		for(Edge edge : nodes.values()){
			if(edge.getStart().equals(node)){
				result.add(edge);
			}
		}

		//remove visited nodes
		Iterator<Edge> iter = result.iterator();
		while(iter.hasNext()){
			final Coord curNode = iter.next().getEnd();
			if(table.get(curNode).visited){
				iter.remove();
			}
		}

		return result;
	}

	protected Integer getWeight(Coord preNode, Coord curNode, Edge neighbor) {
		if(preNode != null && curNode.equals(preNode)){
			return neighbor.getWeight();
		}

		Edge edge = nodes.get(new Route(preNode, curNode));
		List<Coord> e = edge.getEdge();

		Coord a = e.isEmpty() ? edge.getStart() : e.get(e.size() - 1);
		Coord b = curNode;
		Coord c = neighbor.getEdge().isEmpty() ? neighbor.getEnd() : neighbor.getEdge().get(0);

		if(Coord.isCurve(a, b, c)){
			return neighbor.getWeight() + 1;
		}

		return neighbor.getWeight();
	}

	private void markAsVisited(Coord node) {
		table.get(node).visited = true;
	}

	public List<Coord> getShortestWay(Coord destination){
		if(this.end != null && !this.end.equals(destination)){
			throw new IllegalStateException("The distance table is not complete!");
		}

		if(!table.containsKey(destination)) return null;
		if(start.equals(destination)) return new ArrayList<>(Arrays.asList(destination));

		final List<Coord> nodeWay = getNodes(destination);
		final List<Coord> completeWay = new LinkedList<Coord>();

		if(nodeWay.size() == 1) return nodeWay;

		for(int i=0; i < nodeWay.size() - 1; i++){
			Route route = new Route(nodeWay.get(i), nodeWay.get(i + 1));
			Edge edge = nodes.get(route);

			completeWay.add(edge.getStart());
			completeWay.addAll(edge.getEdge());
		}
		completeWay.add(destination);

		return completeWay;
	}

	List<Coord> getNodes(Coord destination) {
		List<Coord> nodeWay = new LinkedList<Coord>();
		nodeWay.add(destination);

		TableEntry entry = table.get(destination);

		do{
			entry = table.get(destination);
			nodeWay.add(entry.prenode);
			destination = entry.prenode;
		}while(!start.equals(entry.prenode));

		Collections.reverse(nodeWay);

		return nodeWay;
	}
}
