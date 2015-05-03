package de.tarent.mica.maze.bot.strategy;

import java.util.Collection;
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

	protected final PathWalkerStrategy getPathWalker(World world) {
		if(this.pathWalker.isArrived(world)){
			final Coord playerCoord = world.getMaze().getPlayerField().getCoord();
			List<Coord> points = getPointsOfInterest(world);

			if(points == null || points.isEmpty()){
				log.debug("No points of interests available!");
				return null;
			}

			PathFinder pf = new PathFinder(world.getMaze());
			List<Coord> shortest = null;
			for(Coord c : points){
				List<Coord> path = pf.getRoute(playerCoord, c);

				if(shortest == null || shortest.isEmpty() || shortest.size() > path.size()){
					shortest = path;
				}
			}

			log.info(LogFormat.format("Points of interrests:\ny{0}", world.getMaze().toString((Collection<Coord>)points)));
			this.pathWalker.startNewMission(world, shortest);
		}

		return this.pathWalker;
	}

	protected abstract List<Coord> getPointsOfInterest(World world);

}
