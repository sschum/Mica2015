package de.tarent.mica.maze.bot;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.event.Event;

public interface Robot {

	public Action handleEvent(Event event);

	public void reset();
}
