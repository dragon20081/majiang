package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import server.command.CMD;
import server.mj.MgsPlayer;


/**
 *  开始下一轮牌
 * @author xue
 *
 */
public class SCMD11005 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11005.class.getName());
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}
