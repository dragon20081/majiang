package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *	使用技能
 * @author xue
 */
public class SCMD11023 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11023.class.getName());
	
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
	}
	@Override
	public void setPlayer(MgsPlayer player) {
	}
}
