package de.tarent.mica.maze.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Field {
	private Coord coord;
	private Collection<Type> types = new HashSet<>();

	public Field(Coord coord, Type...types) {
		super();
		this.coord = coord;
		this.types.addAll(Arrays.asList(types));
	}

	public Coord getCoord() {
		return coord;
	}
	public Collection<Type> getTypes() {
		return types;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return coord + " " + types;
	}
}
