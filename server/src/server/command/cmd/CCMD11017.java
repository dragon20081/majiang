package server.command.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  玩家需要的牌
 * @author xue
 */
public class CCMD11017 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11017.class.getName());
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
		if(this.getValues() == null)return;
		Room r  =  player.getRoom();
		
		if(r.isInTuoguan(player))
		{
			return;
		}
		int type  = this.getIntVaule(0);
		List<Integer> need  = new ArrayList<Integer>();
		
		need.addAll((Collection<? extends Integer>) this.getValues().get(1));
		if(type == 0) //普通
		{
			logger.info("CCMD11017  putong haopai : "+ this.player.getName() +"   " +need );
			this.player.setWishNextPais(need);
		}else  // 1 技能牌
		{
			logger.info("CCMD11017  skill haopai : "+ this.player.getName() +"   " +need );
			this.player.setSkillWishPais(need);
		}
	}

}
