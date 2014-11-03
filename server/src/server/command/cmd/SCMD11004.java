package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.MyArrays;

import server.command.CMD;
import server.mj.MgsPlayer;


/**
 * 玩家打出一张牌  回复
 * @author xue
 *
 */
public class SCMD11004 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11004.class.getName());
	
	private int paiId;
	
	@Override
	public ChannelBuffer getBytes() {
		logger.info("SCMD11004");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeInt(this.paiId);
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.BYTE,DecodeCMD.BYTE));
		MgsPlayer p   = (MgsPlayer) obj;
		System.out.println("--->打牌:"+p.getRoomId()+": " +p.getBusiness().getPlayer().getName());
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		this.writePattern(buf);
		buf.writeByte(p.getRoomId());
		buf.writeByte(this.paiId);
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getPaiId() {
		return paiId;
	}
	public void setPaiId(int paiId) {
		this.paiId = paiId;
	}
}
