package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import common.MyArrays;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  开始
 * @author xue
 */
public class CCMD11058 extends  CMD{
	
	
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
		
		Room r = this.player.getRoom();
		if( this.player.getBusiness().getPlayer().getGold() < r.getBeishu() * 10)
		{
				this.player.zhunbei = false;
				CCMD11111 cmd111 = new CCMD11111();
				cmd111.auto_deal(player, "筹码不足，低于当前房间倍数的10倍！");
				
				// 广播位置
				MyArray arr = new MyArray();
				arr.push(false);
				MyByteArray tmpbuf = new MyByteArray();
				tmpbuf.write(arr);
				this.player.send(11058, tmpbuf.getBuf());
				return;
		}
		
		this.player.zhunbei = true;
		this.player.getRoom().tryStart();
		
	}


		



}
