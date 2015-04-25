package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tarent.mica.maze.model.Coord;

/**
 * Diese Klasse repräsentiert eine Kante in dem Graphen-model.
 * Eine Kante ist eine Verbindung zwischen Knoten. In diesem
 * Kontext representiert eine Kante ein Weg zwischen zwei Kreuzungen
 * im Labyrinth. Jede Kante hat einen start- und end- Punkt und ggf.
 * einen Weg. Start und End-Punkt representieren jeweils eine
 * Kreuzung/Sackgasse im Labyrinth.
 *
 * @author rainu
 *
 */
public class Edge {

	private final Route route;

	private List<Coord> edge = new LinkedList<>();
	private int weight = 1;

	public Edge(Route route, Coord...edge){
		this(route, Arrays.asList(edge));
	}

	public Edge(Route route, List<Coord> edge){
		this.route = route;

		for(int i=0; i < edge.size(); i++){
			final Coord curCoord = edge.get(i);
			final Coord prevCoord = i - 1 <= 0 ? route.getStart() : edge.get(i - 1);
			final Coord nextCoord = i + 1 >= edge.size() ? route.getEnd() : edge.get(i + 1);

			if(isCurve(prevCoord, curCoord, nextCoord)){
				weight++; //curves count twice
			}

			weight++;
		}

		this.edge.addAll(edge);
	}

	private boolean isCurve(Coord prev, Coord cur, Coord next) {
		if(prev == null || cur == null || next == null){
			return false;
		}
		if(prev.getX() == cur.getX() && next.getX() == cur.getX()){
			//vertical align
			return false;
		}
		if(prev.getY() == cur.getY() && next.getY() == cur.getY()){
			//horizonntal align
			return false;
		}

		return true;
	}

	public List<Coord> getEdge() {
		return Collections.unmodifiableList(edge);
	}

	public Coord getStart() {
		return route.getStart();
	}

	public Coord getEnd() {
		return route.getEnd();
	}

	public int getWeight(){
		return weight;
	}

	@Override
	public String toString() {
		return route + "(" + getWeight() + "): " + edge;
	}
}
