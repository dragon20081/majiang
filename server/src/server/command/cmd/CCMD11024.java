package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  离开房间
 * @author xue
 */
public class CCMD11024 extends  CMD{
	
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

	}
	public void auto_deal(Room r)
	{
		if(r != null)
		{
			r.SendRoomBroadcast(11024, null);
		}
	}

}
