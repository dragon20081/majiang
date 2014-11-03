package server.command.cmd;

import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;

import business.conut.Sts_Chat;
import business.entity.MJ_User;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.MgsRoom;
import server.mj.Room;
import server.mj.ServerTimer;

/**
 *  房间内部聊天
 * @author xue
 */
public class CCMD11059 extends  CMD{
	
	
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
		if(this.player.getRoom() == null)
			return;
		if(this.player.mute)
		{
			int now = ServerTimer.distOfSecond(Calendar.getInstance());
			if(now >= this.player.time_jiejin)  //解禁
			{
				this.player.mute = false;
			}else
			{
				String str =getDayStr(this.player.time_jiejin - now);
				CCMD11111 cmd111 = new CCMD11111();
				cmd111.auto_deal(this.player, "你已被禁言，剩余时间"+str+",\n如有疑问请与管理员联系！");
				return;
			}
		}
		String msg = this.getStrValue(0);
		Room r = this.player.getRoom();
		MyArray arr = new MyArray();
		arr.push(this.player.getLocId());
		arr.push(msg);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		r.SendRoomBroadcast(11059, buf1.getBuf());
		
		//存储聊天消息
		MJ_User user = this.player.getBusiness().getPlayer();
		Sts_Chat chat = new Sts_Chat();
		chat.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		chat.setDay(ServerTimer.getNowString());
		chat.setUid(user.getUid());
		chat.setAccount(user.getName());
		chat.setNick(user.getNick());
		chat.setChatMsg(msg);
		this.player.getBusiness().saveDataObject(chat);
		
	}

	public String getDayStr(int dayNum)
	{
		if(dayNum <= 0)return "0天0时0分0秒";
		int miao = dayNum % 60;
		int fen = dayNum/60 %60;
		int xiaoshi = dayNum/60/60%24;;
		int tian = dayNum/24/60/60;
		return  tian+"天 "+xiaoshi+"时 "+ fen+"分 " + miao+"秒";
	}
		



}
