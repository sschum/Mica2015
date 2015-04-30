package de.tarent.mica.maze.test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;

public class MazeBuilder {

	public static Maze fromString(String maze){
		Maze m = new Maze();

		Coord curCoord = new Coord(0, 0);
		m.removeField(curCoord);

		for(int i=0; i < maze.length(); i++){
			char c = maze.charAt(i);

			if(c != '?'){
				if(c == '\n'){
					curCoord = new Coord(0, curCoord.south().getY());
					continue;
				}

				m.putField(new Field(curCoord, Type.fromView(c)));
			}

			curCoord = curCoord.east();
		}

		return m;
	}

	public static Maze testMaze(){
		return MazeBuilder.fromString(
				"#########################\n" +
				"#^      #3 #            #\n" +
				"####### #    ##### #### #\n" +
				"#  #  # ####  #       # #\n" +
				"# ### #    ## # ### # # #\n" +
				"#     # #  ######## #   #\n" +
				"# ##### ## #      # #####\n" +
				"#        # #   1  #   # #\n" +
				"###  ###   #      # ### #\n" +
				"#2#  #9### # ###### #8# #\n" +
				"# #  # #4           # # #\n" +
				"# #  # ###### # # # #   #\n" +
				"# #         #       ### #\n" +
				"# ###### ## # # # #     #\n" +
				"# #       # #    6  ### #\n" +
				"# # ####0#### # # # #   #\n" +
				"#   #  # #  #       # # #\n" +
				"# #   #   #  #######  # #\n" +
				"#  # #     #       # #  #\n" +
				"#####       #### # # ####\n" +
				"#5# # ####     # # #    #\n" +
				"# #      # ### # # ## ###\n" +
				"# ######## # # # #      #\n" +
				"#          #     # #7## #\n" +
				"#########################");
	}
}
