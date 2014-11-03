package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;


import server.command.CMD;
import server.mj.MgsPlayer;

/**
 *   玩家掉线后重新获取所有的牌
 * @author xue
 *
 */
public class CCMD11012 extends  CMD{
	

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
		
		SCMD11012  scmd =  new SCMD11012();
		this.player.send(11012, scmd.getBytes(this.player));
	}

}
