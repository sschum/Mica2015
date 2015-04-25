package de.tarent.mica.maze.bot.strategy.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Direction;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;

/**
 * This class is responsible for finding a route from a start
 * position to a destination.
 *
 * @author rainu
 */
public class PathFinder {

	private List<List<Coord>> routes = new LinkedList<>();
	private final Maze maze;
	private final Coord start;
	private final Coord dest;

	private final long maxDuration;
	private final long startTime;

	public PathFinder(Maze maze, Coord start, Coord dest, TimeUnit unit, long maxDuration) {
		this.maze = maze;
		this.start = start;
		this.dest = dest;
		this.maxDuration = TimeUnit.MILLISECONDS.convert(maxDuration, unit);

		this.startTime = System.currentTimeMillis();
		walk(new LinkedList<Coord>(), start, dest);

		Collections.sort(routes, new Comparator<List<Coord>>() {
			@Override
			public int compare(List<Coord> o1, List<Coord> o2) {
				return Integer.compare(o1.size(), o2.size());
			}
		});
	}

	public PathFinder(Maze maze, Coord start, Coord dest) {
		this(maze, start, dest, TimeUnit.SECONDS, 5);
	}

	private void walk(List<Coord> walkedWay, Coord curCoord, final Coord dest){
		if(timeIsOver()){
			return;
		}
		if(visited(walkedWay, curCoord)){
			return;
		}
		walkedWay.add(curCoord);

		if(destinationReached(curCoord, dest)){
			saveWay(walkedWay);
			return;
		}
		if(worseThanBest(walkedWay)){
			return;
		}

		Collection<Coord> neighbors = getNeighbors(curCoord, dest);
		for(Coord n : neighbors){
			walk(copyWay(walkedWay), n, dest);
		}
	}

	private boolean timeIsOver() {
		return (startTime + maxDuration) <= System.currentTimeMillis();
	}

	private boolean visited(List<Coord> walkedWay, Coord curCoord) {
		return walkedWay.contains(curCoord);
	}

	private boolean destinationReached(Coord curCoord, Coord dest) {
		return curCoord.equals(dest);
	}

	private boolean worseThanBest(List<Coord> walkedWay) {
		int longest = getLongestWay();
		return walkedWay.size() >= longest;
	}

	private int getLongestWay() {
		int longest = Integer.MAX_VALUE;

		for(List<Coord> r : routes){
			if(r.size() > longest){
				longest = r.size();
			}
		}

		return longest;
	}

	private void saveWay(List<Coord> walkedWay) {
		List<Coord> copy = copyWay(walkedWay);

		routes.add(Collections.unmodifiableList(copy));
	}

	private List<Coord> copyWay(List<Coord> walkedWay) {
		List<Coord> copy = new LinkedList<>();
		for(Coord c : walkedWay){
			copy.add(c.clone());
		}
		return copy;
	}

	private List<Coord> getNeighbors(Coord curCoord, Coord dest) {
		List<Coord> neighbors = new LinkedList<>();
		List<Direction> directions = getShortestDirections(curCoord, dest);

		for(Direction dir : directions){
			switch(dir){
			case NORTH: neighbors.add(curCoord.north()); break;
			case EAST:	neighbors.add(curCoord.east()); break;
			case SOUTH: neighbors.add(curCoord.south()); break;
			case WEST:	neighbors.add(curCoord.west()); break;
			}
		}

		Iterator<Coord> iter = neighbors.iterator();
		while(iter.hasNext()){
			Field f = maze.getField(iter.next());

			if(f == null || f.isWall()){
				iter.remove();
			}
		}

		return neighbors;
	}

	//TODO: Vorher das Labyrinth analysieren... je nach "bauart" ist eine "rekursionsrichtung" besser geeignet
	private List<Direction> getShortestDirections(Coord curCord, Coord dest) {
		List<Direction> result = new ArrayList<Direction>(4);

		int horizontalDist = Math.max(curCord.getX(), dest.getX()) - Math.min(curCord.getX(), dest.getX());
		int verticalDist = Math.max(curCord.getY(), dest.getY()) - Math.min(curCord.getY(), dest.getY());

		if(horizontalDist > verticalDist){
			if(curCord.getX() < dest.getX()){
				result.add(Direction.EAST);
			}else if(curCord.getX() > dest.getX()){
				result.add(Direction.WEST);
			}
		}

		if(horizontalDist < verticalDist){
			if(curCord.getY() < dest.getY()){
				result.add(Direction.NORTH);
			}else if(curCord.getY() > dest.getY()){
				result.add(Direction.SOUTH);
			}
		}

		if(!result.contains(Direction.NORTH)) result.add(Direction.NORTH);
		if(!result.contains(Direction.EAST)) result.add(Direction.EAST);
		if(!result.contains(Direction.SOUTH)) result.add(Direction.SOUTH);
		if(!result.contains(Direction.WEST)) result.add(Direction.WEST);

		return result;
	}

	public List<List<Coord>> getRoutes() {
		return Collections.unmodifiableList(routes);
	}

	public static String toString(List<Coord> route){
		List<Field> fields = new LinkedList<>();

		Direction direction = Direction.NORTH;
		for(int i=0; i < route.size(); i++){
			final Coord curCoord = route.get(i);
			final Coord nextCoord = i + 1 < route.size() ? route.get(i + 1) : null;

			if(curCoord.north().equals(nextCoord)){
				direction = Direction.NORTH;
			}else if(curCoord.east().equals(nextCoord)){
				direction = Direction.EAST;
			}else if(curCoord.south().equals(nextCoord)){
				direction = Direction.SOUTH;
			}else if(curCoord.west().equals(nextCoord)){
				direction = Direction.WEST;
			}

			switch(direction){
			case NORTH:
				fields.add(new Field(curCoord, Type.PLAYER_NORTH)); break;
			case EAST:
				fields.add(new Field(curCoord, Type.PLAYER_EAST)); break;
			case SOUTH:
				fields.add(new Field(curCoord, Type.PLAYER_SOUTH)); break;
			case WEST:
				fields.add(new Field(curCoord, Type.PLAYER_WEST)); break;
			}
		}

		final Maze m = new Maze(fields);

		return m.toString();
	}
}
