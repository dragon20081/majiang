package server.mj;

import java.nio.charset.Charset;
import java.util.logging.Logger;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class MgsDecoder extends FrameDecoder {
	
//	private static final Logger logger = Logger.getLogger(MgsDecoder.class.getName());

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		
		if(buffer.readableBytes() < 4)
		{
			return null;
		}
		buffer.markReaderIndex();
		int length = buffer.readInt();
		if(buffer.readableBytes() < length)
		{
			buffer.resetReaderIndex();
			return null;
		}
		ChannelBuffer content = buffer.readBytes(length);
		int cmdCode = content.readInt();
		int command = content.readUnsignedShort();
		//logger.log(Level.INFO,"receive command:"+command);
		MgsEvent event = new MgsEvent(command);
		event.cmdCode  = cmdCode;
		event.message = content.readBytes(content.readableBytes());
		return event;
	}
}
