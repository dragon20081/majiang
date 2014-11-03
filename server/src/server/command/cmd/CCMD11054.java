package server.command.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;
import business.entity.MJ_User;

/**
 *  其他玩家信息
 * @author xue
 */
public class CCMD11054 extends  CMD{
	
	
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf)
	{
		//id,等级、名字、形象、装备技能、金币
		Room r = this.player.getRoom();
		Iterator<MgsPlayer> it = r.getPlayers().values().iterator();
		List<Object> others = new ArrayList<Object>();
		while(it.hasNext())
		{
			MgsPlayer tmpp = it.next();
			if(tmpp == this.player)continue;
			List<Object> tmpinfo1 = getPlayerInfoList(tmpp);
			others.add(tmpinfo1);
		}
		System.out.println(others);
		MyArray arr2 = new MyArray();
		arr2.push(others);
		MyByteArray buf2 = new MyByteArray();
		buf2.write(arr2);
		this.player.send(11054, buf2.getBuf());// 获得房间中其余的玩家
		
		//广播自己加入放假消息
		player.getRoom().boradcastPlayerIn(player);
	}

	public void auto_deal(MgsPlayer p)
	{
		this.player = p;
		//id,等级、名字、形象、装备技能、金币
		MJ_User user = p.getBusiness().getPlayer();
		
		MyArray arr = new MyArray();
		List<Object> tmpinfo = getPlayerInfoList(this.player);
		List<Object> xx1 = new ArrayList<Object>();
		xx1.add(tmpinfo);
		arr.push(xx1);
		Log.info("----->54 palyerInfo :" + xx1);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		this.player.getRoom().SendOtherBroadcast(11054, buf1.getBuf(), this.player);// 广播 自己信息
	}
	
	public List<Object> getPlayerInfoList(MgsPlayer p)
	{
		MJ_User user = p.getBusiness().getPlayer();
		List<Object> list = new ArrayList<Object>();
		
		list.add(p.getLocId());
		list.add(user.getLevel());
		list.add(user.getNick());
		list.add(user.getImage());
		String[] skills = user.getEquipSkill().split(",");
		List<Integer> list1 = new ArrayList<Integer>();
		for(int i = 0 ; i < skills.length;i++)
		{
			String tmp = skills[i];
			if(tmp.equals("") ||skills.equals(" "))
				continue;
			list1.add(Integer.parseInt(tmp));
		}
		list.add(list1);
		list.add(user.getGold());
		list.add(p.zhunbei);
		list.add(user.getSex());
		return list;
	}

		



}
