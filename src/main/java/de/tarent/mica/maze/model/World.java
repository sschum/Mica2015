package de.tarent.mica.maze.model;


public class World {
	private Maze maze;

	public World() {
		maze = new Maze();
	}

	public World(Maze maze) {
		this.maze = maze;
	}

	public Maze getMaze() {
		return maze;
	}
}
