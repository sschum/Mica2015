package de.tarent.mica.maze.bot;

import java.util.LinkedList;
import java.util.List;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.Push;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.action.Swap;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.Event;

public abstract class AbstractRobot implements Robot {
	List<Action> history = new LinkedList<Action>();

	@Override
	public Action handleEvent(Event event) {
		Action action = null;

		if(history.isEmpty()){
			action = handleStartEvent();
		}else{
			if(event instanceof ActionSuccess){
				action = handleActionSuccess((ActionSuccess)event);
			}else if(event instanceof ActionFail){
				action = handleActionFail((ActionFail)event);
			}
		}

		history.add(action);
		return action;
	}

	private Action getLastAction(){
		if(history.isEmpty()) return null;

		return history.get(history.size() - 1);
	}

	protected abstract Action handleStartEvent();

	private Action handleActionSuccess(ActionSuccess event) {
		final Action lastAction = getLastAction();
		final Action nextAction;

		if(lastAction instanceof StartGame){
			nextAction = handleGameStarted(event);
		}else if(lastAction instanceof Walk){
			nextAction = handleWalked(event);
		}else if(lastAction instanceof TurnLeft){
			nextAction = handleTurnedLeft(event);
		}else if(lastAction instanceof TurnRight){
			nextAction = handleTurnedRight(event);
		}else if(lastAction instanceof Push){
			nextAction = handlePushed(event);
		}else if(lastAction instanceof Swap){
			nextAction = handleSwaped(event);
		}else if(lastAction instanceof Look){
			nextAction = handleLooked(event);
		}else{
			throw new IllegalStateException("This code should never be reached!");
		}

		return nextAction;
	}

	protected abstract Action handleGameStarted(ActionSuccess event);

	protected abstract Action handleWalked(ActionSuccess event);

	protected abstract Action handleTurnedLeft(ActionSuccess event);

	protected abstract Action handleTurnedRight(ActionSuccess event);

	protected abstract Action handlePushed(ActionSuccess event);

	protected abstract Action handleSwaped(ActionSuccess event);

	protected abstract Action handleLooked(ActionSuccess event);

	private Action handleActionFail(ActionFail event) {
		final Action lastAction = getLastAction();
		final Action nextAction;

		if(lastAction instanceof StartGame){
			nextAction = handleGameStartFailed(event);
		}else if(lastAction instanceof Walk){
			nextAction = handleWalkFailed(event);
		}else if(lastAction instanceof TurnLeft){
			nextAction = handleTurnLeftFailed(event);
		}else if(lastAction instanceof TurnRight){
			nextAction = handleTurnRightFailed(event);
		}else if(lastAction instanceof Push){
			nextAction = handlePushFailed(event);
		}else if(lastAction instanceof Swap){
			nextAction = handleSwapFailed(event);
		}else if(lastAction instanceof Look){
			nextAction = handleLookFailed(event);
		}else{
			throw new IllegalStateException("This code should never be reached!");
		}

		return nextAction;
	}

	protected abstract Action handleGameStartFailed(ActionFail event);

	protected abstract Action handleWalkFailed(ActionFail event);

	protected abstract Action handleTurnLeftFailed(ActionFail event);

	protected abstract Action handleTurnRightFailed(ActionFail event);

	protected abstract Action handlePushFailed(ActionFail event);

	protected abstract Action handleSwapFailed(ActionFail event);

	protected abstract Action handleLookFailed(ActionFail event);

}
