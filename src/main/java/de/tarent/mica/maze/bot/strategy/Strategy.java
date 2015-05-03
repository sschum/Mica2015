package de.tarent.mica.maze.bot.strategy;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.model.World;

/**
 * A strategy for moving around the maze.
 *
 * @author rainu
 */
public interface Strategy {

	/**
	 * Get the next action for the game.
	 *
	 * @param world The current world.
	 * @return The {@link Action} for the game. Or <b>null</b> if this {@link Strategy} has no action.
	 */
	public Action getNextAction(World world);
}
