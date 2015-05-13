package de.tarent.mica.maze.cli;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.tarent.mica.maze.bot.Robot;
import de.tarent.mica.maze.bot.RobotImpl;
import de.tarent.mica.maze.net.Controller;

public class Commands {

	@Inject
	private Shell parent;

	@Command
	public void start() throws Exception{
		start("rainu", "localhost", 30000);
	}

	@Command
	public void start(
			@Param(value = "host")
			String host,
			@Param(value = "port")
			int port) throws Exception{

		start("rainu", host, port);
	}

	@Command
	public void start(
			@Param(value = "roboName")
			String name,
			@Param(value = "host")
			String host,
			@Param(value = "port")
			int port) throws Exception{

		final StrategyBuilder builder = new StrategyBuilder();
		Shell subShell = ShellBuilder.subshell("strategy", parent)
					.behavior().addHandler(builder)
				.build();

		subShell.commandLoop();

		final Robot robot = new RobotImpl(name, builder.getStrategy());
		new Controller(robot, host, port).start();
	}
}
