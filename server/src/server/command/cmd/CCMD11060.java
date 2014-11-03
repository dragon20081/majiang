package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 * 踢人
 * @author xue
 */
public class CCMD11060 extends  CMD{

	private MgsPlayer player;
	
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
		if(this.player != r.getManager())return;
		
		int pid = this.getIntVaule(0);
		if(pid == this.player.getLocId())return;
		
		MgsPlayer tip = r.getPlayers().get(pid);
		if(tip == null)
		{
			Log.error("cmd11060  playerId" + tip+"can not find !");
			return;
		}
		r.SendRoomBroadcast(11060, null);
		CCMD11056 cmd056 = new CCMD11056();
		cmd056.audo_deal(tip);
		tip.getRoom().leaveRoom(tip);
	}
}
