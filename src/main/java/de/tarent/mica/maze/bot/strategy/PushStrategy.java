package de.tarent.mica.maze.bot.strategy;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Push;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;

/**
 * This {@link Strategy} is responsible to push all buttons
 * (in right order) if there are available.
 *
 * @author rainu
 */
public class PushStrategy extends AbstractStrategy {
	private static final Logger log = Logger.getLogger(PushStrategy.class);

	public PushStrategy(PathWalkerStrategy walker) {
		super(walker);
	}

	@Override
	public Action getNextAction(World world) {
		if(allPushed(world)){
			log.info("All buttons pushed. I'm out!");
			return null;
		}

		if(playerIsOnButton(world)){
			return new Push();
		}

		return super.getNextAction(world);
	}

	private boolean allPushed(World world) {
		return world.getLastPushedButton() != null && world.getLastPushedButton() == 9;
	}

	private boolean playerIsOnButton(World world) {
		Integer toPush = getNextButtonToPush(world);
		final Field player = world.getMaze().getPlayerField();
		final Type btnType = player.getButtonType();

		return Type.getButton(toPush).equals(btnType);
	}

	@Override
	protected List<Coord> getPointsOfInterest(World world) {
		Integer toPush = getNextButtonToPush(world);
		final Type btnType = Type.getButton(toPush);

		final Field btnField = world.getMaze().getButtonField(btnType);
		if(btnField != null){
			return Collections.singletonList(btnField.getCoord());
		}

		return null;
	}

	private Integer getNextButtonToPush(World world) {
		Integer toPush = world.getLastPushedButton() == null ? 0 : world.getLastPushedButton() + 1;
		return toPush;
	}
}
