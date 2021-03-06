package de.tarent.mica.maze.bot.strategy.navigation;

import de.tarent.mica.maze.model.Coord;

class Route {
	private final Coord start;
	private final Coord end;

	public Route(Coord start, Coord end) {
		this.start = start;
		this.end = end;
	}

	public Coord getStart() {
		return start;
	}

	public Coord getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Route other = (Route) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return start + " -> " + end;
	}
}