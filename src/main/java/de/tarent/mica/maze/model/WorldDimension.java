package de.tarent.mica.maze.model;

public class WorldDimension {
	private int minX = 0;
	private int minY = 0;
	private int maxX = 0;
	private int maxY = 0;

	private int width = 0;
	private int height = 0;


	public int getMinX() {
		return minX;
	}
	public void setMinX(int minX) {
		this.minX = minX;
	}
	public int getMinY() {
		return minY;
	}
	public void setMinY(int minY) {
		this.minY = minY;
	}
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	public int getMaxY() {
		return maxY;
	}
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return height + "x" + width + " (" + minX + ";" + minY + " | " + maxX + ";" + maxY + ")";
	}
}
