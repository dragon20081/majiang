package server.command.cmd;

import java.util.ArrayList;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.PlayerSkill;
import server.mj.Room;

/**
 *  听牌
 * @author xue
 */
public class CCMD11015 extends  CMD{
	
	private PatternPai pattern = new PatternPai();
	private int skillId  = -1;

	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		if(this.player.isTing())return;
		this.player.setWishNextPais(new ArrayList<Integer>());
		this.player.setSkillWishPais(new ArrayList<Integer>());
		
		if(this.getValues() != null &&this.getValues().size() > 0)
			this.skillId = this.getIntVaule(0);
		if(this.skillId <= 0)
		{
			System.out.println("not complete use skill ");
		}else
		{
			Log.log("ccmd11015");
			this.player.getSkillutil().useSkillById(this.player,PlayerSkill.ID_HUANGQUAN);
		}
		Room  r = this.player.getRoom();
		this.player.setTing(true);
		this.player.increaseQi();
		CCMD11031 ccmd031 = new CCMD11031();
		ccmd031.auto_deal(this.player, 1);
		
		//设置天听  默认true   听牌之前如果有人 碰杠吃， 或者自己打过牌就不为天听
 		if(r.isTiantingFlag())this.player.setTianhe(true);
		SCMD11015   scmd  =  new SCMD11015(); 
		this.player.getRoom().SendRoomBroadcast(11015, scmd.getBytes(this.player));
	}

}
