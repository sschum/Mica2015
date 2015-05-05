package de.tarent.mica.maze.model;


public class World {
	Maze maze;
	Integer inventarButton;
	Integer lastPushedButton;
	Integer actionCount;

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
	public void swapButton() {
		final Field playerField = maze.getPlayerField();

		Integer invButton = inventarButton;

		if(playerField.hasButton()){
			inventarButton = playerField.getButtonType().getButtonNumber();
			playerField.removeButton();
		}

		if(invButton != null){
			playerField.setButton(Type.getButton(invButton));
			if(inventarButton == invButton){
				inventarButton = null;
			}
		}
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

	public Integer getActionCount() {
		return actionCount;
	}
	public void increasAction(){
		if(actionCount == null){
			actionCount = 0;
		}

		actionCount++;
	}

	@Override
	public String toString() {
		return "Inventar: " + (inventarButton != null ? inventarButton : "-") +
				" Last pushed: " + (lastPushedButton != null ? lastPushedButton : "-") +
				" Actions: " + (actionCount != null ? actionCount : "-") +
				"\n" + maze;
	}
}
