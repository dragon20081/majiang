package server.command.cmd;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import business.UnlockCup;
import business.entity.MJ_Cup;
import business.entity.MJ_User;


/**
 * @author xue
 */
public class CCMD11083 extends CMD{

	private boolean flag = false;
	@Override
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);

		MJ_User user = this.player.getBusiness().getPlayer();
		
		UnlockCup unlockCup = new UnlockCup();
		CCMD11102 cmd102 = new CCMD11102();
		cmd102.setPlayer(player);
		
		for(int i = 2 ; i <= 5; i++)
		{
				boolean unlockNext = unlockCup.checkCupUnlockCondition(user, i);
				List<Integer> list = user.getCupScore();
				if(unlockNext && list.get(i) == -1)
				{
					cmd102.unlock_Cup(i);
					MJ_Cup nextCup = Global.cups.get(i);
					CCMD11111 cmd111 = new CCMD11111();
					cmd111.auto_deal(player,nextCup.getName()+ "  解锁!");
				}
		}
		
		this.player.send(11083, null);
	}
}
