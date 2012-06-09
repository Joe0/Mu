package org.mu.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * This is the {@link ChannelPipelineFactory} that is used for connections to
 * the game server.
 * 
 * @author Joe Pritzel
 * 
 */
public class GameServerChannelPipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// Add handlers/codecs here.

		return pipeline;
	}

}
