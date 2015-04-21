package de.tarent.mica.maze.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Maze {
	private SortedMap<Coord, Field> maze = new TreeMap<>();

	public Maze(){
		this(new Field(new Coord(0, 0), Type.PLAYER_NORTH));
	}

	public Maze(Field...fields) {
		for(Field f : fields){
			putField(f);
		}
	}

	public static SortedMap<Coord, Field> generateEmpty(WorldDimension dim){
		SortedMap<Coord, Field> result = new TreeMap<>();

		int x = dim.getMinX();
		int y = dim.getMinY();

		for(int i=0; i < dim.getHeight(); i++){

			x=dim.getMinX();
			for(int j=0; j < dim.getWidth(); j++){
				Field f = new Field(new Coord(x, y), Type.UNKNOWN);
				result.put(f.getCoord(), f);

				x++;
			}

			y++;
		}

		return result;
	}


	public void putField(Field field){
		maze.put(field.getCoord(), field);
	}

	public void putField(Coord coord, Type...types){
		putField(new Field(coord, types));
	}

	public Field getPlayerField(){
		for(Field f : maze.values()){
			for(Type t : f.getTypes()){
				switch(t){
					case PLAYER_EAST: case PLAYER_NORTH:
					case PLAYER_SOUTH: case PLAYER_WEST: {
						return f;
					}
					default:{
						break;
					}
				}
			}
		}

		return null;
	}

	public List<Field> getButtonFields() {
		List<Field> result = new ArrayList<Field>();

		for(Field f : maze.values()){
			for(Type t : f.getTypes()){
				if(t.isButton()){
					result.add(f);
				}
			}
		}

		return result;
	}

	public boolean hasField(Coord newCoord) {
		return maze.containsKey(newCoord);
	}

	public Field getField(Coord newCoord) {
		return maze.get(newCoord);
	}

	public WorldDimension getDimension(){
		WorldDimension dim = new WorldDimension();

		if(maze.isEmpty()){
			return dim;
		}

		Coord c = maze.values().iterator().next().getCoord();
		int minX = c.getX();
		int maxX = c.getX();

		int minY = c.getY();
		int maxY = c.getY();

		for(Field f : maze.values()){
			if(minX > f.getCoord().getX()){
				minX = f.getCoord().getX();
			}
			if(minY > f.getCoord().getY()){
				minY = f.getCoord().getY();
			}
			if(maxX < f.getCoord().getX()){
				maxX = f.getCoord().getX();
			}
			if(maxY < f.getCoord().getY()){
				maxY = f.getCoord().getY();
			}
		}

		dim.setMinX(minX);
		dim.setMinY(minY);
		dim.setMaxX(maxX);
		dim.setMaxY(maxY);

		if(minX < 0){
			int d = Math.abs(minX);
			minX += d;
			maxX += d;
		}
		if(minY < 0){
			int d = Math.abs(minY);
			minY += d;
			maxY += d;
		}

		dim.setWidth(maxX - minX + 1);
		dim.setHeight(maxY - minY + 1);

		return dim;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		SortedMap<Coord, Field> toPrint = generateEmpty(getDimension());
		toPrint.putAll(maze);

		Integer y = null;

		for(Field f : toPrint.values()){
			Type type = null;

			if(f.getTypes().size() == 1){
				type = f.getTypes().iterator().next();
			}else{
				if(f.hasPlayer()){
					type = f.getPlayerType();
				}else if(f.hasButton()){
					type = f.getButtonType();
				}else {
					throw new IllegalStateException("The model seams to be invalid!");
				}
			}

			if(y != null && y != f.getCoord().getY()){
				sb.append("\n");
			}
			y = f.getCoord().getY();

			sb.append(type.getView());
		}

		return sb.toString();
	}
}
