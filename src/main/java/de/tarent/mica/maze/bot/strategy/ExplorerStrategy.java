package de.tarent.mica.maze.bot.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;
import de.tarent.mica.maze.bot.strategy.navigation.PathFinder;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.util.LogFormat;

/**
 * This strategy tries to explore the maze until all buttons are
 * discovered.
 *
 * @author rainu
 */
public class ExplorerStrategy implements Strategy {
	private static final Logger log = Logger.getLogger(ExplorerStrategy.class);

	private List<Coord> curPath;
	private Coord curDest;

	@Override
	public Action getNetxtAction(World world) {
		if(isDiscovered(world)) return null;

		if(isDarkAroundMe(world)){
			//look in the dark direction
			return turnToDarkOrLookInto(world);
		}

		if(!haveMission(world)){
			startNewMission(world);
		}

		return continueMission(world);
	}

	boolean isDiscovered(World world) {
		List<Field> buttonFields = world.getMaze().getButtonFields();
		int btnCount = world.getInventarButton() != null ? 1 : 0;
		btnCount += buttonFields.size();
		btnCount += world.getLastPushedButton() != null ? world.getLastPushedButton() : 0;

		return btnCount >= 10;
	}

	boolean isDarkAroundMe(World world) {
		final Coord playerCoord = world.getMaze().getPlayerField().getCoord();

		return 	isDark(world, playerCoord.north()) ||
				isDark(world, playerCoord.east()) ||
				isDark(world, playerCoord.south()) ||
				isDark(world, playerCoord.west());
	}

	private boolean isDark(World world, final Coord coord) {
		return world.getMaze().getField(coord) == null;
	}

	Action turnToDarkOrLookInto(World world) {
		final Field player = world.getMaze().getPlayerField();
		final Coord pCoord = player.getCoord();

		//look if the player look into
		switch(player.getPlayerType()){
		case PLAYER_NORTH:
			if(isDark(world, pCoord.north())) return new Look();
			break;
		case PLAYER_EAST:
			if(isDark(world, pCoord.east())) return new Look();
			break;
		case PLAYER_SOUTH:
			if(isDark(world, pCoord.south())) return new Look();
			break;
		case PLAYER_WEST:
			if(isDark(world, pCoord.west())) return new Look();
			break;
		default:
			throw new IllegalStateException("This code should be never reached!");
		}

		//count turns look to nearest
		int[] count = new int[]{0,0,0,0};
		switch(player.getPlayerType()){
		case PLAYER_NORTH:	count[0] = 0; count[1] = 1; count[2] = 2; count[3] = 1; break;
		case PLAYER_EAST:	count[0] = 1; count[1] = 0; count[2] = 1; count[3] = 2; break;
		case PLAYER_SOUTH:	count[0] = 2; count[1] = 1; count[2] = 0; count[3] = 1; break;
		case PLAYER_WEST:	count[0] = 1; count[1] = 2; count[2] = 1; count[3] = 0; break;
		default:
			throw new IllegalStateException("This code should be never reached!");
		}

		if(!isDark(world, pCoord.north())) count[0] = 3;
		if(!isDark(world, pCoord.east())) count[1] = 3;
		if(!isDark(world, pCoord.south())) count[2] = 3;
		if(!isDark(world, pCoord.west())) count[3] = 3;

		int min = new TreeSet<>(Arrays.asList(count[0], count[1], count[2], count[3])).first();
		if(min == 2) return new TurnRight();
		if(min != 1) throw new IllegalStateException("This code should be never reached!");

		if(count[0] == 1){
			switch(player.getPlayerType()){
			case PLAYER_EAST: return new TurnLeft();
			case PLAYER_WEST: return new TurnRight();
			default:
			}
		}
		if(count[1] == 1){
			switch(player.getPlayerType()){
			case PLAYER_NORTH: return new TurnRight();
			case PLAYER_SOUTH: return new TurnLeft();
			default:
			}
		}
		if(count[2] == 1){
			switch(player.getPlayerType()){
			case PLAYER_EAST: return new TurnRight();
			case PLAYER_WEST: return new TurnLeft();
			default:
			}
		}
		if(count[3] == 1){
			switch(player.getPlayerType()){
			case PLAYER_SOUTH: return new TurnRight();
			case PLAYER_NORTH: return new TurnLeft();
			default:
			}
		}

		throw new IllegalStateException("This code should be never reached!");
	}

	private boolean haveMission(World world) {
		return (curPath != null && !curPath.isEmpty()) || (curDest != null && !curDest.equals(world.getMaze().getPlayerField().getCoord()));
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

	private void startNewMission(World world) {
		final Coord playerCoord = world.getMaze().getPlayerField().getCoord();
		List<Coord> points = getPointsOfInterest(world);

		if(points.isEmpty()){
			throw new RuntimeException("No points of interests available!");
		}

		PathFinder pf = new PathFinder(world.getMaze());
		for(Coord c : points){
			List<Coord> path = pf.getRoute(playerCoord, c);

			if(curPath == null || curPath.isEmpty() || curPath.size() > path.size()){
				curPath = path;
			}
		}

		if(curPath != null){
			curPath = new ArrayList<Coord>(curPath);
			curPath.remove(0); //0 is the start position itself!
		}

		log.info(LogFormat.format("Points of interrests:\ny{0}", world.getMaze().toString((Collection<Coord>)points)));
		log.info(LogFormat.format("Start new mission:\ny{0}", world.getMaze().toString(curPath)));
	}

	private Coord getCurrentDestination(World world) {
		if(curDest == null || curDest.equals(world.getMaze().getPlayerField().getCoord())){
			curDest = curPath.get(0);
			curPath.remove(0);
		}

		return curDest;
	}

	protected List<Coord> getPointsOfInterest(World world){
		final Maze maze = world.getMaze();
		List<Coord> pointsOfInterest = new ArrayList<Coord>(maze.getWayFields().size());

		for(Field field : maze.getWayFields()){
			if(	maze.getField(field.getCoord().north()) == null ||
				maze.getField(field.getCoord().east()) == null ||
				maze.getField(field.getCoord().south()) == null ||
				maze.getField(field.getCoord().west()) == null){

				pointsOfInterest.add(field.getCoord());
			}
		}

		return pointsOfInterest;
	}
}
