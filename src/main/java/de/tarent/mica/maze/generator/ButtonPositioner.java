package de.tarent.mica.maze.generator;

import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.util.Random;

public class ButtonPositioner implements MazeGenerator {
	private final MazeGenerator generator;

	public ButtonPositioner(MazeGenerator generator) {
		this.generator = generator;
	}

	@Override
	public Maze generateMaze() {
		Maze maze = generator.generateMaze();

		for(Type btn : Type.getButtons()){
			Field field;
			do{
				field = new Random().choose(maze.getWayFields());
			}while(field.hasButton());

			field.setButton(btn);
		}

		return maze;
	}

}
