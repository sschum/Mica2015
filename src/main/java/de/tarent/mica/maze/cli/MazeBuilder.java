package de.tarent.mica.maze.cli;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.annotation.Param;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.raysha.lib.jsimpleshell.io.OutputBuilder;
import de.tarent.mica.maze.generator.ButtonPositioner;
import de.tarent.mica.maze.generator.MazeGenerator;
import de.tarent.mica.maze.generator.MazePerforater;
import de.tarent.mica.maze.generator.PerfectMazeGenerator;
import de.tarent.mica.maze.util.Random;

public class MazeBuilder {

	@Inject
	private OutputBuilder out;

	private MazeGenerator generator;

	@Command(name="default", abbrev="d")
	public void defaultGenerator() throws ExitException{
		generator = new PerfectMazeGenerator(25, 25);
		generator = new MazePerforater(generator, 25);

		throw new ExitException();
	}

	@Command
	public void setDimension(
			@Param("height")
			int height,
			@Param("width")
			int width){

		generator = new PerfectMazeGenerator(height, width);
	}

	@Command
	public void setRandomDimension(){
		Random r = new Random();

		final int height = r.nextInt() % 100;
		final int width = r.nextInt() % 100;

		setDimension(height, width);
		out.out().normal("Coosen dimension: " + height + "x" + width).println();
	}

	@Command
	public void setPerforator(
			@Param("percent")
			int percent){

		if(generator == null){
			out.err().normal("You have to configure a generator at the first.").println();
			return;
		}

		generator = new MazePerforater(generator, percent);
	}

	public MazeGenerator getGenerator(){
		return new ButtonPositioner(generator);
	}
}
