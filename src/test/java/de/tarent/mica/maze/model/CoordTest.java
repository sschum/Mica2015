package de.tarent.mica.maze.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordTest {

	@Test
	public void north(){
		assertEquals(new Coord(0, 1), new Coord(0, 0).north());
	}

	@Test
	public void east(){
		assertEquals(new Coord(1, 0), new Coord(0, 0).east());
	}

	@Test
	public void south(){
		assertEquals(new Coord(0, -1), new Coord(0, 0).south());
	}

	@Test
	public void west(){
		assertEquals(new Coord(0-1, 0), new Coord(0, 0).west());
	}
}
