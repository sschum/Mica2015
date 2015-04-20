package de.tarent.mica.maze.model;


public class World {
	Maze maze;
	Integer inventarButton;
	Integer lastPushedButton;

	public World() {
		maze = new Maze();
	}

	public World(Maze maze) {
		this.maze = maze;
	}

	public Maze getMaze() {
		return maze;
	}

	public Integer getInventarButton() {
		return inventarButton;
	}
	public void putButton() {
		final Field playerField = maze.getPlayerField();

		if(playerField.hasButton()){
			inventarButton = playerField.getButtonType().getButtonNumber();
			playerField.removeButton();
		}
	}
	public void dropButton() {
		if(inventarButton == null) return;

		final Field playerField = maze.getPlayerField();
		playerField.setButton(Type.getButton(inventarButton));

		this.inventarButton = null;
	}

	public Integer getLastPushedButton() {
		return lastPushedButton;
	}
	public void pushButton() {
		final Field playerField = maze.getPlayerField();

		if(playerField.hasButton()){
			Integer number = playerField.getButtonType().getButtonNumber();
			if(number != null){
				if(lastPushedButton == null){
					if(number == 0){
						lastPushedButton = 0;
					}
				}else{
					if(number == (lastPushedButton + 1)){
						lastPushedButton++;
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Inventar: " + (inventarButton != null ? inventarButton : "-") +
				" Last pushed: " + (lastPushedButton != null ? lastPushedButton : "-") +
				"\n" + maze;
	}
}
