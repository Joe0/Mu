package org.mu.module;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Serves as the configuration for the entire event system.
 * 
 * @author Joe Pritzel
 * 
 */
public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("EventHandler bus"))
				.toInstance(
						new AsyncEventBus(Executors.newSingleThreadExecutor()));
		bind(ScheduledExecutorService.class).annotatedWith(
				Names.named("EventHandler scheduler")).toInstance(
				Executors.newSingleThreadScheduledExecutor());
	}

}
