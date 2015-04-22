package de.tarent.mica.maze.bot.strategy;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.test.MazeBuilder;

public class PathFinderTest {

	private Maze createRoom(int width, int height){
		StringBuilder sMaze = new StringBuilder();
		for(int i=0; i < height; i++){
			if(i==0 || i + 1 == height){
				sMaze.append(StringUtils.repeat("#", width) + "\n");
			}else{
				sMaze.append("#" + StringUtils.repeat(" ", width - 2) + "#\n");
			}
		}

		return MazeBuilder.fromString(sMaze.toString());
	}

	@Test
	public void testRoom(){
		final Maze maze = createRoom(4, 4);
		final Coord start = new Coord(1, -2);
		final Coord dest = new Coord(2, -1);

		PathFinder pf = new PathFinder(maze, start, dest);

		assertTrue(2 == pf.getRoutes().size());
		assertTrue(pf.getRoutes().contains(
			new LinkedList<>(Arrays.asList(start, new Coord(1, -1), dest))
		));
		assertTrue(pf.getRoutes().contains(
			new LinkedList<>(Arrays.asList(start, new Coord(2, -2), dest))
		));
	}

	@Test
	public void testBigRoom(){
		final Maze maze = createRoom(100, 100);
		final Coord start = new Coord(1, -98);
		final Coord dest = new Coord(98, -1);

		PathFinder pf = new PathFinder(maze, start, dest, TimeUnit.SECONDS, 1);
		assertTrue(pf.getRoutes().size() >= 1);
	}

	@Test
	public void testRealMaze(){
		final Maze maze = MazeBuilder.fromString(
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

		for(int i=0; i <= 9; i++){
			final Coord start = maze.getPlayerField().getCoord();
			final Coord dest = maze.getButtonField(Type.getButton(i)).getCoord();

			PathFinder pf = new PathFinder(maze, start, dest, TimeUnit.MILLISECONDS, 500);
			assertTrue(pf.getRoutes().size() >= 1);
		}
	}
}
