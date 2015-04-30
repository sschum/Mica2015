package de.tarent.mica.maze.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Field {
	private final Coord coord;
	private Set<Type> types = new HashSet<>();

	public Field(Coord coord, Type...types) {
		this(coord, Arrays.asList(types));
	}

	public Field(Coord coord, Collection<Type> types) {
		this.coord = coord;
		this.types = new HashSet<>(types);

		purge();
		validate();
	}

	private void purge() {
		Iterator<Type> iter = types.iterator();
		while(iter.hasNext()){
			if(iter.next() == null){
				iter.remove();
			}
		}
	}

	private void validate() {
		boolean isWay = hasButton() || hasPlayer();

		if(isWay){
			types.add(Type.WAY);
		}
	}

	public boolean hasPlayer(){
		return getPlayerType() != null;
	}

	public boolean hasButton(){
		return getButtonType() != null;
	}

	public boolean isWall() {
		for(Type t : getTypes()){
			if(t == Type.WALL){
				return true;
			}
		}

		return false;
	}

	public Coord getCoord() {
		return coord;
	}
	public Collection<Type> getTypes() {
		return types;
	}

	public Type getPlayerType() {
		for(Type t : types){
			if(t.isPlayer()){
				return t;
			}
		}
		return null;
	}

	public Type getButtonType() {
		for(Type t : types){
			if(t.isButton()){
				return t;
			}
		}
		return null;
	}

	public void removeButton() {
		Iterator<Type> iter = types.iterator();
		while(iter.hasNext()){
			if(iter.next().isButton()){
				iter.remove();
			}
		}
	}

	public void setButton(Type button) {
		removeButton();
		types.add(button);
	}

	public void removePlayer() {
		Iterator<Type> iter = types.iterator();
		while(iter.hasNext()){
			if(iter.next().isPlayer()){
				iter.remove();
			}
		}
	}

	public void setPlayer(Type playerType) {
		removePlayer();
		types.add(playerType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (coord == null) {
			if (other.coord != null)
				return false;
		} else if (!coord.equals(other.coord))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return coord + " " + types;
	}
}
