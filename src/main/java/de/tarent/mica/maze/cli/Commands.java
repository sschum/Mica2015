package de.tarent.mica.maze.cli;

import java.io.IOException;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.builder.ShellBuilder;
import de.tarent.mica.maze.bot.Robot;
import de.tarent.mica.maze.bot.RobotImpl;
import de.tarent.mica.maze.generator.ButtonPositioner;
import de.tarent.mica.maze.generator.MazeGenerator;
import de.tarent.mica.maze.generator.PerfectMazeGenerator;
import de.tarent.mica.maze.model.Maze;
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

		final MazeBuilder mazeBuilder = new MazeBuilder();
		subShell = ShellBuilder.subshell("maze", parent)
				.behavior().addHandler(mazeBuilder)
			.build();

		subShell.commandLoop();
		final MazeGenerator generator = mazeBuilder.getGenerator();

		new Controller(robot, generator, host, port).start();
	}

	@Command
	public Maze buildMaze() throws IOException{
		final MazeBuilder mazeBuilder = new MazeBuilder();
		Shell subShell = ShellBuilder.subshell("maze", parent)
				.behavior().addHandler(mazeBuilder)
			.build();

		subShell.commandLoop();
		final MazeGenerator generator = mazeBuilder.getGenerator();

		return generator.generateMaze();
	}
}
