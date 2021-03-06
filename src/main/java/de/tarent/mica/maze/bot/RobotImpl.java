package de.tarent.mica.maze.bot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.LookActionSuccess;
import de.tarent.mica.maze.bot.strategy.Strategy;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.util.LogFormat;

/**
 * The Robot is simply responsible for updating the world.
 * Only the used {@link Strategy} have to decide the next
 * actions.
 *
 * @author rainu
 */
public class RobotImpl extends AbstractRobot {
	private static final Logger log = Logger.getLogger(RobotImpl.class);

	World world;
	private String name;
	private Strategy strategy;

	public RobotImpl(String name, Strategy strategy) {
		this.name = name;
		this.strategy = strategy;
	}

	@Override
	public void reset() {
		super.reset();

		strategy.reset();
	}

	@Override
	protected Action handleStartEvent() {
		world = new World();

		return new StartGame(name);
	}

	@Override
	protected Action handleGameStarted(ActionSuccess event) {
		log.info("Game started.");

		return getNextAction();
	}

	@Override
	protected Action handleWalked(ActionSuccess event) {
		log.info("Walked.");
		world.increasAction();

		final Field playerField = world.getMaze().getPlayerField();
		final Type playerType = playerField.getPlayerType();

		Coord newCoord = null;

		switch(playerType){
		case PLAYER_NORTH:
			newCoord = playerField.getCoord().north();
			break;
		case PLAYER_EAST:
			newCoord = playerField.getCoord().east();
			break;
		case PLAYER_SOUTH:
			newCoord = playerField.getCoord().south();
			break;
		case PLAYER_WEST:
			newCoord = playerField.getCoord().west();
			break;
		default:
			throw new IllegalStateException("Player type was expected!");
		}

		final Field movedField = getOrCreateField(newCoord);
		playerField.removePlayer();
		movedField.setPlayer(playerType);

		logWorld();
		return getNextAction();
	}

	private Field getOrCreateField(Coord newCoord) {
		if(!world.getMaze().hasField(newCoord)){
			world.getMaze().putField(new Field(newCoord, Type.WAY));
		}

		return world.getMaze().getField(newCoord);
	}

	@Override
	protected Action handleTurnedLeft(ActionSuccess event) {
		log.info("Turned left.");
		world.increasAction();

		final Field playerField = world.getMaze().getPlayerField();
		final Type playerType = playerField.getPlayerType();

		final Type newPlayerType;
		switch(playerType){
		case PLAYER_NORTH:
			newPlayerType = Type.PLAYER_WEST; break;
		case PLAYER_EAST:
			newPlayerType = Type.PLAYER_NORTH; break;
		case PLAYER_SOUTH:
			newPlayerType = Type.PLAYER_EAST; break;
		case PLAYER_WEST:
			newPlayerType = Type.PLAYER_SOUTH; break;
		default:
			throw new IllegalStateException("Player type was expected!");
		}

		playerField.setPlayer(newPlayerType);

		logWorld();
		return getNextAction();
	}

	@Override
	protected Action handleTurnedRight(ActionSuccess event) {
		log.info("Turned right.");
		world.increasAction();

		final Field playerField = world.getMaze().getPlayerField();
		final Type playerType = playerField.getPlayerType();

		final Type newPlayerType;
		switch(playerType){
		case PLAYER_NORTH:
			newPlayerType = Type.PLAYER_EAST; break;
		case PLAYER_EAST:
			newPlayerType = Type.PLAYER_SOUTH; break;
		case PLAYER_SOUTH:
			newPlayerType = Type.PLAYER_WEST; break;
		case PLAYER_WEST:
			newPlayerType = Type.PLAYER_NORTH; break;
		default:
			throw new IllegalStateException("Player type was expected!");
		}

		playerField.setPlayer(newPlayerType);

		logWorld();
		return getNextAction();
	}

	@Override
	protected Action handlePushed(ActionSuccess event) {
		log.info("Pushed button.");
		world.increasAction();

		world.pushButton();

		logWorld();
		return getNextAction();
	}

	@Override
	protected Action handleSwaped(ActionSuccess event) {
		log.info("Swaped button.");
		world.increasAction();

		world.swapButton();

		logWorld();
		return getNextAction();
	}

	@Override
	protected Action handleLooked(ActionSuccess event) {
		log.info("Looked.");
		world.increasAction();

		if(event instanceof LookActionSuccess){
			handleLooked((LookActionSuccess)event);
		}// else -> i don't see anything!

		logWorld();
		return getNextAction();
	}

	private void handleLooked(LookActionSuccess event) {
		final Field playerField = world.getMaze().getPlayerField();
		final Type playerType = playerField.getPlayerType();

		Coord lastCoord = playerField.getCoord();
		if(event.getFields() != null) for(LookActionSuccess.Field lookField : event.getFields()){
			Coord newCoord, leftCoord, rightCoord;

			switch(playerType){
			case PLAYER_NORTH:
				/*
				 *   l r
				 *    ^
				 */
				newCoord = lastCoord.north();
				leftCoord = newCoord.west();
				rightCoord = newCoord.east();
				break;
			case PLAYER_EAST:
				/*
				 *  l
				 * >
				 *  r
				 */
				newCoord = lastCoord.east();
				leftCoord = newCoord.north();
				rightCoord = newCoord.south();
				break;
			case PLAYER_SOUTH:
				/*
				 *    v
				 *   r l
				 */
				newCoord = lastCoord.south();
				leftCoord = newCoord.east();
				rightCoord = newCoord.west();
				break;
			case PLAYER_WEST:
				/*
				 *   r
				 *    <
				 *   l
				 */
				newCoord = lastCoord.west();
				leftCoord = newCoord.south();
				rightCoord = newCoord.north();
				break;
			default:
				throw new IllegalStateException("Player type was expected!");
			}
			lastCoord = newCoord;

			if(!world.getMaze().hasField(newCoord)){
				Field newField = new Field(newCoord,
						lookField.isWall() ? Type.WALL : Type.WAY,
						Type.getButton(lookField.getButtonNumber()));

				world.getMaze().putField(newField);
			}
			if(!world.getMaze().hasField(leftCoord) && !lookField.isWall() && !lookField.hasLeftBranch()){
				Field leftField = new Field(leftCoord, Type.WALL);

				world.getMaze().putField(leftField);
			}
			if(!world.getMaze().hasField(rightCoord) && !lookField.isWall() && !lookField.hasRightBranch()){
				Field rightField = new Field(rightCoord, Type.WALL);

				world.getMaze().putField(rightField);
			}
		}

	}

	@Override
	protected Action handleGameStartFailed(ActionFail event) {
		log.error("Game start failed!");

		return getNextAction();
	}

	@Override
	protected Action handleWalkFailed(ActionFail event) {
		log.error("Walk failed!");
		world.increasAction();

		return getNextAction();
	}

	@Override
	protected Action handleTurnLeftFailed(ActionFail event) {
		log.error("Turn left failed!");
		world.increasAction();

		return getNextAction();
	}

	@Override
	protected Action handleTurnRightFailed(ActionFail event) {
		log.error("Turn right failed!");
		world.increasAction();

		return getNextAction();
	}

	@Override
	protected Action handlePushFailed(ActionFail event) {
		log.error("Push failed!");
		world.increasAction();

		return getNextAction();
	}

	@Override
	protected Action handleSwapFailed(ActionFail event) {
		log.error("Swap failed!");
		world.increasAction();

		return getNextAction();
	}

	@Override
	protected Action handleLookFailed(ActionFail event) {
		log.error("Look failed!");
		world.increasAction();

		return getNextAction();
	}

	private void logWorld(){
		final String worldString =  world.toString()
				.replace("#", new String(new byte[]{-79}, Charset.forName("CP850")))
				.replace("?", new String(new byte[]{-73}, Charset.forName("CP1252")));

		log.debug("\n" + worldString);
		try {
			Files.write(Paths.get(System.getProperty("java.io.tmpdir") + "/RayMaze.state"), worldString.getBytes());
		} catch (IOException e) {}
	}

	private Action getNextAction() {
		final Action action = strategy.getNextAction(world);

		log.info(LogFormat.format("Choose the action r{0}",
				action == null ? null : action.getClass().getSimpleName()));

		return action;
	}
}
