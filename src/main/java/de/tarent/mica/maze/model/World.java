package de.tarent.mica.maze.model;

import java.util.SortedMap;
import java.util.TreeMap;

public class World {

	private SortedMap<Coord, Field> maze = new TreeMap<>();

	public World(Field...fields) {
		for(Field f : fields){
			putField(f);
		}
	}

	public void putField(Field field){
		maze.put(field.getCoord(), field);
	}

	public void putField(Coord coord, Type...types){
		putField(new Field(coord, types));
	}

	public Coord getPlayerCoord(){
		for(Field f : maze.values()){
			for(Type t : f.getTypes()){
				switch(t){
					case PLAYER_EAST: case PLAYER_NORTH:
					case PLAYER_SOUTH: case PLAYER_WEST: {
						return f.getCoord();
					}
					default:{
						break;
					}
				}
			}
		}

		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Integer y = null;

		for(Field f : maze.values()){
			Type type = null;

			if(f.getTypes().size() == 1){
				type = f.getTypes().iterator().next();
			}else{
				loop: for(Type t : f.getTypes()){
					switch(t){
						case PLAYER_EAST: case PLAYER_NORTH:
						case PLAYER_SOUTH: case PLAYER_WEST: {
							type = t;
							break loop;
						}
						default:{
							type = null;
							break;
						}
					}
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
