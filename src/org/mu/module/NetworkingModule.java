package org.mu.module;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.mu.net.GameServerChannelPipelineFactory;

import com.google.inject.AbstractModule;

/**
 * This serves as the configuration for the entire networking portion of the server.
 * 
 * @author Joe Pritzel
 * 
 */
public class NetworkingModule extends AbstractModule {

	@Override
	protected void configure() {
		
		ChannelFactory channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
		
		bootstrap.setPipelineFactory(new GameServerChannelPipelineFactory());
		
		bind(ServerBootstrap.class).toInstance(bootstrap);
	}

}
