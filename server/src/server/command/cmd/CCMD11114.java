package server.command.cmd;

import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.CountDao;
import business.conut.Sts_BuyProp;
import business.conut.Sts_Chat;
import business.conut.Sts_UseProp;
import business.entity.MJ_User;
import business.entity.M_Prop;

/**
 *  喇叭
 * @author xue
 */
public class CCMD11114 extends  CMD{
	

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
		
		String louder = this.getStrValue(0);
		String nick = this.player.getBusiness().getPlayer().getNick();
		if("".equals(nick)|| " ".equals(nick))
			nick = "???";
		louder = nick+":" + louder;
		//检查喇叭数量
		M_Prop prop  = this.player.getBusiness().getPlayer().getProps();
		int ownLouder = prop.getPro1Num();
		if(ownLouder >= 1)
		{
			prop.setPro1Num(ownLouder -1);
			this.player.getBusiness().saveProp(prop);
			MyArray arr = new MyArray();
			arr.push(louder);
			MyByteArray bytearr = new MyByteArray();
			bytearr.write(arr);
			ChannelBuffer msg = this.player.coderCMD(11115, bytearr.getBuf());
			Global.worldChannelGroup.write(msg);
			saveUseLabaData();
		}
		//返回喇叭数量
		this.player.send(11114, null);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.replay_prop(player, 1);
		
		//存储聊天消息
		MJ_User user = this.player.getBusiness().getPlayer();
		Sts_Chat chat = new Sts_Chat();
		chat.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		chat.setDay(ServerTimer.getNowString());
		chat.setUid(user.getUid());
		chat.setAccount(user.getName());
		chat.setNick(user.getNick());
		chat.setChatMsg("喇叭:"+louder);
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
	
	public void saveUseLabaData()
	{
		MJ_User user = this.player.getBusiness().getPlayer();
		Sts_UseProp buy = new Sts_UseProp();
		initUseProp(user,buy);
		CountDao cdao = new CountDao();
		cdao.saveSts_Object(buy);
	}
	public void initUseProp(MJ_User user,Sts_UseProp use)
	{
		use.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		use.setDay(ServerTimer.getNowString());
		use.setTime1(ServerTimer.getDay());
		use.setTime2(ServerTimer.getMonth());
		use.setUid(user.getUid());
		use.setAccount(user.getName());
		use.setNick(user.getNick());
		use.mod_userProp(1, 1);
	}

}
