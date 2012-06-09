package org.mu.core.net;

import java.net.SocketAddress;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.netty.bootstrap.ServerBootstrap;

/**
 * This is the interface in which we use to start the networking portion of our
 * application. It will attempt to clean itself up when the server is shutting down.
 * 
 * @author Joe Pritzel
 * 
 */
@Singleton
public class Networking {

	/**
	 * This is the bootstrap that will be used.
	 */
	private final ServerBootstrap bootstrap;

	/**
	 * The is the socket address to bind the bootstrap to.
	 */
	private final SocketAddress address;

	/**
	 * Constructs a new Networking object.
	 * 
	 * @param bootstrap
	 *            - The fully configured {@link ServerBootstrap}.
	 * @param address
	 *            - The {@link SocketAddress} to bind the
	 *            {@link ServerBootstrap} to.
	 */
	@Inject
	protected Networking(ServerBootstrap bootstrap, SocketAddress address) {
		this.bootstrap = bootstrap;
		this.address = address;
	}

	/**
	 * Starts the networking portion of the server.
	 */
	public final void start() {
		this.bootstrap.bind(address);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				bootstrap.releaseExternalResources();
			}
		});

	}
}
