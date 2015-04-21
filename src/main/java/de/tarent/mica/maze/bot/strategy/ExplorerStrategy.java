package de.tarent.mica.maze.bot.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.World;

/**
 * This strategy tries to explore the maze until all buttons are
 * discovered.
 *
 * @author rainu
 */
public class ExplorerStrategy implements Strategy {

	@Override
	public Action getNetxtAction(World world) {
		if(isDiscovered(world)) return null;

		if(isDarkAroundMe(world)){
			//look in the dark direction
			return turnToDarkOrLookInto(world);
		}

		return null;
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
}
