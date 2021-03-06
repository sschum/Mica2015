package de.tarent.mica.maze.bot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.LookActionSuccess;
import de.tarent.mica.maze.bot.strategy.Strategy;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Maze;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;

public class RobotImplTest {

	private RobotImpl toTest;

	@Before
	public void setup(){
		toTest = new RobotImpl("rainu", mock(Strategy.class));
	}

	@Test
	public void testHandleWalked_movedNorth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_NORTH)));

		toTest.handleWalked(new ActionSuccess());
		assertEquals(new Field(player.north(), Type.PLAYER_NORTH), toTest.world.getMaze().getPlayerField());
		assertEquals(new Field(player, Type.WAY), toTest.world.getMaze().getField(player));

		assertEquals(
				toTest.world.getMaze().toString(),
				"^\n" +
				" ");
	}

	@Test
	public void testHandleWalked_movedEast(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_EAST)));

		toTest.handleWalked(new ActionSuccess());
		assertEquals(new Field(player.east(), Type.PLAYER_EAST), toTest.world.getMaze().getPlayerField());
		assertEquals(new Field(player, Type.WAY), toTest.world.getMaze().getField(player));

		assertEquals(
				toTest.world.getMaze().toString(),
				" >");
	}

	@Test
	public void testHandleWalked_movedSouth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_SOUTH)));

		toTest.handleWalked(new ActionSuccess());
		assertEquals(new Field(player.south(), Type.PLAYER_SOUTH), toTest.world.getMaze().getPlayerField());
		assertEquals(new Field(player, Type.WAY), toTest.world.getMaze().getField(player));

		assertEquals(
				toTest.world.getMaze().toString(),
				" \n" +
				"v");
	}

	@Test
	public void testHandleWalked_movedWest(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_WEST)));

		toTest.handleWalked(new ActionSuccess());
		assertEquals(new Field(player.west(), Type.PLAYER_WEST), toTest.world.getMaze().getPlayerField());
		assertEquals(new Field(player, Type.WAY), toTest.world.getMaze().getField(player));

		assertEquals(
				toTest.world.getMaze().toString(),
				"< ");
	}

	@Test
	public void testHandleTurnedLeft_fromNorth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_NORTH)));

		toTest.handleTurnedLeft(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_WEST), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"<");
	}

	@Test
	public void testHandleTurnedLeft_fromEast(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_EAST)));

		toTest.handleTurnedLeft(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_NORTH), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"^");
	}

	@Test
	public void testHandleTurnedLeft_fromSouth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_SOUTH)));

		toTest.handleTurnedLeft(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_EAST), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				">");
	}

	@Test
	public void testHandleTurnedLeft_fromWest(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_WEST)));

		toTest.handleTurnedLeft(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_SOUTH), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"v");
	}

	@Test
	public void testHandleTurnedRight_fromNorth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_NORTH)));

		toTest.handleTurnedRight(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_EAST), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				">");
	}

	@Test
	public void testHandleTurnedRight_fromEast(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_EAST)));

		toTest.handleTurnedRight(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_SOUTH), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"v");
	}

	@Test
	public void testHandleTurnedRight_fromSouth(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_SOUTH)));

		toTest.handleTurnedRight(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_WEST), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"<");
	}

	@Test
	public void testHandleTurnedRight_fromWest(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_WEST)));

		toTest.handleTurnedRight(new ActionSuccess());
		assertEquals(new Field(player, Type.PLAYER_NORTH), toTest.world.getMaze().getPlayerField());

		assertEquals(
				toTest.world.getMaze().toString(),
				"^");
	}

	@Test
	public void testHandleLooked_north(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_NORTH)));

		/*
		 *  #
		 *
		 *   #
		 * #1#
		 * #
		 *  ^
		 */

		LookActionSuccess event = new LookActionSuccess();
		event.setFields(new ArrayList<LookActionSuccess.Field>());
		event.getFields().add(new LookActionSuccess.Field(false, true, null));
		event.getFields().add(new LookActionSuccess.Field(false, false, 1));
		event.getFields().add(new LookActionSuccess.Field(true, false, null));
		event.getFields().add(new LookActionSuccess.Field(true, true, null));
		event.getFields().add(new LookActionSuccess.Field());

		toTest.handleLooked(event);

		assertEquals(
				new Field(new Coord(-1, 1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-1, 1)));
		assertEquals(
				new Field(new Coord(0, 1), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, 1)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(1, 1)));

		assertEquals(
				new Field(new Coord(-1, 2), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-1, 2)));
		assertEquals(
				new Field(new Coord(0, 2), Type.BUTTON1),
				toTest.world.getMaze().getField(new Coord(0, 2)));
		assertEquals(
				new Field(new Coord(1, 2), Type.WALL),
				toTest.world.getMaze().getField(new Coord(1, 2)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(-1, 3)));
		assertEquals(
				new Field(new Coord(0, 3), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, 3)));
		assertEquals(
				new Field(new Coord(1, 3), Type.WALL),
				toTest.world.getMaze().getField(new Coord(1, 3)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(-1, 4)));
		assertEquals(
				new Field(new Coord(0, 4), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, 4)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(1, 4)));

		assertEquals(
				new Field(new Coord(0, 5), Type.WALL),
				toTest.world.getMaze().getField(new Coord(0, 5)));

		assertEquals(
				toTest.world.getMaze().toString(),
				"?#?\n" +
				"? ?\n" +
				"? #\n" +
				"#1#\n" +
				"# ?\n" +
				"?^?");
	}

	@Test
	public void testHandleLooked_east(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_EAST)));

		/*
		 *   ##
		 *  > 1  #
		 *    ##
		 */

		LookActionSuccess event = new LookActionSuccess();
		event.setFields(new ArrayList<LookActionSuccess.Field>());
		event.getFields().add(new LookActionSuccess.Field(false, true, null));
		event.getFields().add(new LookActionSuccess.Field(false, false, 1));
		event.getFields().add(new LookActionSuccess.Field(true, false, null));
		event.getFields().add(new LookActionSuccess.Field(true, true, null));
		event.getFields().add(new LookActionSuccess.Field());

		toTest.handleLooked(event);

		assertEquals(
				new Field(new Coord(1, 1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(1, 1)));
		assertEquals(
				new Field(new Coord(1, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(1, 0)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(1, -1)));

		assertEquals(
				new Field(new Coord(2, 1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(2, 1)));
		assertEquals(
				new Field(new Coord(2, 0), Type.BUTTON1),
				toTest.world.getMaze().getField(new Coord(2, 0)));
		assertEquals(
				new Field(new Coord(2, -1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(2, -1)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(3, 1)));
		assertEquals(
				new Field(new Coord(3, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(3, 0)));
		assertEquals(
				new Field(new Coord(3, -1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(3, -1)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(4, 1)));
		assertEquals(
				new Field(new Coord(4, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(4, 0)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(4, -1)));

		assertEquals(
				new Field(new Coord(5, 0), Type.WALL),
				toTest.world.getMaze().getField(new Coord(5, 0)));

		assertEquals(
				toTest.world.getMaze().toString(),
				"?##???\n" +
				"> 1  #\n" +
				"??##??");
	}

	@Test
	public void testHandleLooked_south(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_SOUTH)));

		/*
		 *  v
		 *   #
		 * #1#
		 * #
		 *
		 *  #
		 */

		LookActionSuccess event = new LookActionSuccess();
		event.setFields(new ArrayList<LookActionSuccess.Field>());
		event.getFields().add(new LookActionSuccess.Field(false, true, null));
		event.getFields().add(new LookActionSuccess.Field(false, false, 1));
		event.getFields().add(new LookActionSuccess.Field(true, false, null));
		event.getFields().add(new LookActionSuccess.Field(true, true, null));
		event.getFields().add(new LookActionSuccess.Field());

		toTest.handleLooked(event);

		assertNull(
				toTest.world.getMaze().getField(new Coord(-1, -1)));
		assertEquals(
				new Field(new Coord(0, -1), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, -1)));
		assertEquals(
				new Field(new Coord(1, -1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(1, -1)));

		assertEquals(
				new Field(new Coord(-1, -2), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-1, -2)));
		assertEquals(
				new Field(new Coord(0, -2), Type.BUTTON1),
				toTest.world.getMaze().getField(new Coord(0, -2)));
		assertEquals(
				new Field(new Coord(1, -2), Type.WALL),
				toTest.world.getMaze().getField(new Coord(1, -2)));

		assertEquals(
				new Field(new Coord(-1, -3), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-1, -3)));
		assertEquals(
				new Field(new Coord(0, -3), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, -3)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(1, -3)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(-1, -4)));
		assertEquals(
				new Field(new Coord(0, -4), Type.WAY),
				toTest.world.getMaze().getField(new Coord(0, -4)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(1, -4)));

		assertEquals(
				new Field(new Coord(0, -5), Type.WALL),
				toTest.world.getMaze().getField(new Coord(0, -5)));

		assertEquals(
				toTest.world.getMaze().toString(),
				"?v?\n" +
				"? #\n" +
				"#1#\n" +
				"# ?\n" +
				"? ?\n" +
				"?#?");
	}

	@Test
	public void testHandleLooked_west(){
		final Coord player = new Coord(0,0);
		toTest.world = new World(new Maze(new Field(player, Type.PLAYER_WEST)));

		/*
		 *    ##
		 *  #  1 <
		 *     ##
		 */

		LookActionSuccess event = new LookActionSuccess();
		event.setFields(new ArrayList<LookActionSuccess.Field>());
		event.getFields().add(new LookActionSuccess.Field(false, true, null));
		event.getFields().add(new LookActionSuccess.Field(false, false, 1));
		event.getFields().add(new LookActionSuccess.Field(true, false, null));
		event.getFields().add(new LookActionSuccess.Field(true, true, null));
		event.getFields().add(new LookActionSuccess.Field());

		toTest.handleLooked(event);

		assertNull(
				toTest.world.getMaze().getField(new Coord(-1, 1)));
		assertEquals(
				new Field(new Coord(-1, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(-1, 0)));
		assertEquals(
				new Field(new Coord(-1, -1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-1, -1)));

		assertEquals(
				new Field(new Coord(-2, 1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-2, 1)));
		assertEquals(
				new Field(new Coord(-2, 0), Type.BUTTON1),
				toTest.world.getMaze().getField(new Coord(-2, 0)));
		assertEquals(
				new Field(new Coord(-2, -1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-2, -1)));

		assertEquals(
				new Field(new Coord(-3, 1), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-3, 1)));
		assertEquals(
				new Field(new Coord(-3, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(-3, 0)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(-3, -1)));

		assertNull(
				toTest.world.getMaze().getField(new Coord(-4, 1)));
		assertEquals(
				new Field(new Coord(-4, 0), Type.WAY),
				toTest.world.getMaze().getField(new Coord(-4, 0)));
		assertNull(
				toTest.world.getMaze().getField(new Coord(-4, -1)));

		assertEquals(
				new Field(new Coord(-5, 0), Type.WALL),
				toTest.world.getMaze().getField(new Coord(-5, 0)));

		assertEquals(
				toTest.world.getMaze().toString(),
				"??##??\n" +
				"#  1 <\n" +
				"???##?");
	}

	@Test
	public void handlePushed(){
		toTest.world = mock(World.class);

		toTest.handlePushed(new ActionSuccess());

		verify(toTest.world).pushButton();
	}

	@Test
	public void handleDroped(){
		toTest.world = mock(World.class);

		toTest.handleSwaped(new ActionSuccess());

		verify(toTest.world).swapButton();
	}
}
