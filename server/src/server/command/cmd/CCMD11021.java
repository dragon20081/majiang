package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  取消匹配
 * @author xue
 */
public class CCMD11021 extends  CMD{
	
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
		
		SCMD11021  scmd = new SCMD11021();
		Room  r  = this.player.getRoom();
		if(r != null)
		{
			r.SendRoomBroadcast(11021, scmd.getBytes(this.player));
		}
	}

}
