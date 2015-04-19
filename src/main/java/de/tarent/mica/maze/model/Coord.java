package de.tarent.mica.maze.model;


public class Coord implements Comparable<Coord>{
	private int x;
	private int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/*
	 *         ^
	 *         |2
	 *         |1
	 * -3 -2 -1|0 1 2 3 4 5
	 * --------+------->
	 *         |-1
	 *         |-2
	 *         |-3
	 */

	/**
	 * Create a new {@link Coord} instance that represents the
	 * <b>north</b> neighbor of this instance.
	 *
	 * @return A new neighbor {@link Coord}.
	 */
	public Coord north(){
		return new Coord(x, y + 1);
	}

	/**
	 * Create a new {@link Coord} instance that represents the
	 * <b>east</b> neighbor of this instance.
	 *
	 * @return A new neighbor {@link Coord}.
	 */
	public Coord east(){
		return new Coord(x + 1, y);
	}

	/**
	 * Create a new {@link Coord} instance that represents the
	 * <b>south</b> neighbor of this instance.
	 *
	 * @return A new neighbor {@link Coord}.
	 */
	public Coord south(){
		return new Coord(x, y - 1);
	}

	/**
	 * Create a new {@link Coord} instance that represents the
	 * <b>west</b> neighbor of this instance.
	 *
	 * @return A new neighbor {@link Coord}.
	 */
	public Coord west(){
		return new Coord(x - 1, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coord other = (Coord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Coord o) {
		int c = Integer.compare(this.y, o.y);
		if(c == 0){
			c = Integer.compare(this.x, o.x);
		}

		return c;
	}

	@Override
	public String toString() {
		return "(" + x + ";" + y + ")";
	}
}
