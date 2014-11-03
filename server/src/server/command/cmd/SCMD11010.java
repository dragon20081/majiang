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
 *   过  pass
 * @author xue
 *
 */
public class SCMD11010 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11010.class.getName());
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.INT));
		MgsPlayer p  = (MgsPlayer) obj;
		Room r = p.getRoom();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		this.writePattern(buf);
		buf.writeInt(p.getBusiness().getPlayer().getUid());
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}
