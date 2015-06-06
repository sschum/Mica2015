package de.tarent.mica.maze.bot.strategy.navigation;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import de.tarent.mica.maze.generator.MazePerforater;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tarent.mica.maze.generator.ButtonPositioner;
import de.tarent.mica.maze.generator.MazeGenerator;
import de.tarent.mica.maze.generator.PerfectMazeGenerator;
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

//	@Test
//	public void testRoom(){
//		final Maze maze = createRoom(4, 4);
//		final Coord start = new Coord(1, -2);
//		final Coord dest = new Coord(2, -1);
//
//		PathFinder pf = new PathFinder(maze, start, dest);
//
//		assertTrue(2 == pf.getRoutes().size());
//		assertTrue(pf.getRoutes().contains(
//			new LinkedList<>(Arrays.asList(start, new Coord(1, -1), dest))
//		));
//		assertTrue(pf.getRoutes().contains(
//			new LinkedList<>(Arrays.asList(start, new Coord(2, -2), dest))
//		));
//	}

	@Test
	public void testBigRoom(){
		final Maze maze = createRoom(100, 100);
		final Coord start = new Coord(2, -98);
		final Coord dest = new Coord(98, -2);

		PathFinder pf = new PathFinder(maze);
		List<Coord> route = pf.getRoute(start, dest);
		assertNotNull(route);
		assertFalse(route.isEmpty());
	}

	@Test
	public void testRealMaze(){
		final Maze maze = MazeBuilder.testMaze();

		final Coord start = maze.getPlayerField().getCoord();

		for(int i=0; i <= 9; i++){
			final Coord dest = maze.getButtonField(Type.getButton(i)).getCoord();

			PathFinder pf = new PathFinder(maze);
			List<Coord> route = pf.getRoute(start, dest);

			assertNotNull(route);
			assertFalse(route.isEmpty());
		}
	}

	@Test
	public void speedTest(){
		MazeGenerator gen = new ButtonPositioner(
				new MazePerforater(
						new PerfectMazeGenerator(100, 100), 5));

		Maze maze = gen.generateMaze();

		PathFinder pf = new PathFinder(maze);
		pf.getRoutes(maze.getButtonField(Type.BUTTON0).getCoord(),
			Arrays.asList(
					maze.getButtonField(Type.BUTTON6).getCoord(),
					maze.getButtonField(Type.BUTTON7).getCoord(),
					maze.getButtonField(Type.BUTTON8).getCoord(),
					maze.getButtonField(Type.BUTTON9).getCoord()
			));
	}
}
