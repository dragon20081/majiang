package server.command.cmd;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import common.MyArrays;

import server.command.CMD;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import business.entity.MJ_User;

/**
 *  注册修改玩家昵称和性别
 * @author xue
 */
public class CCMD11105 extends  CMD{
	
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
		
		Map<Object,Object> map= this.getMapValue(0);
		
		
		CCMD11102 cmd102 = new CCMD11102();
		cmd102.setPlayer(player);
		
		MJ_User user = this.player.getBusiness().getPlayer();
		String nick = (String) map.get("昵称");
		boolean flag = cmd102.modNick(user,nick); 
	
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.setPlayer(player);
		cmd101.sendUserInfo(MyArrays.asList(1));
		this.player.getBusiness().savePlayer(user);
		
		
		MyByteArray mybuf = new MyByteArray();
		
		Map<Object,Object> resultMap=  new HashMap<Object, Object>();
		
		if(!flag)
		{
			resultMap.put("结果", false);
			resultMap.put("失败原因", "昵称已存在");
			mybuf.write(resultMap);
			this.player.send(11105, mybuf.getBuf());
			return;
		}
		int sex =  (Integer) map.get("性别");
		
		cmd102.mod_sex(user, sex);
		resultMap.put("结果", true);
		mybuf.write(resultMap);
		this.player.send(11105, mybuf.getBuf());
	}
}
