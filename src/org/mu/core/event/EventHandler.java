package org.mu.core.event;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.eventbus.EventBus;

/**
 * Handles event dispatching and subscriptions to events.
 * 
 * @author Joe Pritzel
 * 
 */
public class EventHandler {

	/**
	 * Handles event listener subscriptions and event dispatching.
	 */
	private final EventBus bus;

	private final ScheduledExecutorService scheduler;

	@Inject
	public EventHandler(@Named("EventHandler bus") EventBus bus,
			@Named("EventHandler scheduler") ScheduledExecutorService scheduler) {
		this.bus = bus;
		this.scheduler = scheduler;
	}

	/**
	 * Registers an event listener.
	 * 
	 * @param listener
	 *            - The event listener to register.
	 */
	public void registerListener(Object listener) {
		bus.register(listener);
	}

	/**
	 * Unregisters an event listener.
	 * 
	 * @param listener
	 *            - The event listener to unregister.
	 */
	public void unregisterListener(Object listener) {
		bus.unregister(listener);
	}

	/**
	 * Dispatches an event to all subscribed event listeners.
	 * 
	 * @param event
	 *            - The event to dispatch.
	 */
	public void dispatch(Object event) {
		bus.post(event);
	}

	/**
	 * Dispatches an event to all subscribed event listeners after the specified
	 * number of milliseconds.
	 * 
	 * @param event
	 *            - The event to dispatch.
	 * @param delay
	 *            - The number of milliseconds to delay the dispatching of the
	 *            event.
	 */
	public void dispatch(final Object event, long delay) {
		scheduler.schedule(new Runnable() {

			@Override
			public void run() {
				bus.post(event);
			}

		}, delay, TimeUnit.MILLISECONDS);
	}
}
