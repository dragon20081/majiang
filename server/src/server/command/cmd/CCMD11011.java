package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;


import server.command.CMD;
import server.mj.MgsPlayer;

/**
 *   玩家掉线消息
 * @author xue
 *
 */
public class CCMD11011 extends  CMD{
	

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
		
		SCMD11011  scmd =  new SCMD11011();
		this.player.getRoom().SendRoomBroadcast(11011, scmd.getBytes(this.player));
	}
	public ChannelBuffer auto_deal(MgsPlayer p)
	{
		SCMD11011  scmd =  new SCMD11011(); 
		return scmd.getBytes(p);
	}

}
