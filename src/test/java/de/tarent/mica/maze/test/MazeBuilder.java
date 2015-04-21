package de.tarent.mica.maze.test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;

public class MazeBuilder {

	public static Maze fromString(String maze){
		Maze m = new Maze();

		Coord curCoord = new Coord(0, 0);

		for(int i=0; i < maze.length(); i++){
			char c = maze.charAt(i);

			if(c == '\n'){
				curCoord = new Coord(0, curCoord.south().getY());
				continue;
			}

			m.putField(new Field(curCoord, Type.fromView(c)));
			curCoord = curCoord.east();
		}

		return m;
	}
}
