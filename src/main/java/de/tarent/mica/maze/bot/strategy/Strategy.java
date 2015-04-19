package de.tarent.mica.maze.bot.strategy;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.model.World;

public interface Strategy {

	public Action getNetxtAction(World world);
}
