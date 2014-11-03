package server.mj;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class HttpChannelPipelineFactory implements ChannelPipelineFactory {
	
	private static final Logger logger = Logger.getLogger(MyChannelPipelineFactory.class.getName());

	public ChannelPipeline getPipeline() throws Exception {
		logger.log(Level.INFO,"new pipeline!");
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("handler", new AdminServerHandler());
		return pipeline;
	}
}
