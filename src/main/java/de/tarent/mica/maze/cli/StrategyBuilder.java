package de.tarent.mica.maze.cli;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.exception.ExitException;
import de.tarent.mica.maze.bot.strategy.ExplorerStrategy;
import de.tarent.mica.maze.bot.strategy.PathWalkerStrategy;
import de.tarent.mica.maze.bot.strategy.PushStrategy;
import de.tarent.mica.maze.bot.strategy.Strategy;
import de.tarent.mica.maze.bot.strategy.StrategyChain;
import de.tarent.mica.maze.bot.strategy.SwapStrategy;

public class StrategyBuilder {
	private StrategyChain chain = new StrategyChain();
	private PathWalkerStrategy walker = new PathWalkerStrategy();

	@Command(name="default", abbrev="d")
	public void defaultStrategy() throws ExitException{
		chain.addStrategy(new SwapStrategy(walker));
		chain.addStrategy(new PushStrategy(walker));
		chain.addStrategy(new ExplorerStrategy(walker));

		throw new ExitException();
	}

	@Command
	public void addExplorer(){
		chain.addStrategy(new ExplorerStrategy(walker));
	}

	@Command
	public void addSwap(){
		chain.addStrategy(new SwapStrategy(walker));
	}

	@Command
	public void addPush(){
		chain.addStrategy(new PushStrategy(walker));
	}

	@Command
	public void removeLast(){
		chain.removeLast();
	}

	public Strategy getStrategy(){
		return chain;
	}
}
