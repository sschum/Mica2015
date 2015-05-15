package de.tarent.mica.maze.generator;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;

/**
 * This class is responsible for scale a maze which was generated
 * by the given generator.
 *
 * @author rainu
 */
public class MazeScaler implements MazeGenerator {
	private final MazeGenerator generator;

	public MazeScaler(MazeGenerator generator) {
		this.generator = generator;
	}

	@Override
	public Maze generateMaze() {
		Maze maze = generator.generateMaze();

		return fromString(maze.toString());
	}

	public Maze fromString(String maze){
		Maze m = new Maze();

		Coord curCoord = new Coord(0, 0);
		m.removeField(curCoord);

		for(int i=0; i < maze.length(); i++){
			char c = maze.charAt(i);

			if(c != '?'){
				if(c == '\n'){
					curCoord = new Coord(0, curCoord.south().south().getY());
					continue;
				}

				m.putField(new Field(curCoord, Type.fromView(c)));
				m.putField(new Field(curCoord.east(), Type.fromView(c)));

				m.putField(new Field(curCoord.south(), Type.fromView(c)));
				m.putField(new Field(curCoord.south().east(), Type.fromView(c)));
			}

			curCoord = curCoord.east().east();
		}

		return m;
	}
}
