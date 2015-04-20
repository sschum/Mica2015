package de.tarent.mica.maze.bot;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.LookActionSuccess;
import de.tarent.mica.maze.bot.strategy.Strategy;
import de.tarent.mica.maze.model.Coord;
import de.tarent.mica.maze.model.Field;
import de.tarent.mica.maze.model.Type;
import de.tarent.mica.maze.model.World;

/**
 * The Robot is simply responsible for updating the world.
 * Only the used {@link Strategy} have to decide the next
 * actions.
 *
 * @author rainu
 */
public class RobotImpl extends AbstractRobot {

	World world;
	private Strategy strategy;

	public RobotImpl(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	protected Action handleStartEvent() {
		world = new World();

		return getNextAction();
	}

	@Override
	protected Action handleGameStarted(ActionSuccess event) {
		return getNextAction();
	}

	@Override
	protected Action handleWalked(ActionSuccess event) {
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

		return getNextAction();
	}

	@Override
	protected Action handleTurnedRight(ActionSuccess event) {
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

		return getNextAction();
	}

	@Override
	protected Action handlePushed(ActionSuccess event) {
		world.pushButton();

		return getNextAction();
	}

	@Override
	protected Action handleGot(ActionSuccess event) {
		world.putButton();

		return getNextAction();
	}

	@Override
	protected Action handleDroped(ActionSuccess event) {
		world.dropButton();

		return getNextAction();
	}

	@Override
	protected Action handleLooked(ActionSuccess event) {
		if(event instanceof LookActionSuccess){
			handleLooked((LookActionSuccess)event);
		}// else -> i don't see anything!

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
			if(!world.getMaze().hasField(leftCoord) && !lookField.isWall()){
				Field leftField = new Field(leftCoord,
						lookField.hasLeftBranch() ? Type.WAY : Type.WALL);

				world.getMaze().putField(leftField);
			}
			if(!world.getMaze().hasField(rightCoord) && !lookField.isWall()){
				Field rightField = new Field(rightCoord,
						lookField.hasRightBranch() ? Type.WAY : Type.WALL);

				world.getMaze().putField(rightField);
			}
		}

	}

	@Override
	protected Action handleGameStartFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleWalkFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleTurnLeftFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleTurnRightFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handlePushFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleGetFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleDropFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	@Override
	protected Action handleLookFailed(ActionFail event) {
		// TODO Auto-generated method stub
		return getNextAction();
	}

	private Action getNextAction() {
		return strategy.getNetxtAction(world);
	}

}
