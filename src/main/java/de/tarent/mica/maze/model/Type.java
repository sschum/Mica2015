package de.tarent.mica.maze.model;

public enum Type {
	PLAYER_NORTH('^', true, false),
	PLAYER_EAST('>', true, false),
	PLAYER_SOUTH('v', true, false),
	PLAYER_WEST('<', true, false),

	WAY(' ', false, false),
	WALL('#', false, false),
	UNKNOWN('?', false, false),

	BUTTON0('0', false, true),
	BUTTON1('1', false, true),
	BUTTON2('2', false, true),
	BUTTON3('3', false, true),
	BUTTON4('4', false, true),
	BUTTON5('5', false, true),
	BUTTON6('6', false, true),
	BUTTON7('7', false, true),
	BUTTON8('8', false, true),
	BUTTON9('9', false, true);

	private char view;
	private boolean isPlayer;
	private boolean isButton;

	private Type(char view, boolean isPlayer, boolean isButton) {
		this.view = view;
		this.isPlayer = isPlayer;
		this.isButton = isButton;
	}

	public char getView() {
		return view;
	}

	public boolean isButton() {
		return isButton;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public Integer getButtonNumber(){
		if(this.name().startsWith("BUTTON")){
			return Integer.parseInt(this.name().replace("BUTTON", ""));
		}

		return null;
	}

	public static Type getButton(Integer btnNumber){
		if(btnNumber == null) return null;

		return Type.valueOf("BUTTON" + btnNumber);
	}

	@Override
	public String toString() {
		return "" + getView();
	}
}
