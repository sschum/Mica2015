package de.tarent.mica.maze.bot.action;

import de.tarent.mica.maze.model.Maze;

public class StartGame extends Action {
	private String playerName;
	private Maze maze;

	public StartGame(String playerName) {
		super();
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
	}

	public Maze getMaze() {
		return maze;
	}
}
