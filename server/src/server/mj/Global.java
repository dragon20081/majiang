package server.mj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import sun.misc.BASE64Encoder;

import business.Business;
import business.entity.MJ_Cup;
import business.entity.MJ_DanJil;
import business.entity.MJ_DataFan;
import business.entity.MJ_DayTask;
import business.entity.MJ_PayInfo;
import business.entity.MJ_Resource;
import business.entity.MJ_Skill;
import business.entity.MJ_TmpResource;
import business.entity.M_Shop;


public final class Global {
//	private static final Logger logger = Logger.getLogger(Global.class.getName());

	public static final int SERVER_VERSION  =  2;
	
	public static final Hashtable<String,MgsPlayer> players = new Hashtable<String,MgsPlayer>();
//	public static final Hashtable<Integer, MJ_User> today_players = new Hashtable<Integer, MJ_User>();
	public static final ChannelGroup worldChannelGroup = new DefaultChannelGroup();
	public static final ChannelGroup chatChannnelGroup = new DefaultChannelGroup();
	public static final ArrayList<ChannelBuffer> notice = new ArrayList<ChannelBuffer>();
	public static final ArrayList<ChannelBuffer> speaker = new ArrayList<ChannelBuffer>();
	public static final Vector<MgsPlayer> robot = new Vector<MgsPlayer>();
	public static Timer timer = new HashedWheelTimer(200,TimeUnit.MILLISECONDS);
	public static String notices = "这是一条公告";
	//等待匹配队列  房间数据
	public static final ArrayList<MgsPlayer> autoMathList  =  new ArrayList<MgsPlayer>();
//	public static final ArrayList<Room> waitRoomList  =  new ArrayList<Room>();
	
//	public static final Map<Integer,M_Shop> shopItems = new HashMap<Integer, M_Shop>();
	
	// 分成 4个类别  形象 道具  技能 金币
	public static final Map<Integer,M_Shop> shopItem_roles  = new LinkedHashMap<Integer, M_Shop>();
	public static final Map<Integer,M_Shop> shopItem_skills = new LinkedHashMap<Integer, M_Shop>();
	public static final Map<Integer,M_Shop> shopItem_props  = new LinkedHashMap<Integer, M_Shop>();
	public static final Map<Integer,M_Shop> shopItem_gold   = new LinkedHashMap<Integer, M_Shop>();
	
	public static final Map<Integer,MJ_PayInfo> payItems   = new LinkedHashMap<Integer, MJ_PayInfo>();
	public static final Map<Integer,MJ_DayTask> tasks   = new LinkedHashMap<Integer, MJ_DayTask>();
	public static final Map<String,MJ_Skill> skills   = new LinkedHashMap<String, MJ_Skill>();
	public static final Map<String,MJ_DataFan> fans   = new LinkedHashMap<String, MJ_DataFan>();
	
	public static final Map<Integer,MJ_DanJil> danji   = new LinkedHashMap<Integer, MJ_DanJil>();
	public static final Map<Integer,MJ_Cup> cups   = new LinkedHashMap<Integer, MJ_Cup>();
	
	public static List<MJ_Resource> resourceList  = null;
	public static Map<String,MJ_Resource> resMap = new LinkedHashMap<String, MJ_Resource>();
	
	public static List<MJ_TmpResource> tmpresourceList  = null;
	public static Map<String,MJ_TmpResource> tmpresMap = new LinkedHashMap<String, MJ_TmpResource>();
	
	public static final RoomManager rManager = new RoomManager();
	static {
		int i = 0;
		System.out.println("load shop data");
		Business b = new Business();
		List<M_Shop> tmp =	b.findShopItem();
		for(i = 0 ;i < tmp.size();i++)
		{
			M_Shop tmpshop = tmp.get(i);
			if(tmpshop.getType().equals("形象"))
				shopItem_roles.put(tmpshop.getProId(),tmpshop);
			else if(tmpshop.getType().equals("技能"))
				shopItem_skills.put(tmpshop.getProId(),tmpshop);
			else if(tmpshop.getType().equals("道具"))
				shopItem_props.put(tmpshop.getProId(),tmpshop);
			else if(tmpshop.getType().equals("金币"))
				shopItem_gold.put(tmpshop.getProId(),tmpshop);
		}
		resourceList = b.findResourceItem();
		
		for(i = 0; i < resourceList.size();i++)
		{
			MJ_Resource r = resourceList.get(i);
			resMap.put(r.getRid(), r);
		}
		
		tmpresourceList = b.findTmpResourceItem();
		
		for(i = 0; i < tmpresourceList.size();i++)
		{
			MJ_TmpResource r = tmpresourceList.get(i);
			tmpresMap.put(r.getRid(), r);
		}
		
		List<MJ_PayInfo> payList = b.findPayItem();
		for(i = 0; i < payList.size(); i++)
		{
			MJ_PayInfo pay = payList.get(i);
			payItems.put(pay.getPayId(), pay);
		}
		List<MJ_DayTask> taskList = b.findTaskList();
		for(i = 0; i < taskList.size(); i++)
		{
			MJ_DayTask task = taskList.get(i);
			tasks.put(task.getTaskId(), task);
		}
		
		List<MJ_Skill> skillList = b.findSkillList();
		for(i = 0; i < skillList.size(); i++)
		{
			MJ_Skill skill = skillList.get(i);
			skills.put(skill.getName(), skill);
		}
		
		
		List<MJ_DataFan> fanList = b.findFanList();
		for(i = 0; i < fanList.size(); i++)
		{
			MJ_DataFan fan = fanList.get(i);
			fans.put(fan.getName(), fan);
		}
		
		List<MJ_DanJil> danjiList = b.findDanjiList();
		for(i = 0; i < danjiList.size(); i++)
		{
			MJ_DanJil dj = danjiList.get(i);
			danji.put(dj.getId(), dj);
		}
		
		List<MJ_Cup> cupList = b.findCupList();
		for(i = 0; i < cupList.size(); i++)
		{
			MJ_Cup cup = cupList.get(i);
			cups.put(cup.getId(), cup);
		}
	}
	public static synchronized String BASE64Encod(String s) {   
		if (s == null) return "";   
		return (new BASE64Encoder()).encode( s.getBytes() );   
	}   
	public static synchronized  void addToAutoMathQueue(MgsPlayer p)
	{
	}
	public static void addPlayer(MgsPlayer player)
	{
		Global.players.put(player.getName(), player);
		int size = Global.players.size();
		Calendar now = Calendar.getInstance();
    	long timeNum = now.getTimeInMillis()+28800000;
    	int currentTime = (int)(timeNum/86400000);
    	worldChannelGroup.add(player.getChannel());
	}
	public static void removePlayer(MgsPlayer player)
	{
		Global.players.remove(player.getName());
		if(player != null&&player.getChannel()!= null)worldChannelGroup.remove(player.getChannel());
	}
	public static synchronized  MgsPlayer getRobot()
	{
		MgsPlayer robot1 =  null;
		if(robot.size() >0)
			robot1  = robot.remove(0);
		return robot1;
	}
	public static synchronized void giveBackRobot(MgsPlayer robot1)
	{
		if(robot1 != null)robot.add(robot1);
	}
}
