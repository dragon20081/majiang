package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 * 确认从游戏回到房间
 * @author xue
 */
public class CCMD11061 extends  CMD{

	
	@Override
	public void setPlayer(MgsPlayer player) {
		
		this.player = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		Room r = this.player.getRoom();
		
	}
}
