package server.command.cmd;

import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsPlayer;
import server.mj.PlayerSkill;

/**
 *  使用技能
 * @author xue
 */
public class CCMD11033 extends  CMD{
	
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
		int skillId  = this.getIntVaule(0);
		Map<Integer,Integer> skills = this.player.getSkillMap();
		if(skills.containsKey((Integer)skillId))
		{
			switch(skillId)
			{
			
				case  PlayerSkill.ID_FEIYANHUANCHAO:
				case  PlayerSkill.ID_BUDONGRUSHAN:
				case  PlayerSkill.ID_HUANGQUAN:
					
										skills.put(skillId, 1); //开启技能
										auto_deal(this.player,skillId);
																		break;
			}
		}
	}
	public void auto_deal(MgsPlayer p, int skillId)
	{
		SCMD11033 scmd = new SCMD11033();
		scmd.setUid(p.getRoomId());
		scmd.setSkillId(skillId);
		p.getRoom().SendRoomBroadcast(11033, scmd.getBytes());
	}
	public void auto_deal_single(MgsPlayer p, int skillId)
	{
		SCMD11033 scmd = new SCMD11033();
		scmd.setUid(p.getRoomId());
		scmd.setSkillId(skillId);
		p.send(11033, scmd.getBytes());
	}

}
