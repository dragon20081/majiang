package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import business.entity.MJ_User;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.SCMD3;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.Room;
import server.mj.RoomManager;

/**
 *  加入房间
 * @author xue
 */
public class CCMD11053 extends  CMD{
	
	
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
		int roomID = this.getIntVaule(0);
		
		MJ_User usr = this.player.getBusiness().getPlayer();
		
		RoomManager rManager = Global.rManager;
		boolean gaming = rManager.isInGaming(usr.getName());
		if(gaming)
		{
			CCMD11053 cmd053 = new CCMD11053();
			cmd053.auto_deal_fail(player, "上一局游戏还未结束，请等待！", true);
			return;
		}
		
		if(usr.getImage() < 0)
		{
			auto_deal_fail(player,"没有形象，加入房间失败",true);
			return;
		}
		if(roomID == 0)// 自动加入
		{
			boolean b = rManager.automath(player,0,0,0);
			if(b)
			{
				auto_deal(player,true);
			//	player.getRoom().boradcastPlayerIn(player);
			}else
			{
				auto_deal_fail(player,"当前没有空闲的房间",true);
			}
			return;
		}
		Room r = rManager.waitinRooms.get((Integer)roomID);
		if(r != null)
		{
			if(r.getPlayers().size() >=4) // r.getPlayerLimit()
			{
				auto_deal_fail(player,"房间已满" ,true);
				return;
			}
			r.addPlayer(player);
			auto_deal(player,true);
			//r.boradcastPlayerIn(player);
		}else
		{
			r = rManager.gamingRooms.get((Integer)roomID);
			if(r == null )
			auto_deal_fail(player,"加入房间失败!" ,true);
			else
			auto_deal_fail(player,"房间正在游戏中!" ,true);
		}
	}
	public void auto_deal(MgsPlayer p,boolean cache)
	{
		Room r = p.getRoom();
		MyArray arr = new MyArray();
		arr.push(r.getRoomID());
		arr.push(p.getLocId());
		arr.push(r.getManager().getLocId());
		arr.push(r.getPlayerLimit());
		arr.push(r.getChipsLimit());
		arr.push(r.getBeishu());
//		arr.push(r.getSkillLimit());
		boolean skill = r.getSkillLimit() == 0? false : true;
		arr.push(skill);
		arr.push(r.getQuanLimit());
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		p.send(11053, buf.getBuf());
		//其他玩家信息
		
	}
	public void auto_deal_fail(MgsPlayer p,String msg,boolean cache)
	{
		SCMD3 scmd3 = new SCMD3();
		MyArray arr = new MyArray();
		arr.push(-1);
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		scmd3.list.add(p.coderCMD(11053, buf.getBuf()));
		scmd3.list.add(new CCMD11111().getBuf(p, msg));
		p.send(3, scmd3.getBytes());
	}

	public ChannelBuffer getBuf(MgsPlayer p)
	{
		Room r = p.getRoom();
		MyArray arr = new MyArray();
		arr.push(r.getRoomID());
		arr.push(p.getLocId());
		arr.push(r.getManager().getLocId());
		arr.push(r.getPlayerLimit());
		arr.push(r.getChipsLimit());
		arr.push(r.getBeishu());
		boolean skill = r.getSkillLimit() == 0? false : true;
		arr.push(skill);
		arr.push(r.getQuanLimit());
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		return p.coderCMD(11053, buf.getBuf());
	}

		



}
