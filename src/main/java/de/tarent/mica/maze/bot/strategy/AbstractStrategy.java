package de.tarent.mica.maze.bot.strategy;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.strategy.navigation.PathFinder;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.util.LogFormat;

/**
 * This class contains basic code snippet for a {@link Strategy}.
 *
 * @author rainu
 */
public abstract class AbstractStrategy implements Strategy {
	private final Logger log = Logger.getLogger(getClass());

	protected static final int MAX_POINTS_OF_INTEREST = 5;

	private PathWalkerStrategy pathWalker;

	public AbstractStrategy(PathWalkerStrategy walker) {
		this.pathWalker = walker;
	}

	@Override
	public Action getNextAction(World world){
		PathWalkerStrategy walker = getPathWalker(world);
		if(walker == null){
			return null; //no points of interests available
		}

		return walker.getNextAction(world);
	}

	@Override
	public void reset() {
		pathWalker.reset();
	}

	protected final PathWalkerStrategy getPathWalker(World world) {
		if(this.pathWalker.isArrived(world)){
			final Coord playerCoord = world.getMaze().getPlayerField().getCoord();
			List<Coord> points = getPointsOfInterest(world);

			if(points == null || points.isEmpty()){
				log.debug("No points of interests available!");
				return null;
			}

			log.debug(LogFormat.format("Points of interrests:\ny{0}", world.getMaze().toString((Collection<Coord>)points)));

			if(points.size() > MAX_POINTS_OF_INTEREST){
				log.debug("To many points of interests. Reduce them...!");
				reducePointsOfInterest(world, points);
				log.debug(LogFormat.format("Reduced points of interrests:\ny{0}", world.getMaze().toString((Collection<Coord>)points)));
			}

			PathFinder pf = new PathFinder(world.getMaze());
			List<List<Coord>> routes = pf.getRoutes(playerCoord, points);
			List<Coord> shortest = null;
			for(List<Coord> path : routes){
				if(path == null || path.isEmpty()){
					continue;
				}

				if(shortest == null || shortest.isEmpty() || shortest.size() > path.size()){
					shortest = path;
				}
			}

			this.pathWalker.startNewMission(world, shortest);
		}

		return this.pathWalker;
	}

	private void reducePointsOfInterest(World world, List<Coord> points) {
		final Coord playerCoord = world.getMaze().getPlayerField().getCoord();

		Collections.sort(points, new Comparator<Coord>() {
			@Override
			public int compare(Coord o1, Coord o2) {
				return Integer.compare(calcDistance(o1), calcDistance(o2));
			}

			private int calcDistance(Coord c){
				double dist = new Point(playerCoord.getX(), playerCoord.getY()).distance(c.getX(), c.getY());
				return (int)dist;
			}
		});

		Iterator<Coord> iter = points.iterator();
		for(int i=0; iter.hasNext(); i++){
			if(i >= MAX_POINTS_OF_INTEREST){
				iter.remove();
			}

			iter.next();
		}
	}

	protected abstract List<Coord> getPointsOfInterest(World world);

}
