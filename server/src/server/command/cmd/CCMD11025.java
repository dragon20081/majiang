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
public class CCMD11025 extends  CMD{
	
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
		Room r =  this.player.getRoom();
		if(r != null)
		{
			r.SendOtherBroadcast(11024, new SCMD11024().getBytes(this.player), this.player);
			r.leaveRoom(player);
		}
	}

}
