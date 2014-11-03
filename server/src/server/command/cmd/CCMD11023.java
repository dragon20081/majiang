package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  使用技能
 * @author xue
 */
public class CCMD11023 extends  CMD{
	
	private PatternPai pattern = new PatternPai();

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
		this.setBytes(buf);
		int skillId = 	this.getIntVaule(0);
		Room r = this.player.getRoom();
		if(r == null || r.getSkillLimit() != 1)//不能使用技能
			return;
		
		
	}

}
