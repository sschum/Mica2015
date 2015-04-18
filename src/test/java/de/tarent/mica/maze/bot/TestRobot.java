package de.tarent.mica.maze.bot;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;

class TestRobot extends AbstractRobot {
	public static Action returnAction = new Action();

	@Override
	protected Action handleStartEvent() {
		return returnAction;
	}
	@Override
	protected Action handleGameStarted(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleWalked(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleTurnedLeft(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleTurnedRight(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handlePushed(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleGot(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleDroped(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleLooked(ActionSuccess event) {
		return returnAction;
	}
	@Override
	protected Action handleGameStartFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleWalkFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleTurnLeftFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleTurnRightFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handlePushFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleGetFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleDropFailed(ActionFail event) {
		return returnAction;
	}
	@Override
	protected Action handleLookFailed(ActionFail event) {
		return returnAction;
	}
}