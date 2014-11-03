package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import business.Business;
import business.entity.MJ_User;

import server.command.CMD;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  开始自动匹配, 加入匹配队列
 * @author xue
 */
public class CCMD11001_old extends  CMD{
	

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
		//解析指令,获得匹配条件
		super.setBytes(buf);
		int playerLimit = 	this.getIntVaule(0);
		int skillLimit  =   this.getIntVaule(1);
		int quanLimit   =   this.getIntVaule(2);
		
		boolean poorGold = false;
		String comm = "";
		MJ_User user = this.player.getBusiness().getPlayer();
		if(user.getGold() == 0)
		{
			comm = "你的金币为0,不能进入对战房间!";
			poorGold = true;
		}
		if(!poorGold && playerLimit == 4)
		{
			if(user.getGold() < Room.MIN_CHIPS)
			{
				comm = "你的金币不足,4人场进入最低金币限额:"+ Room.MIN_CHIPS+"!";				
				poorGold = true;
			}
		}
		if(poorGold)
		{
			SCMD11001 scmd  = new SCMD11001();
			this.player.send(11001, scmd.getFaliMsg(playerLimit, 1, 1));
			CCMD11111 ccmd = new CCMD11111();
			ccmd.auto_deal(this.player, comm);
			return;
		}
//		Global.addToAutoMathQueue(this.player);
//		Global.automatch(player, playerLimit, skillLimit,quanLimit);
		SCMD11001 scmd  = new SCMD11001();
		this.player.send(11001, scmd.getBytes(this.player));
		player.getRoom().boradcastPlayerIn(player);
		player.getRoom().sendRoomInfo();
//		String nick =  player.getBusiness().getPlayer().getName();
//		System.out.println("  ---->广播玩家进入房间:"+nick);
	}

}
