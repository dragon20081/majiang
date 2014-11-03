package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.MyArrays;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;


/**
 *   玩家掉线消息
 * @author xue
 *
 */
public class SCMD11011 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11011.class.getName());
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.BYTE));
		MgsPlayer p  = (MgsPlayer) obj;
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		this.writePattern(buf);
		buf.writeInt(p.getRoomId());
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}
