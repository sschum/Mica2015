package de.tarent.mica.maze.bot.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Swap;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;

/**
 * This {@link Strategy} is responsible to put a button into
 * the inventory if this button is not pushed and should be
 * pushed in the future.
 *
 * @author rainu
 */
public class SwapStrategy extends AbstractStrategy {

	public SwapStrategy(PathWalkerStrategy pws) {
		super(pws);
	}

	@Override
	public Action getNextAction(World world) {
		if(canPushInventoryButton(world) || canPutIntoInventory(world)){
			return new Swap();
		}

		return super.getNextAction(world);
	}

	private boolean canPutIntoInventory(World world) {
		final Field playerField = world.getMaze().getPlayerField();
		if(playerField.hasButton()){
			if(world.getInventarButton() == null){
				return true;
			}

			if(world.getLastPushedButton() == null){
				return false;
			}

			return world.getInventarButton() < world.getLastPushedButton() - 1;
		}

		return false;
	}

	private boolean canPushInventoryButton(World world) {
		if(world.getInventarButton() != null){
			if(world.getLastPushedButton() == null){
				return world.getInventarButton() == 0;
			}

			return world.getInventarButton() == world.getLastPushedButton() + 1;
		}

		return false;
	}

	@Override
	protected List<Coord> getPointsOfInterest(World world) {
		if(world.getInventarButton() != null) return Collections.EMPTY_LIST;

		List<Coord> points = new ArrayList<>();
		Integer toPush = getNextButtonToPush(world);

		if(toPush != null){
			for(int i = toPush + 1; i < 10; i++){
				Type btnType = Type.getButton(i);

				Field f = world.getMaze().getButtonField(btnType);
				if(f != null){
					points.add(f.getCoord());
				}
			}
		}

		return points;
	}

	private Integer getNextButtonToPush(World world) {
		Integer toPush = world.getLastPushedButton() == null ? 0 : world.getLastPushedButton() + 1;
		return toPush;
	}
}
