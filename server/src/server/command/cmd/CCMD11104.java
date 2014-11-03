package server.command.cmd;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import business.entity.MJ_User;

/**
 *  消耗钻石，刷新玩家单机次数
 * @author xue
 */
public class CCMD11104 extends  CMD{
	
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
			
		MJ_User user = this.player.getBusiness().getPlayer();
		int cost = user.getRefreshDia();
		boolean flag = false;
		if(user.getDianQuan() >= cost)
		{
			flag = true;
			CCMD11102 cmd102 = new CCMD11102();
			cmd102.setPlayer(player);
			cmd102.refreshDanji();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("结果", flag);
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(map);
		this.player.send(11104, byteBuf.getBuf());
		
		if(!flag)
		{
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(player, "钻石不足！");
		}
	}
}
