package server.http;

import java.util.Calendar;

import server.mj.MgsCache;
import server.mj.ServerTimer;
import business.CountDao;
import business.conut.Sts_MuteBlacklist;
import common.Log;

public class Html_AddJinyan implements IHtml{

	@Override
	public String getHtml(String content) {
		
		int now = ServerTimer.distOfSecond(Calendar.getInstance());
		CountDao cdao = new CountDao();
		//1黑名单   0禁言...
		String mesage="1";
		String contents[] = content.split("&");
		String namevalue="";
		if(contents[0].split("=").length>1){
			namevalue=contents[0].split("=")[1];
		}
		String typevalue=contents[2].split("=")[1];
		String day="1";
		int d=1;
		
		if(contents[3].split("=").length>1){
			day=contents[3].split("=")[1];
		}
		d=Integer.parseInt(day);
		d = d *24*60*60;
		 if(namevalue.length()>0){
				mesage="1";
				boolean nickEnable =	cdao.checkNickEnable(namevalue);
				if(!nickEnable)
						return "0";
				if(typevalue.equals("0")){  
					//禁言
					Log.log("所该天数为:-->"+d);
					Sts_MuteBlacklist mute_black = cdao.findMute_Black(namevalue);
					if(mute_black == null)
					{
						if(d == 0)return "0";
						mute_black = new Sts_MuteBlacklist();
						mute_black.setNick(namevalue);
						mute_black.setBlack(false);
						mute_black.setMute(true);
						mute_black.setMuteDays(now + d);
						mute_black.setBlackDays(0);
					}else
					{
						if(d == 0)
						{
							//如果在线， 解除禁言
							MgsCache.getInstance().jiechuJInyan(mute_black.getNick());
						}
						
//						if(mute_black.getBlackDays() > 0 && d == 0)
//						{
//							mute_black.setMuteDays(0);
//							mute_black.setMute(false);
//						}
						if(d == 0) //删除记录
						{
//							cdao.deleteSts_Object(mute_black);
//							return "1";
							mute_black.setMuteDays(0);
							mute_black.setMute(false);
							
						}else if(d != 0)
						{
							mute_black.setMuteDays(now + d);
							mute_black.setMute(true);
						}
					}
					cdao.saveSts_Object(mute_black);
					//查询sts_mute_blackLst;
					//保存数据
				}else if(typevalue.equals("1")){
				    //加黑名单...
					Sts_MuteBlacklist mute_black = cdao.findMute_Black(namevalue);
					if(mute_black == null)
					{
						if(d == 0)return "0";
						mute_black = new Sts_MuteBlacklist();
						mute_black.setNick(namevalue);
						mute_black.setBlack(true);
						mute_black.setMute(false);
						mute_black.setMuteDays(0);
						mute_black.setBlackDays(now + d);
					}else
					{
//						if(mute_black.getMuteDays() > 0 && d== 0)
//						{
//							mute_black.setBlack(false);
//							mute_black.setBlackDays(0);
//						}
						if(d == 0)
						{
//							cdao.deleteSts_Object(mute_black);
//							return "1";
							mute_black.setBlack(false);
							mute_black.setBlackDays(0);
						}else if(d != 0)
						{
							mute_black.setBlack(true);
							mute_black.setBlackDays(now + d);
						}
					}
					cdao.saveSts_Object(mute_black);
				}
		 }else{
			    mesage="0";
		 }
		return mesage;
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
