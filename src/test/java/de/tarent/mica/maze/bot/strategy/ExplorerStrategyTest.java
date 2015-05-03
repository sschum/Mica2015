package de.tarent.mica.maze.bot.strategy;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.strategy.ExplorerStrategy;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.test.MazeBuilder;

public class ExplorerStrategyTest {

	ExplorerStrategy toTest;

	@Before
	public void setup(){
		toTest = new ExplorerStrategy(new PathWalkerStrategy());
	}

	@Test
	public void isDiscovered(){
		World world = new World();

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_allInWorld(){
		World world = new World();
		for(int i=0; i < 10; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		assertTrue(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_oneInInventory(){
		World world = new World();
		for(int i=1; i < 10; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.putButton();

		assertTrue(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_onePushed(){
		World world = new World();
		for(int i=1; i < 10; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.pushButton();

		assertTrue(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_fourPushed(){
		World world = new World(MazeBuilder.fromString(
			"?####????????????#####???\n" +
			"#   >#??????????#     #??\n" +
			"# ###???????##### # # #??\n" +
			"# #  #???? #      # ##???\n" +
			"# # ##???# #      # # #??\n" +
			"# #   #??# # #### #   #??\n" +
			"# # #  #?# #   2# #####??\n" +
			"# # #   ## # ####      ??\n" +
			"# # #    7   #6#  ##?###?\n" +
			"# ###   ## # # # ##??# 3#\n" +
			"#      #??## #      ?#  #\n" +
			"?###  #??#   # #####?# #?\n" +
			"#  # # ??#####     ##   #\n" +
			"# ## # #?      #  ?#  # #\n" +
			"#    # # # # # #  ??### #\n" +
			"# #### #       # 1?#  # #\n" +
			"#      # # # # #  ??# # #\n" +
			"?##### #  9    #  ??# # #\n" +
			"#      # # # # ######   #\n" +
			"?# #####              # #\n" +
			"#0 #    ### ##### ### # #\n" +
			"?#   ##   # #  8# #   # #\n" +
			"?# # #?## # # ##### ### #\n" +
			"?? # #            #     #\n" +
			"????#?############?#####?"));

		world = spy(world);
		doReturn(4).when(world).getLastPushedButton();

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_onePushedAndOneInInventory(){
		World world = new World();
		for(int i=1; i < 10; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.pushButton();
		world.getMaze().getPlayerField().removePlayer();
		world.getMaze().getField(new Coord(1, 0)).setPlayer(Type.PLAYER_EAST);
		world.putButton();

		assertTrue(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_notEnough_allInWorld(){
		World world = new World();
		for(int i=0; i < 5; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_notEnough_oneInInventory(){
		World world = new World();
		for(int i=1; i < 5; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.putButton();

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_notEnough_onePushed(){
		World world = new World();
		for(int i=1; i < 5; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.pushButton();

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDiscovered_notEnough_onePushedAndOneInInventory(){
		World world = new World();
		for(int i=1; i < 5; i++) world.getMaze().putField(new Coord(i, 0), Type.getButton(i));

		world.getMaze().getPlayerField().setButton(Type.BUTTON0);
		world.pushButton();
		world.getMaze().getPlayerField().removePlayer();
		world.getMaze().getField(new Coord(1, 0)).setPlayer(Type.PLAYER_EAST);
		world.putButton();

		assertFalse(toTest.isDiscovered(world));
	}

	@Test
	public void isDarkAroundMe(){
		World world = new World();

		assertTrue(toTest.isDarkAroundMe(world));
	}

	@Test
	public void isDarkAroundMe_eastDark(){
		World world = new World(MazeBuilder.fromString("###\n#^\n###"));

		assertTrue(toTest.isDarkAroundMe(world));
	}

	@Test
	public void isDarkAroundMe_southDark(){
		World world = new World(MazeBuilder.fromString("###\n#^#\n#"));

		assertTrue(toTest.isDarkAroundMe(world));
	}

	@Test
	public void isDarkAroundMe_westDark(){
		World world = new World(MazeBuilder.fromString("###\n^##\n###"));

		assertTrue(toTest.isDarkAroundMe(world));
	}

	@Test
	public void isDarkAroundMe_northDark(){
		World world = new World(MazeBuilder.fromString("#^#\n###"));

		assertTrue(toTest.isDarkAroundMe(world));
	}

	@Test
	public void isDarkAroundMe_surrounded(){
		World world = new World(MazeBuilder.fromString("###\n#^#\n###"));

		assertFalse(toTest.isDarkAroundMe(world));
	}

	@Test
	public void turnToDarkOrLookInto_lookNorth(){
		World world = new World(MazeBuilder.fromString("#^#\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof Look);
	}

	@Test
	public void turnToDarkOrLookInto_lookEast(){
		World world = new World(MazeBuilder.fromString("##>\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof Look);
	}

	@Test
	public void turnToDarkOrLookInto_lookSouth(){
		World world = new World(MazeBuilder.fromString("###\n#v#"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof Look);
	}

	@Test
	public void turnToDarkOrLookInto_lookWest(){
		World world = new World(MazeBuilder.fromString("<##\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof Look);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lookNorth(){
		World world = new World(MazeBuilder.fromString("###\n##^"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lookEast(){
		World world = new World(MazeBuilder.fromString("###\n#>#"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lookSouth(){
		World world = new World(MazeBuilder.fromString("v##\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lookWest(){
		World world = new World(MazeBuilder.fromString("#<#\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnLeft_lookNorth(){
		World world = new World(MazeBuilder.fromString("###\n^##"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnLeft);
	}

	@Test
	public void turnToDarkOrLookInto_TurnLeft_lookEast(){
		World world = new World(MazeBuilder.fromString("#>#\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnLeft);
	}

	@Test
	public void turnToDarkOrLookInto_TurnLeft_lookSouth(){
		World world = new World(MazeBuilder.fromString("##v\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnLeft);
	}

	@Test
	public void turnToDarkOrLookInto_TurnLeft_lookWest(){
		World world = new World(MazeBuilder.fromString("###\n#<#"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnLeft);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lastOption_lookNorth(){
		World world = new World(MazeBuilder.fromString("###\n#^#"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lastOption_lookEast(){
		World world = new World(MazeBuilder.fromString("###\n>##\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lastOption_lookSouth(){
		World world = new World(MazeBuilder.fromString("#v#\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}

	@Test
	public void turnToDarkOrLookInto_TurnRight_lastOption_lookWest(){
		World world = new World(MazeBuilder.fromString("###\n##<\n###"));

		assertTrue(toTest.turnToDarkOrLookInto(world) instanceof TurnRight);
	}
}
