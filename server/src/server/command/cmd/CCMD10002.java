package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import business.Business;
import business.entity.MJ_User;

import server.command.CMD;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.MgsRoom;

/**
 * 
 * 断线
 * @author xue
 *
 */
public class CCMD10002 extends  CMD{
	
	private Business  business ;

	public void setPlayer(MgsPlayer player) {
		this.player = player;
		this.business  = this.player.getBusiness();
	}
	public ChannelBuffer getBytes() {
		return null;
	}

	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
	
	}

}
