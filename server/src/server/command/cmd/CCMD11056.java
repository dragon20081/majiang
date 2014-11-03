package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PatternPai;
import server.mj.Global;
import server.mj.MgsPlayer;
import business.entity.MJ_User;
import business.entity.M_Shop;

/**
 *  玩家离开房间
 * @author xue
 */
public class CCMD11056 extends  CMD{
	
	
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
		
		this.player.zhunbei = false;
		if(this.player.getRoom() == null)return;
		{
			
		}
		int pid = this.player.getLocId();
		MyArray arr = new MyArray();
		arr.push(pid);
		arr.push(1);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		this.player.getRoom().SendRoomBroadcast(11056, buf1.getBuf());
		this.player.getRoom().leaveRoom(player);
	}
	public void audo_deal(MgsPlayer p)
	{
		MyArray arr = new MyArray();
		arr.push(p.getLocId());
		arr.push(2);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		if(p.getRoom() != null)p.getRoom().SendRoomBroadcast(11056, buf1.getBuf());
		else
		{
			p.send(11056, buf1.getBuf());
		}
	}


		



}
