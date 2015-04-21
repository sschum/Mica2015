package de.tarent.mica.maze.cli;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.tarent.mica.maze.bot.RobotImpl;
import de.tarent.mica.maze.bot.strategy.ExplorerStrategy;
import de.tarent.mica.maze.net.Controller;

public class Commands {

	@Command
	public void start() throws Exception{
		new Controller(new RobotImpl("rainu", new ExplorerStrategy()), "localhost", 30000).start();
	}

	@Command
	public void start(
			@Param(value = "host")
			String host,
			@Param(value = "port")
			int port) throws Exception{

		new Controller(null, host, port).start();
	}
}
