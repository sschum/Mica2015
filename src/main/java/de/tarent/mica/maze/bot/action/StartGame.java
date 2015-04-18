package de.tarent.mica.maze.bot.action;

public class StartGame extends Action {

	private String playerName;

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
}
