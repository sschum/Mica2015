package de.tarent.mica.maze.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
	BUTTON9('9', false, true),

	POINT('*', false, false);

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

	public static Type fromView(char view){
		for(Type t : Type.values()){
			if(t.getView() == view){
				return t;
			}
		}

		return null;
	}

	public static Iterable<Type> getButtons(){
		List<Type> buttons = new ArrayList<Type>(Arrays.asList(values()));
		Iterator<Type> iter = buttons.iterator();

		while(iter.hasNext()){
			if(!iter.next().isButton){
				iter.remove();
			}
		}

		return buttons;
	}

	@Override
	public String toString() {
		return "" + getView();
	}
}
