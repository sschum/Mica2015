package de.tarent.mica.maze.util;

import java.util.Collections;
import java.util.List;

/**
 * Eigene Random-Klasse, die zusätzliche funktionalitäten als {@link java.util.Random} bietet.
 *
 * @author rainu
 *
 */
public class Random extends java.util.Random {
	private static final long serialVersionUID = 1L;

	/**
	 * Durchläuft das {@link Runnable} zufällige mal...
	 *
	 * @param toRun
	 */
	public void runXTimes(Runnable toRun){
		final int max = Math.abs(nextInt() % 13);
		for(int i = max; i >= 0; i--){
			toRun.run();
		}
	}

	@Override
	public int nextInt() {
		setSeed(13121989 * System.nanoTime());

		for(int i = super.nextInt() % 13; i >= 0; i--){
			super.nextInt();
		}

		return super.nextInt();
	}

	@Override
	public boolean nextBoolean() {
		return nextInt() % 2 == 0;
	}


	/**
	 * Liefert ein Zufälliges Element aus dem gegebenen Array.
	 *
	 * @param values
	 * @return
	 */
	public <T> T choose(@SuppressWarnings("unchecked") T...values) {
		int index = Math.abs(nextInt() % values.length);
		return values[index];
	}

	/**
	 * Liefert ein Zufälliges Element aus dem gegebenen Array.
	 *
	 * @param values
	 * @return
	 */
	public <T> T choose(List<T> values) {
		int index = Math.abs(nextInt() % values.size());
		return values.get(index);
	}

	/**
	 * Durchwürfelt eine Liste...
	 *
	 * @param toShuffle
	 */
	public void shuffle(final List<?> toShuffle){
		if(toShuffle == null || toShuffle.size() <= 1) return;

		final Random me = this;
		this.runXTimes(new Runnable() {
			@Override
			public void run() {
				Collections.shuffle(toShuffle, me);
			}
		});
	}
}
