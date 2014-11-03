package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.SCMD3;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.Room;
import server.mj.RoomManager;
import business.entity.MJ_User;

/**
 *  创建房间
 * @author xue
 */
public class CCMD11052 extends  CMD{
	
	
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
		RoomManager rManager = Global.rManager;
		MJ_User user = this.player.getBusiness().getPlayer();
		
		boolean gaming = rManager.isInGaming(user.getName());
		if(gaming)
		{
			this.player.send(11052, null);
			CCMD11053 cmd053 = new CCMD11053();
			cmd053.auto_deal_fail(player, "上一局游戏还未结束，请等待！", true);
			return;
		}
		if(user.getImage() < 0)
		{
			this.player.send(11052, null);
			CCMD11053 cmd053 = new CCMD11053();
			cmd053.auto_deal_fail(player, "没有形象，不能创建房间", true);
			return;
		}
		rManager.createRoom(player ,user.getNick()+"", 2, 1, 1);
		
		Room r = this.player.getRoom();
		MyArray arr = new MyArray();
		arr.push(r.getRoomID());
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(arr);
		SCMD3 scmd3 = new SCMD3();
		scmd3.list.add(this.player.coderCMD(11052, byteBuf.getBuf()));
		CCMD11053 cmd053 = new CCMD11053();
		scmd3.list.add(cmd053.getBuf(player));
		this.player.send(3, scmd3.getBytes());
	}
}
