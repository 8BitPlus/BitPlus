package me.themallard.bitmmo.api;

import java.util.HashMap;
import java.util.Map;

import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;

public class Context {

	private static final Map<Thread, AbstractAnalysisProvider> binded = new HashMap<Thread, AbstractAnalysisProvider>();

	/**
	 * Waits on the current Thread until either the provider that was submitted
	 * with the bind method has been binded or until the method times out
	 * (25,000 ms).
	 */
	public static void block() {
		synchronized (binded) {
			final long startTime = System.currentTimeMillis();
			final Thread thread = Thread.currentThread();
			while (true) {
				if (binded.containsKey(thread))
					break;

				long now = System.currentTimeMillis();
				long d = now - startTime;
				if (d >= 25000) {
					throw new RuntimeException("Timed out.");
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Maps the current Thread with the provider or throws a RuntimeException if
	 * the current thread is already binded.
	 * 
	 * @param provider
	 */
	public static void bind(AbstractAnalysisProvider provider) {
		synchronized (binded) {
			Thread thread = Thread.currentThread();
			if (binded.containsKey(thread))
				throw new RuntimeException("A provider is already binded to this thread!");

			binded.put(thread, provider);
		}
	}

	/**
	 * Removes the current Thread (and thus provider) from the map.
	 */
	public static void unbind() {
		synchronized (binded) {
			binded.remove(Thread.currentThread());
		}
	}

	/**
	 * Gets the provider that was binded on the current Thread.
	 * 
	 * @return An AbstractAnalysisProvider or null.
	 */
	public static AbstractAnalysisProvider current() {
		synchronized (binded) {
			return binded.get(Thread.currentThread());
		}
	}
}