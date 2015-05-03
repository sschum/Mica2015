package de.tarent.mica.maze.bot.strategy;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.model.World;
import de.tarent.mica.maze.util.LogFormat;

/**
 * A chain of {@link Strategy}. Each {@link Strategy} will be asked for
 * next action (ordered by the chain position -> 0 is the first n is the last).
 *
 * @author rainu
 */
public class StrategyChain implements Strategy {
	private static final Logger log = Logger.getLogger(AbstractStrategy.class);

	private List<Strategy> chain = new LinkedList<Strategy>();

	@Override
	public Action getNextAction(World world) {
		for(Strategy strategy : chain){
			Action action = strategy.getNextAction(world);
			if(action != null){
				log.info(
					LogFormat.format(
						"The b{0} has decided for r{1}",
						strategy.getClass().getSimpleName(),
						action.getClass().getSimpleName()));
				return action;
			}
		}

		log.info("No strategy hase chosen an action!");
		return null;
	}

	public void addStrategy(Strategy strategy){
		this.chain.add(strategy);
	}
}
