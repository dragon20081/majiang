package server.command.cmd;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import server.command.CMD;
import server.command.Login360;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.Business;
import business.CountDao;
import business.conut.Sts_MuteBlacklist;
import business.conut.Sts_RewardProp;
import business.entity.MJ_User;

import common.MyArrays;


public class CCMD11301 extends  CMD {

	
	//每天单机最多场数
	public static List<Integer> MAX_CHANG_DANJI = MyArrays.asList(3,3,2,2,2,1,1,1,0,0);
	
	private Business  business ;
	private MgsPlayer exsitedPlayer; 

	private String name;
	private String code360;
	private String platform = "";
	private int version = 0;
	
	
	public static final String PRE_360 = "360MG_";
	private boolean haveSeed = false;
	
	public void setPlayer(MgsPlayer player) {
		this.player = player;
		this.business  = this.player.getBusiness();
	}
	public ChannelBuffer getBytes() {
		return null;
	}

	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		Map<Object,Object> map = this.getMapValue(0);
		
		
		version  = (Integer) map.get("版本号");
		name  = (String) map.get("登录名");
		code360 = (String) map.get("360用户Code");
		platform = (String)map.get("平台");
		
		this.player.platform = platform;
		if(version != Global.SERVER_VERSION)
		{
			SCMD10001 scmd = new SCMD10001();
			scmd.setPlayer(player);
			scmd.setFlag(4);
			if(version > Global.SERVER_VERSION)
			scmd.setFlag(2);
			this.player.send(10001, scmd.getBytes(null)); //版本号不同
			return;
		}
		boolean isExisted =  false;
		if(name != null)
		{
			name = PRE_360+name;
			isExisted = MgsCache.getInstance().getUserInfos().containsKey(name);
		}
		if(isExisted)
		{
			haveSeed = true;
			exsitedPlayer  = MgsCache.getInstance().getUserInfos().get(name); 
			
				this.player.CacheBufMap = exsitedPlayer.CacheBufMap;
				this.player.cacheMapKeys = exsitedPlayer.cacheMapKeys;
				this.player.getCmdRand().setSeed(exsitedPlayer.getCmdRand().getSeed());
				this.player.cup_type = exsitedPlayer.cup_type;
				this.player.cupOpen = exsitedPlayer.cupOpen;
				this.player.danji_type = exsitedPlayer.danji_type;
				this.player.danjiOpen = exsitedPlayer.danjiOpen;
				
				this.player.userCup = exsitedPlayer.userCup;
				this.player.userDanji = exsitedPlayer.userDanji;
				this.player.setTokenMap(exsitedPlayer.getTokenMap());
				this.player.setUserinfo_360(exsitedPlayer.getUserinfo_360());
				
				exsitedPlayer.setOnLine(false);
				if(exsitedPlayer.getChannel() !=null)exsitedPlayer.getChannel().close();
				MgsCache.getInstance().removeCache(exsitedPlayer);
		}else
		{
			if("360".equals(platform) && this.code360 != null)
			{
					boolean b = this.login_360();
					if(!b) //登陆失败
					{
						SCMD10001 scmd = new SCMD10001();
						scmd.setPlayer(player);
						scmd.setFlag(2);
						this.player.send(10001, scmd.getBytes(null)); //登陆失败
						return;
					}
			}
		}
		String id = this.player.getUserinfo_360().get("id");
		id = PRE_360 + id;
		loginF(id,isExisted);
	}
	/**
	 * 360登陆验证方法
	 */
	public boolean login_360()
	{
		Login360 login360 = new Login360();
		boolean b = login360.login(this.code360,this.player);
		return b;
	}
	/**
	 * @param name
	 * @param pwd
	 * @param version
	 *   5.已经在线  6. 版本号错误
	 */
	public void loginF(String name,boolean broast)
	{
		boolean loginResult = business.login_360(name);
		
		int flag = 0;
		
		MJ_User m_player = business.getPlayer() ;
		SCMD10001 scmd = new SCMD10001();
		scmd.setPlayer(this.player);
		if(!loginResult)
		{
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(this.player, "登陆失败!");
			this.failMsg();
			return;  //登陆失败
		}else
		{
			flag = 1;
		}
		// 检查是否在黑名单中
		CountDao cdao = new CountDao();
		int now = ServerTimer.distOfSecond(Calendar.getInstance());
		Sts_MuteBlacklist mb = null;
		if(m_player != null) mb= cdao.findMute_Black(m_player.getNick());
		
		if(mb != null)
		{
			if(mb.getBlackDays() > now)
			{
				flag = 6;
				mb.setBlack(true);
			}else
			{
				mb.setBlack(false);
				mb.setBlackDays(0);
			}
			if(mb.getMuteDays() > now)
			{
				this.player.time_jiejin = mb.getMuteDays();
				this.player.mute = true;
				mb.setMute(true);
			}else
			{
				mb.setMute(false);
				mb.setMuteDays(0);
			}
			cdao.saveSts_Object(mb);
		}
		
		if(flag == 1)
		{
			this.player.setName(m_player.getName());
			if(!haveSeed)this.player.getCmdRand().setSeed();
			sendSuccessMsg();
			
		}else if(flag == 6)
		{
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(this.player, "你被加入黑名单中，\n如有疑问请与管理员联系！");
			failMsg();
		}
	}
	
	private void sendSuccessMsg()
	{
		CCMD11101 cmd101  = new CCMD11101();
		cmd101.setPlayer(this.player);
		cmd101.sendUserInfo(MyArrays.asList(1,2,3,4,5,6,7,9,11,12,13,21,22,23,24,31,33));
		
		int seed  = this.player.getCmdRand().getSeed();
		String token = this.player.getTokenMap().get("access_token");
		Map<Object,Object> map = new HashMap<Object, Object>();
		map.put("结果", true);
		map.put("种子", seed);
		map.put("登录名",this.player.getName());
		map.put("token",token);
		map.put("信息", this.player.getUserinfo_360());
		
		MyByteArray myBuf = new MyByteArray();
		myBuf.write(map);
		this.player.send(11301, myBuf.getBuf());
		//公告
		CCMD11113 cmd113 = new CCMD11113();
		cmd113.auto_deal_one(player);
		//重连
		MgsCache.getInstance().addToCache(this.player);
		Hashtable<String, MgsPlayer> t = MgsCache.getInstance().getUserInfos();
		this.player.startHeartBeat();
		//是否是今天第一次登陆
		MJ_User user =this.player.getBusiness().getPlayer();
		if(user.firstLogin_today)
		{
			user.setDanJiChangs(MAX_CHANG_DANJI);
			user.setDanjiCount(0);
			user.setOpenedDanji(-1);
			user.setOpenedCup(-1);
		}
		CountDao cdao = new CountDao();
		Sts_RewardProp reward = cdao.findTodayRewardProp(user);
		cdao.saveSts_Object(reward);
		cmd101.sendUserInfo(MyArrays.asList(32));
		
		if(user.getOpenedDanji()  > 0)
		{
			CCMD11102 cmd102 = new CCMD11102();
			cmd102.setPlayer(player);
			user.setOpenedDanji(-2);
			cmd102.mod_gold(100);
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(player, "系统检测到你上次在人机对战中意外退出,补偿金币:" + 100 +"\n每日限一次,所以请注意你的网络哟！");
		}
		CCMD11303 cmd303 = new CCMD11303();
		cmd303.setPlayer(this.player);
		cmd303.getUnVerifiedOrder();
	}
	private void failMsg()
	{
		Map<Object,Object> map = new HashMap<Object, Object>();
		map.put("结果", false);
		MyByteArray myBuf = new MyByteArray();
		myBuf.write(map);
		this.player.send(11301, myBuf.getBuf());
	}
	

}
