package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  修改运
 * @author xue
 */
public class CCMD11032 extends  CMD{
	
	private PatternPai pattern = new PatternPai();

	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		this.setBytes(buf);
	}
	public void auto_deal(MgsPlayer p, int mod, boolean isTmp)
	{
		SCMD11032 scmd = new SCMD11032();
		scmd.setUid(p.getRoomId());
		scmd.setXiugailinag(mod);
		
		
		int yun = p.getYun() + p.getTmpYun();
//		if(yun > 5) yun = 5;
		scmd.setResult(yun);
		scmd.setFlag(isTmp);
		p.getRoom().SendRoomBroadcast(11032, scmd.getBytes());
	}

}
