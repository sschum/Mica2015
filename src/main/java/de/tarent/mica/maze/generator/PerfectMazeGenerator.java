package de.tarent.mica.maze.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.util.Random;

/**
 * This class is responsible for generating a perfect maze.
 * A perfect maze is defined as a maze which has one and only one path
 * from any point in the maze to any other point. This means that
 * the maze has no inaccessible sections, no circular paths, no open areas.
 *
 * @see <a href="http://www.mazeworks.com/mazegen/mazetut/index.htm">mazeworks.com</a>
 *
 * @author rainu
 */
public class PerfectMazeGenerator implements MazeGenerator {
	private final Maze emptyGrid;

	public PerfectMazeGenerator(int height, int width){
		this.emptyGrid = generateFrame(height, width);
	}

	private Maze generateFrame(int height, int width) {
		Maze maze = new Maze();

		for(int x=0; x < width; x++){
			maze.putField(new Coord(x, 0), Type.WALL);
			maze.putField(new Coord(x, height - 1), Type.WALL);
		}

		for(int y=1; y < height; y++){
			maze.putField(new Coord(0, y), Type.WALL);
			maze.putField(new Coord(width - 1, y), Type.WALL);
		}

		for(int y=1; y < height - 1; y++){
			for(int x=1; x < width - 1; x++){
				final Coord coord = new Coord(x, y);

				if(y % 2 == 0 || x % 2 == 0){
					maze.putField(new Field(coord, Type.WALL));
				}else{
					maze.putField(new Field(coord, Type.WAY));
				}
			}
		}
		return maze;
	}

	public Maze generateMaze(){
		Maze maze = emptyGrid.clone();

		List<Coord> cellStack = new LinkedList<Coord>();
		final List<Coord> emptyCells = getEmptyCells(maze);
		final int totalCells = emptyCells.size();
		int visitedCells = 1;
		Coord currentCell = getRandom(emptyCells);

		while(visitedCells < totalCells){
			List<Coord> neighbors = getNeighbors(maze, currentCell);
			if(!neighbors.isEmpty()){
				Coord neighbor = getRandom(neighbors);
				breakWallDown(maze, currentCell, neighbor);
				cellStack.add(currentCell);

				currentCell = neighbor;
				visitedCells++;
			}else{
				currentCell = cellStack.get(cellStack.size() - 1);
				cellStack.remove(cellStack.size() - 1);
			}
		}

		return maze;
	}

	private List<Coord> getEmptyCells(Maze maze) {
		final List<Field> fields = maze.getWayFields();
		List<Coord> result = new ArrayList<Coord>(fields.size());

		for(Field f : fields){
			result.add(f.getCoord());
		}

		return result;
	}

	private Coord getRandom(List<Coord> emptyCells) {
		Random r = new Random();

		return r.choose(emptyCells);
	}

	private List<Coord> getNeighbors(Maze maze, Coord currentCell) {
		List<Coord> neighbors = new ArrayList<Coord>(4);

		neighbors.add(currentCell.north().north());
		neighbors.add(currentCell.east().east());
		neighbors.add(currentCell.south().south());
		neighbors.add(currentCell.west().west());

		Iterator<Coord> iter = neighbors.iterator();
		while(iter.hasNext()){
			Coord neighbor = iter.next();

			if(!maze.hasField(neighbor) || maze.getField(neighbor).isWall()){
				//out of maze
				iter.remove();
				continue;
			}

			if(	!maze.getField(neighbor.north()).isWall() ||
				!maze.getField(neighbor.east()).isWall() ||
				!maze.getField(neighbor.south()).isWall() ||
				!maze.getField(neighbor.west()).isWall()){

				iter.remove();
			}
		}

		return neighbors;
	}

	private void breakWallDown(Maze maze, Coord cell, Coord neighbor) {
		if(cell.north().north().equals(neighbor)){
			maze.putField(new Field(cell.north(), Type.WAY));
		}else if(cell.east().east().equals(neighbor)){
			maze.putField(new Field(cell.east(), Type.WAY));
		}else if(cell.south().south().equals(neighbor)){
			maze.putField(new Field(cell.south(), Type.WAY));
		}else if(cell.west().west().equals(neighbor)){
			maze.putField(new Field(cell.west(), Type.WAY));
		}
	}
}
