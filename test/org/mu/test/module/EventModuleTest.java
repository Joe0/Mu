package org.mu.test.module;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.mu.core.event.EventHandler;
import org.mu.module.EventModule;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;

/**
 * Tests all aspects of the event system.
 * 
 * @author Joe Pritzel
 * 
 */
public class EventModuleTest {

	/**
	 * Tests a manually created {@link EventHandler}.
	 */
	@Test
	public void testManual() {
		EventHandler handler = new EventHandler(new AsyncEventBus(
				Executors.newSingleThreadExecutor()),
				Executors.newSingleThreadScheduledExecutor());
		test(handler);
	}

	/**
	 * Tests an {@link EventHandler} that was created using DI.
	 */
	@Test
	public void testDI() {
		EventHandler handler = Guice.createInjector(new EventModule())
				.getInstance(EventHandler.class);
		test(handler);
	}

	/**
	 * Tests the given {@link EventHandler}.
	 * 
	 * @param handler
	 *            - The {@link EventHandler} to test.
	 */
	private void test(EventHandler handler) {
		testMethods(handler);
		testThreadExecution(handler);
	}

	/**
	 * Tests the methods in the given {@link EventHandler}.
	 * 
	 * @param handler
	 *            - The {@link EventHandler} to test.
	 */
	private void testMethods(EventHandler handler) {
		Object listener = new Object() {

			@SuppressWarnings("unused")
			@Subscribe
			public void setFlag(TestObject o) {
				o.flag.set(true);
			}
		};

		handler.registerListener(listener);

		TestObject test = new TestObject();

		handler.dispatch(test);

		try {
			Thread.sleep(15);
		} catch (Exception e) {
		} finally {
			handler.unregisterListener(listener);
			assertTrue(test.flag.get());
		}
	}

	/**
	 * Ensures that the given {@link EventHandler} only executes subscribed
	 * listeners on a single thread.
	 * 
	 * @param handler
	 *            - The {@link EventHandler} to test.
	 */
	private void testThreadExecution(final EventHandler handler) {
		Object listener = new Object() {

			@SuppressWarnings("unused")
			@Subscribe
			public void setFlag(TestObject o) {
				if(o.flag.get()) {
					o.threadId2.set(Thread.currentThread().getId());
				} else {
					o.threadId1.set(Thread.currentThread().getId());
					o.flag.set(true);	
				}
				
			}
		};

		handler.registerListener(listener);

		final TestObject test = new TestObject();

		handler.dispatch(test);
		
		FutureTask<Void> executed = new FutureTask<>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				handler.dispatch(test);
				return null;
			}
			
		});
		
		new Thread(executed).start();

		try {
			executed.get();
			Thread.sleep(15);
		} catch (Exception e) {
		} finally {
			handler.unregisterListener(listener);
			assertTrue(test.threadId1.get() != -1 && test.threadId1.get() == test.threadId2.get());
		}
	}

	class TestObject {
		public final AtomicBoolean flag = new AtomicBoolean(false);
		public final AtomicLong threadId1 = new AtomicLong(-1);
		public final AtomicLong threadId2 = new AtomicLong(-1);
	}

}
