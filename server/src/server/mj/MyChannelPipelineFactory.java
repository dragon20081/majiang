package server.mj;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class MyChannelPipelineFactory implements ChannelPipelineFactory {
	
	private static final Logger logger = Logger.getLogger(MyChannelPipelineFactory.class.getName());

	public ChannelPipeline getPipeline() throws Exception {
		logger.log(Level.INFO,"new pipeline!");
		return Channels.pipeline(new MgsDecoder(), new MgsPlayer());
	}
}
