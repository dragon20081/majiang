package server.command.cmd;

import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;


import server.command.CMD;
import server.command.SCMD3;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *   玩家请求下一张牌 摸牌
 * @author xue
 *
 */
public class CCMD11006 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11006.class.getName());
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

		boolean bb  = true;
		if(!bb)return;
		Room r = this.player.getRoom();
		if(r.getCurPlayer() != this.player)return;
		
		if(r.getIDList().size() == 0)
		{
			r.liuju();
			return;
		}
		int nextId =  r.getNextPaiByYun(this.player);
		
		logger.info("cmd 11006:"+this.player.getName() +" paiId  " + nextId );
		SCMD11006   scmd =  new SCMD11006();
		scmd.setNextId(nextId);
		this.player.getBusiness().saveUserOperate("mopai: "+ nextId);
		
		this.player.getRoom().SendRoomBroadcast(11006, scmd.getBytes(this.player));
		this.player.setTouchedPai(nextId);
		//开始倒计时
		this.player.getRoom().start_chupaiTimer();
		
		this.player.increaseQi(1);
		CCMD11031 ccmd031 = new CCMD11031();
		ccmd031.auto_deal(this.player, 1);
	}
	public  ChannelBuffer  auto_deal(MgsPlayer p) {
		

		//根据运， 使用技能后确定的牌序等决定下一张牌
		this.player  = p;
		Room r = this.player.getRoom();
		if(r.getIDList().size() == 0)
		{
			r.liuju();
			return null;
		}
		int nextId  =  r.getNextPaiByYun(player);
		logger.info("cmd 11006    auto_deal:"+this.player.getName() +" paiId  " + nextId );
		p.setTouchedPai(nextId);
		this.player.getBusiness().saveUserOperate("mopai: "+ nextId);
		SCMD11006   scmd =  new SCMD11006();
		scmd.setNextId(nextId);
		this.player.getRoom().start_chupaiTimer();
		
		SCMD3 scmd3 = new SCMD3();
		scmd3.list.add(p.coderCMD(11006, scmd.getBytes(this.player)));
		
		this.player.increaseQi(1);
		CCMD11031 ccmd031 = new CCMD11031();
		scmd3.list.add(ccmd031.getBuf(this.player, 1));
		
		return p.coderCMD(3, scmd3.getBytes());
		 
	}

}
