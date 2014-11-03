package server.command.cmd;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import server.command.CMD;
import server.command.GlobalData;
import server.command.Login360;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.Business;
import business.CountDao;
import business.conut.Sts_MuteBlacklist;
import business.conut.Sts_RewardProp;
import business.conut.Sts_UserCup;
import business.entity.MJ_Cup;
import business.entity.MJ_User;

import common.MyArrays;


public class CCMD10001 extends  CMD implements TimerTask{

	private static final Logger log = Logger.getLogger(CCMD10001.class.getName());
	
	//每天单机最多场数
	public static List<Integer> MAX_CHANG_DANJI = MyArrays.asList(3,3,2,2,2,1,1,1,0,0);
	
	private Business  business ;
	private MgsPlayer exsitedPlayer; 

	private String name;
	private String pwd;
	private int loginType = 0;
	private String pingtai = "";
	
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
		//解析指令
//		super.setBytes(buf);
		
		log.info("PROGRAM_VERSION:"+GlobalData.PROGRAM_VERSION);
		
		int version  = buf.readShort();
		name  =this.readUTF(buf);
		pwd   =this.readUTF(buf);
		loginType = buf.readByte();
		if(buf.readableBytes() > 0)
		{
			pingtai = this.readUTF(buf);
		}
		this.player.platform = pingtai;
//		System.out.println(name +"      "+ pwd + "     " + loginType + "    " + pingtai);
		if(version != Global.SERVER_VERSION)
		{
			log.warning("版本号不对!服务器版本号:"+Global.SERVER_VERSION+",收到:"+version);
			SCMD10001 scmd = new SCMD10001();
			scmd.setPlayer(player);
			scmd.setFlag(4);
			if(version > Global.SERVER_VERSION)
			{
				scmd.setFlag(2);
			}
			this.player.send(10001, scmd.getBytes(null)); //版本号不同
			return;
		}

//		Hashtable<String, MgsPlayer> tmp11 =  MgsCache.getInstance().getUserInfos();
		boolean isExisted = MgsCache.getInstance().getUserInfos().containsKey(name);
		if(isExisted)
		{
			haveSeed = true;
			exsitedPlayer  = MgsCache.getInstance().getUserInfos().get(name); 
			String exittPwd = exsitedPlayer.getBusiness().getPlayer().getPwd();
			if(exittPwd.equals(pwd))
			{
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
//				CCMD11056 c56 = new CCMD11056();
//				c56.audo_deal(exsitedPlayer);
				MgsCache.getInstance().removeCache(exsitedPlayer);
			}else
			{
				exsitedPlayer = null;
				haveSeed = false;
			}
		}else
		{
			if("360".equals(pingtai))
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
		//4 密码错误  3登陆人数超上限   0 错误  1 登陆   5. 已经在线  6 版本号不对
		loginF(name,pwd,isExisted);
	}
	/**
	 * 360登陆验证方法
	 */
	public boolean login_360()
	{
		//name  == Authorization Code
		Login360 login360 = new Login360();
		boolean b = login360.login(this.name,this.player);
		return b;
	}
	
	/**
	 * @param name
	 * @param pwd
	 * @param version
	 *   5.已经在线  6. 版本号错误
	 */
	public void loginF(String name,String pwd,boolean broast)
	{
		System.out.println("登陆....");
		int flag    = business.login_auto(name,pwd,loginType,pingtai);
		MJ_User m_player = business.getPlayer() ;
		SCMD10001 scmd = new SCMD10001();
		scmd.setPlayer(this.player);
		System.out.println("flag:"+flag);
		switch(flag)
		{
			case  7:
				
				scmd.setFlag(7);
				this.player.send(10001, scmd.getBytes(this));
				return;  //7密码错误  
			case  8: 
				scmd.setFlag(8);
				this.player.send(10001, scmd.getBytes(this));
				return;  //8用户名可用
			case 9:
				scmd.setFlag(9);
				this.player.send(10001, scmd.getBytes(this));
				return;  //8用户名可用
				
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

		if(this.player == null)return;
//		business.rate_of_return1(m_player);
		switch(flag)
		{
			case 0: 
					scmd.setFlag(0);			break;
			case 1: 
					this.player.setName(m_player.getName());
					if(!haveSeed)this.player.getCmdRand().setSeed();
					int seed  = this.player.getCmdRand().getSeed();
					scmd.setSeed(seed);
					scmd.setFlag(1);				break;
			case 2: 
					scmd.setFlag(2);				break;
			case 3: 
					scmd.setFlag(3);				break;
			case 4: 
					scmd.setFlag(4);				break;
			case 5:
					scmd.setFlag(5);				break;
			case 6: scmd.setFlag(6); 				break; //黑名单
		}
		
		if(flag == 1)
		{
			
			CCMD11101 cmd101  = new CCMD11101();
			cmd101.setPlayer(this.player);
			cmd101.sendUserInfo(MyArrays.asList(1,2,3,4,5,6,7,9,11,12,13,21,22,23,24,31,33));
			this.player.send(10001, scmd.getBytes(m_player));
			//公告
			CCMD11113 cmd113 = new CCMD11113();
			cmd113.auto_deal_one(player);
			//重连
			if(flag == 1)MgsCache.getInstance().addToCache(this.player);
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
			
		}else if(flag == 6)
		{
			this.player.send(10001, scmd.getBytes(m_player));
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(this.player, "你被加入黑名单中，\n如有疑问请与管理员联系！");
		}else 
		{
			this.player.send(10001, scmd.getBytes(m_player));
		}
	}
	
	public void run(Timeout arg0) throws Exception {
		
		if(exsitedPlayer!= null && exsitedPlayer.isOnLine()) //正在线，不能连接
		{
			SCMD10001 scmd = new SCMD10001();
			scmd.setFlag(5);
			this.player.send(10001, scmd.getBytes(null)); 
			return;
		}else  //可以连接， 清理掉上一个的缓存
		{
			if(exsitedPlayer != null)exsitedPlayer.closing();
			loginF(name,pwd,true);
		}
	}
	
	


}
