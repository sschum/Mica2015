package de.tarent.mica.maze.bot.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.util.LogFormat;

/**
 * This {@link Strategy} only follows a given path.
 *
 * @author rainu
 */
public class PathWalkerStrategy implements Strategy {
	private static final Logger log = Logger.getLogger(PathWalkerStrategy.class);

	private List<Coord> curPath;
	private Coord dest;
	private Coord curDest;

	public void startNewMission(World world, List<Coord> path) {
		if(path == null) throw new NullPointerException("The path must not be null!");

		curPath = new ArrayList<Coord>(path);
		curPath.remove(0); //0 is the start position itself!

		dest = curPath.get(curPath.size() - 1);

		log.info(LogFormat.format("Start new mission:\ny{0}", world.getMaze().toString(curPath)));
	}

	public boolean isArrived(World world){
		return dest == null || world.getMaze().getPlayerField().getCoord().equals(dest);
	}

	@Override
	public Action getNextAction(World world) {
		return continueMission(world);
	}

	private Action continueMission(World world) {
		final Coord dest = getCurrentDestination(world);
		final Field playerField = world.getMaze().getPlayerField();
		final Coord pCoord = playerField.getCoord();

		switch(playerField.getPlayerType()){
		case PLAYER_NORTH:
			if(pCoord.north().equals(dest)) return new Walk();
			if(pCoord.east().equals(dest)) return new TurnRight();
			if(pCoord.south().equals(dest)) return new TurnRight();
			if(pCoord.west().equals(dest)) return new TurnLeft();
			break;
		case PLAYER_EAST:
			if(pCoord.east().equals(dest)) return new Walk();
			if(pCoord.south().equals(dest)) return new TurnRight();
			if(pCoord.west().equals(dest)) return new TurnLeft();
			if(pCoord.north().equals(dest)) return new TurnRight();
			break;
		case PLAYER_SOUTH:
			if(pCoord.south().equals(dest)) return new Walk();
			if(pCoord.west().equals(dest)) return new TurnRight();
			if(pCoord.north().equals(dest)) return new TurnRight();
			if(pCoord.east().equals(dest)) return new TurnLeft();
			break;
		case PLAYER_WEST:
			if(pCoord.west().equals(dest)) return new Walk();
			if(pCoord.north().equals(dest)) return new TurnRight();
			if(pCoord.east().equals(dest)) return new TurnRight();
			if(pCoord.south().equals(dest)) return new TurnLeft();
			break;
		}

		return null;
	}

	private Coord getCurrentDestination(World world) {
		if(curDest == null || curDest.equals(world.getMaze().getPlayerField().getCoord())){
			curDest = curPath.get(0);
			curPath.remove(0);
		}

		return curDest;
	}
}
