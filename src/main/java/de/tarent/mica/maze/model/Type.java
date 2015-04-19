package de.tarent.mica.maze.model;

public enum Type {
	PLAYER_NORTH('^'),
	PLAYER_EAST('>'),
	PLAYER_SOUTH('v'),
	PLAYER_WEST('<'),

	WAY(' '),
	WALL('#'),
	BUTTON('*');

	private char view;

	private Type(char view) {
		this.view = view;
	}

	public char getView() {
		return view;
	}

	@Override
	public String toString() {
		return "" + getView();
	}
}
