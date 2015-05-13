package de.tarent.mica.maze.bot.strategy.navigation;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.test.MazeBuilder;

public class GraphTest {

	private Maze testMaze() {
		final Maze m = MazeBuilder.fromString(
				"######\n" +
				"#    ###\n" +
				"#### # #\n" +
				"# 1 0  #\n" +
				"#  # ###\n" +
				"######\n");
		return m;
	}

	@Test
	public void detectCrossings(){
		final Maze m = MazeBuilder.testMaze();

		System.out.println(m);

		final Maze m2 = new Maze();

		System.out.println(Graph.detectCrossings(m));
		for(Coord c : Graph.detectCrossings(m)){
			m2.putField(c, Type.WAY);
		}

		System.out.println(m2);
	}

	@Test
	public void detectNeighborCrossings(){
		final Maze m = testMaze();

		/*
		 *
		 * ######
		 * #<<<<###
		 * ####^#^#
		 * # <<*>^#
		 * #  #v###
		 * ######
		 *
		 */
		Coord start = m.getButtonField(Type.BUTTON0).getCoord();
		Map<Coord, List<Coord>> crossings = Graph.detectNeighborCrossings(m, start);
		assertTrue(4 == crossings.size());
		assertTrue(crossings.containsKey(new Coord(4, -4)));
		assertTrue(crossings.containsKey(new Coord(2, -3)));
		assertTrue(crossings.containsKey(new Coord(1, -1)));
		assertTrue(crossings.containsKey(new Coord(6, -2)));

		/*
		 *
		 * ######
		 * #    ###
		 * #### # #
		 * #<*>>  #
		 * #^<# ###
		 * ######
		 *
		 */
		start = m.getButtonField(Type.BUTTON1).getCoord();
		crossings = Graph.detectNeighborCrossings(m, start);
		assertTrue(3 == crossings.size());
		assertTrue(crossings.containsKey(new Coord(2, -4)));
		assertTrue(crossings.containsKey(new Coord(4, -3)));
		assertTrue(crossings.containsKey(new Coord(1, -3)));
	}

	@Test
	public void testAnalyse(){
		testAnalyse(MazeBuilder.fromString(
				"##########\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"#        #\n" +
				"##########"));

		testAnalyse(MazeBuilder.fromString(
				"        \n" +
				"        \n" +
				"        \n" +
				"        \n" +
				"        \n" +
				"        \n" +
				"        \n" +
				"        "));

		testAnalyse(MazeBuilder.fromString(
				"######\n" +
				"#    ###\n" +
				"#### # #\n" +
				"# 1 0  #\n" +
				"#  # ###\n" +
				"######\n"));

		testAnalyse(MazeBuilder.fromString(
				   "   ?\n"+
				   "# #?\n"+
				   " 0 ?\n"+
				   "# #?\n"+
				   "   ?\n"+
				   "#< #\n"+
				   "?# ?"));

		testAnalyse(MazeBuilder.fromString(
				"?#####????\n" +
				"#     #???\n" +
				"# ### #???\n" +
				"# #   #???\n" +
				"# # ### ##\n" +
				"#<#       \n" +
				"#   ######\n" +
				"? # #?????\n" +
				"??# #?????\n" +
				"??#  ?????"));

		testAnalyse(MazeBuilder.fromString(
				"??????#?#???????????\n" +
				"?????  #  ??????????\n" +
				"?????# #  ??????????\n" +
				"?????# # #??????????\n" +
				"?????#    #?????????\n" +
				"?????# #  #######???\n" +
				" ##### ## #      #??\n" +
				"        # #      #??\n" +
				"##  ###   #      #??\n" +
				"???????## # #####???\n" +
				"??????#        0????\n" +
				"???????##### # # # ?\n" +
				"???????????#       ?\n" +
				"???????????# # # # ?\n" +
				"???????????#       #\n" +
				"???????????# # # # ?\n" +
				"???????????#v     ??\n" +
				"????????????#####???\n"));

		testAnalyse(MazeBuilder.fromString(
				"?######?\n" +
				"#> 0   ?\n" +
				"# #?#?#?\n" +
				"#      ?\n" +
				"# #?#?#?\n" +
				"#       \n" +
				"#   #?#?\n" +
				"#  ?????\n" +
				"?##?????"));
	}

	private void testAnalyse(final Maze maze) {
		List<Field> fields = maze.getWayFields();
		Graph g = new Graph(maze);

		for(Entry<Route, Edge> entry : g.nodes.entrySet()){
			remove(fields, entry.getValue().getStart());
			remove(fields, entry.getValue().getEnd());
			for(Coord coord : entry.getValue().getEdge()){
				remove(fields, coord);
			}
		}

		assertTrue(fields.toString(), fields.isEmpty());
	}

	private void remove(List<Field> fields, Coord c) {
		Iterator<Field> iter = fields.iterator();
		while(iter.hasNext()){
			if(iter.next().getCoord().equals(c)){
				iter.remove();
			}
		}
	}

	@Test
	public void getShortestWay_CrossingToCrossing(){
		/*
		 *
		 * ######
		 * #*   ###
		 * #### #*#
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*#*#
		 * #   ***#
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(6, -2), new Coord(1, -1));

		assertTrue(9 == way.size());
		assertEquals(new Coord(6, -2), way.get(0));
		assertEquals(new Coord(6, -3), way.get(1));
		assertEquals(new Coord(5, -3), way.get(2));
		assertEquals(new Coord(4, -3), way.get(3));
		assertEquals(new Coord(4, -2), way.get(4));
		assertEquals(new Coord(4, -1), way.get(5));
		assertEquals(new Coord(3, -1), way.get(6));
		assertEquals(new Coord(2, -1), way.get(7));
		assertEquals(new Coord(1, -1), way.get(8));
	}

	@Test
	public void getShortestWay_CrossingToCrossing2(){
		final Maze maze = MazeBuilder.fromString(
				"?##########?\n" +
				"#          #\n" +
				"# ######## #\n" +
				"# #??????# #\n" +
				" v#??????#6#\n" +
				"   ???????#?\n" +
				"#  ?????????\n" +
				"?#??????????");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(new Coord(1, -4), new Coord(0, -4));

		assertTrue(2 == way.size());
		assertEquals(new Coord(1, -4), way.get(0));
		assertEquals(new Coord(0, -4), way.get(1));
	}

	@Test
	public void getShortestWay_CrossingToEdge(){
		/*
		 *
		 * ######
		 * #*   ###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(1, -1), new Coord(4, -2));

		assertTrue(5 == way.size());
		assertEquals(new Coord(1, -1), way.get(0));
		assertEquals(new Coord(2, -1), way.get(1));
		assertEquals(new Coord(3, -1), way.get(2));
		assertEquals(new Coord(4, -1), way.get(3));
		assertEquals(new Coord(4, -2), way.get(4));
	}

	@Test
	public void getShortestWay_EdgeToCrossing(){
		/*
		 *
		 * ######
		 * #*   ###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 * ######
		 * #****###
		 * ####*# #
		 * #      #
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(4, -2), new Coord(1, -1));

		assertTrue(5 == way.size());
		assertEquals(new Coord(4, -2), way.get(0));
		assertEquals(new Coord(4, -1), way.get(1));
		assertEquals(new Coord(3, -1), way.get(2));
		assertEquals(new Coord(2, -1), way.get(3));
		assertEquals(new Coord(1, -1), way.get(4));
	}

	@Test
	public void getShortestWay_EdgeToCrossing2(){
		final Maze maze = MazeBuilder.fromString(
			   "   ?\n"+
			   "# #?\n"+
			   " 0 ?\n"+
			   "# #?\n"+
			   "   ?\n"+
			   "#< #\n"+
			   "?# ?");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(new Coord(1, -5), new Coord(2, -4));

		assertTrue(3 == way.size());
		assertEquals(new Coord(1, -5), way.get(0));
		assertEquals(new Coord(2, -5), way.get(1));
		assertEquals(new Coord(2, -4), way.get(2));
	}

	@Test
	public void getShortestWay_EdgeToCrossing3(){
		final Maze maze = MazeBuilder.fromString(
				"???#  ?????\n" +
				"???# #?????\n" +
				"???# # ##  \n" +
				"???# > 8   \n" +
				"?### # ##  \n" +
				"#2   #?????\n" +
				"?### #?????\n" +
				"???  #?????\n" +
				"???  #?????\n" +
				"????#??????");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(new Coord(5, -3), new Coord(5, 0));

		assertEquals(new Coord(5, -3), way.get(0));
	}

	@Test
	public void getShortestWay_EdgeToCrossing4(){
		final Maze maze = MazeBuilder.fromString(
			"??????#?#???????????\n" +
			"?????  #  ??????????\n" +
			"?????# #  ??????????\n" +
			"?????# # #??????????\n" +
			"?????#    #?????????\n" +
			"?????# #  #######???\n" +
			" ##### ## #      #??\n" +
			"        # #      #??\n" +
			"##  ###   #      #??\n" +
			"???????## # #####???\n" +
			"??????#        0????\n" +
			"???????##### # # # ?\n" +
			"???????????#       ?\n" +
			"???????????# # # # ?\n" +
			"???????????#       #\n" +
			"???????????# # # # ?\n" +
			"???????????#v     ??\n" +
			"????????????#####???\n");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(maze.getPlayerField().getCoord(), maze.getButtonField(Type.BUTTON0).getCoord());

		assertTrue(10 == way.size());
		assertEquals(new Coord(12, -16), way.get(0));
		assertEquals(new Coord(12, -15), way.get(1));
		assertEquals(new Coord(12, -14), way.get(2));
		assertEquals(new Coord(12, -13), way.get(3));
		assertEquals(new Coord(12, -12), way.get(4));
		assertEquals(new Coord(12, -11), way.get(5));
		assertEquals(new Coord(12, -10), way.get(6));
		assertEquals(new Coord(13, -10), way.get(7));
		assertEquals(new Coord(14, -10), way.get(8));
		assertEquals(new Coord(15, -10), way.get(9));
	}

	@Test
	public void getShortestWay_EdgeToCrossing5(){
		final Maze maze = MazeBuilder.fromString(
				"?######?\n" +
				"#> 0   ?\n" +
				"# #?#?#?\n" +
				"#      ?\n" +
				"# #?#?#?\n" +
				"#       \n" +
				"#   #?#?\n" +
				"#  ?????\n" +
				"?##?????");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(maze.getPlayerField().getCoord(), maze.getButtonField(Type.BUTTON0).getCoord());

		assertTrue(3 == way.size());
		assertEquals(new Coord(1, -1), way.get(0));
		assertEquals(new Coord(2, -1), way.get(1));
		assertEquals(new Coord(3, -1), way.get(2));

		way = graph.getShortestWay(maze.getButtonField(Type.BUTTON0).getCoord(), maze.getPlayerField().getCoord());

		assertTrue(3 == way.size());
		assertEquals(new Coord(3, -1), way.get(0));
		assertEquals(new Coord(2, -1), way.get(1));
		assertEquals(new Coord(1, -1), way.get(2));
	}

	@Test
	public void getShortestWay_EdgeToEdge(){
		/*
		 *
		 * ######
		 * # *  ###
		 * #### # #
		 * #     *#
		 * #  # ###
		 * ######
		 *
		 * ######
		 * # ***###
		 * ####*# #
		 * #   ***#
		 * #  # ###
		 * ######
		 *
		 */
		Graph g = new Graph(testMaze());

		List<Coord> way = g.getShortestWay(new Coord(2, -1), new Coord(6, -3));

		assertTrue(7 == way.size());
		assertEquals(new Coord(2, -1), way.get(0));
		assertEquals(new Coord(3, -1), way.get(1));
		assertEquals(new Coord(4, -1), way.get(2));
		assertEquals(new Coord(4, -2), way.get(3));
		assertEquals(new Coord(4, -3), way.get(4));
		assertEquals(new Coord(5, -3), way.get(5));
		assertEquals(new Coord(6, -3), way.get(6));
	}

	@Test
	public void getShortestWay_EdgeToEdge2(){
		final Maze maze = MazeBuilder.fromString(
				"?#######\n" +
				"# <     \n" +
				"? # # # ");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(new Coord(2, -1), new Coord(7, -1));

		assertTrue(6 == way.size());
		assertEquals(new Coord(2, -1), way.get(0));
		assertEquals(new Coord(3, -1), way.get(1));
		assertEquals(new Coord(4, -1), way.get(2));
		assertEquals(new Coord(5, -1), way.get(3));
		assertEquals(new Coord(6, -1), way.get(4));
		assertEquals(new Coord(7, -1), way.get(5));
	}

	@Test
	public void getShortestWay_EdgeToEdge3(){
		final Maze maze = MazeBuilder.fromString(
				"?#####????\n" +
				"#     #???\n" +
				"# ### #???\n" +
				"# #   #???\n" +
				"# # ### ##\n" +
				"#<#       \n" +
				"#   ######\n" +
				"? # #?????\n" +
				"??# #?????\n" +
				"??#  ?????");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(new Coord(1, -5), new Coord(3, -9));

		assertTrue(7 == way.size());
		assertEquals(new Coord(1, -5), way.get(0));
		assertEquals(new Coord(1, -6), way.get(1));
		assertEquals(new Coord(2, -6), way.get(2));
		assertEquals(new Coord(3, -6), way.get(3));
		assertEquals(new Coord(3, -7), way.get(4));
		assertEquals(new Coord(3, -8), way.get(5));
		assertEquals(new Coord(3, -9), way.get(6));

		way = graph.getShortestWay(new Coord(3, -9), new Coord(1, -5));

		assertTrue(7 == way.size());
		assertEquals(new Coord(3, -9), way.get(0));
		assertEquals(new Coord(3, -8), way.get(1));
		assertEquals(new Coord(3, -7), way.get(2));
		assertEquals(new Coord(3, -6), way.get(3));
		assertEquals(new Coord(2, -6), way.get(4));
		assertEquals(new Coord(1, -6), way.get(5));
		assertEquals(new Coord(1, -5), way.get(6));
	}

	@Test
	public void getShortestWay_EdgeToEdge4(){
		final Maze maze = MazeBuilder.fromString(
				"?#####?############?#?##?\n" +
				"#     #            # #  #\n" +
				"# ### ##### # # ## # # #?\n" +
				"# #   # #   # #   ##   #?\n" +
				"# # ### ##### ###    #  #\n" +
				"# #              ##### #?\n" +
				"#   ###### # # # #      #\n" +
				"#v#??#   #       # #####?\n" +
				"# #??#   # # # # #      #\n" +
				"# #??#   #       # #### #\n" +
				"# #??#   # # # # # #    #\n" +
				"# #??#   #       # # ## #\n" +
				"#0 ??#     ######  # #  #\n" +
				"????###### #   #  #  ###?\n" +
				"???#       # ### #      #\n" +
				"???#  ## # # # ##   ### #\n" +
				"?### ##  # #        # # #\n" +
				"#       #### # ##   # # #\n" +
				"# ##### #    # # #  # # #\n" +
				"# #???# #### # #  #   # #\n" +
				"# #???#      # ##  ## # #\n" +
				"# #???#      #    ##  # #\n" +
				"# #???# ######## # #### #\n" +
				"# #???  #          #    #\n" +
				"?#?????#?##########?####?");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(maze.getPlayerField().getCoord(), maze.getButtonField(Type.BUTTON0).getCoord());

		assertTrue(6 == way.size());
		assertEquals(new Coord(1, -7), way.get(0));
		assertEquals(new Coord(1, -8), way.get(1));
		assertEquals(new Coord(1, -9), way.get(2));
		assertEquals(new Coord(1, -10), way.get(3));
		assertEquals(new Coord(1, -11), way.get(4));
		assertEquals(new Coord(1, -12), way.get(5));
	}

	@Test
	public void getShortestWay_curvesWeightMore(){
		final Maze maze = MazeBuilder.fromString(
			"#######\n" +
			"#    0#\n" +
			"# # # #\n" +
			"#     #\n" +
			"# # # #\n" +
			"#^    #\n" +
			"#######");

		Graph graph = new Graph(maze);
		List<Coord> way = graph.getShortestWay(maze.getPlayerField().getCoord(), maze.getButtonField(Type.BUTTON0).getCoord());

		assertFalse(way.contains(new Coord(3, -3)));
	}
}
