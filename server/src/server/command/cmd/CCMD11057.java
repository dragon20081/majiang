package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

import common.Log;

/**
 *  准备
 * @author xue
 */
public class CCMD11057 extends  CMD{
	
	
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
		super.setBytes(buf);
		//已经开始就不改变状态
		if(this.player.getRoom().enteredGame)
			return;
		this.player.zhunbei = this.getBooleanVaule(0);
		//如果钱不够，就准备不起
		//金币必须大于倍数的10倍
		
		Room r = this.player.getRoom();
		if(this.player.getBusiness().getPlayer().getGold() < r.getBeishu() * 10)
		{
			this.player.zhunbei = false;
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(player, "筹码不足，低于当前房间倍数的10倍！");
		}
		
		
		Log.log("this.getBooleanVaule(0):"+this.getBooleanVaule(0));
		MyArray arr = new MyArray();
		arr.push(this.player.getLocId());
		arr.push(this.player.zhunbei);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		this.player.getRoom().SendRoomBroadcast(11057, buf1.getBuf());
		
		Log.info("----->zhunbei:" + this.player.getName() + "locId  :"+ this.player.getLocId()  + "    status:   "  + this.player.zhunbei);
	}
}
