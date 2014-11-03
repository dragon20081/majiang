package server.mj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import server.command.cmd.CCMD11113;

import business.CountDao;
import business.conut.Sts_Notice;
import business.conut.Sts_Online;

import common.MyArrays;

/**
 * 玩家数据缓存，适用于游戏房间内部掉线 （暂时不做）
 * @author xue
 */
public class MgsCache implements TimerTask{
	private static final int MAX_CACHE =   2000;
	private static final Logger logger = Logger.getLogger(MgsCache.class.getName());
	
	private static MgsCache _instance;
	
	public  Hashtable<String, MgsPlayer> userInfos = new Hashtable<String, MgsPlayer>();
	
	private boolean controllFlag  = false;
	private int totalTime = 0;
	private CountDao cdao;
	public List<Sts_Notice> notices;
	
	public static MgsCache getInstance()
	{
		if(_instance == null)
		{
			_instance = new MgsCache();
		}
		return _instance;
	}
	public MgsCache(){
	
		cdao = new CountDao();
		notices = cdao.findSts_Notice();
		start();
	}
	public void start()
	{
		if(controllFlag == false){
			Global.timer.newTimeout(this, 2, TimeUnit.SECONDS);
			}
	}
	public synchronized void addToCache(MgsPlayer p)
	{
		String name =  p.getBusiness().getPlayer().getName();
		userInfos.put(name, p);
		Global.addPlayer(p);
	}
	public synchronized void removeCache(MgsPlayer p)
	{
		Global.removePlayer(p);
		if(p.getBusiness().getPlayer() == null)return;
		String name  =  p.getBusiness().getPlayer().getName();
		if(this.userInfos.containsKey(name))
			this.userInfos.remove(name);
	}
	public synchronized MgsPlayer getOnlnePlayer(String name)
	{
		MgsPlayer p = null;
		if(this.userInfos.containsKey(name))
			p = this.userInfos.get(name);
		return p;
	}
	/**
	 * @param list  需要清除缓存的list列表
	 * @param count 是否需要统计掉线率
	 */
//	public synchronized ChannelBuffer removeCacheList(List<MgsPlayer> list,boolean count)
//	{
//		ChannelBuffer tmp = ChannelBuffers.dynamicBuffer();
//		for(int i = 0 ; i <  list.size() ;i++)
//		{
//			MgsPlayer p  = list.get(i);
//			String name  =  p.getBusiness().getPlayer().getName();
//			if(this.userInfos.containsKey(name))
//				this.userInfos.remove(name);
//			if(count)
//			{
//				MJ_User user  =  p.getBusiness().getPlayer();
//				user.setTaopaoRate(user.getTaopaoRate() + 1);
//				p.getBusiness().savePlayer(user);
//			}
//		}
//		return tmp;
//	}
	public void run(Timeout timeout) throws Exception {
		
		Calendar now = Calendar.getInstance();
		totalTime += 10;
		if(totalTime % 600  == 0) //10分钟记录平均在线
		{
		   saveAverOnline();
		   broadcastNotice();
		}
		
		
		//检查玩家是否掉线超时

		long timeNum = now.getTimeInMillis()+28800000;
		int seconds  = (int) (timeNum / 1000);
		Iterator<String> it  = this.userInfos.keySet().iterator();
		List<String> removeUids = new ArrayList<String>();
		
		while(it.hasNext())
		{
			String name  =  it.next();
			MgsPlayer p  =  (MgsPlayer) this.userInfos.get(name);
			if(p.getOffLineTime() == -1) continue;
			int passedTime  =  seconds  -  p.getOffLineTime();
			if((int)(passedTime/ 60) >= 5) // 超时5分钟,清理出缓存
			{
				removeUids.add(name);
			}
		}
		for(int i  = 0 ;  i< removeUids.size();i++)
		{
			String name  =  removeUids.get(i);
			this.userInfos.remove(name);
		}
		Global.timer.newTimeout(this, 10, TimeUnit.SECONDS);
	}
	
	public synchronized void saveAverOnline()
	{
		Calendar now = Calendar.getInstance();
		List<Integer> list = this.getOnlineInfo();
		Sts_Online online = cdao.findTodaySts_OnLine();
		if(online == null)
			online = new Sts_Online(); //allOnline,chang2,chang4,dating
		online.setAbsDay(ServerTimer.distOfDay(now));
		online.setDay(ServerTimer.getDay());
		online.setOlplayer(online.getOlplayer() + ServerTimer.distOfMinute(now)+":"+list.get(0)+";");
		online.setOlChang(online.getOlChang() + ServerTimer.distOfMinute(now)+":" + list.get(1)+":"+ list.get(2)+";");
		online.setOlDating(online.getOlDating() + ServerTimer.distOfMinute(now)+":" +list.get(3)+";");
	   cdao.saveSts_Object(online);
	}
	public synchronized void broadcastNotice()
	{
		if(notices.size() == 0)return;
		Sts_Notice notice = notices.remove(0);
		CCMD11113 cmd113 = new CCMD11113();
		cmd113.auto_deal_all(notice.getContentStr());
		notices.add(notice);
	}
	public synchronized void deleteNotice(String day)
	{
		Sts_Notice n = null;
		for(int i = 0; i < this.notices.size();i++)
		{
			Sts_Notice tmp = this.notices.get(i);
			if(tmp.getDay().equals(day))
			{
				n = tmp;
				notices.remove(i);
				break;
			}
		}
		if(n == null)return;
		cdao.deleteSts_Object(n);
	}
	public synchronized void saveNotice(Sts_Notice notice)
	{
		CCMD11113 cmd113 = new CCMD11113();
		cmd113.auto_deal_all(notice.getContentStr());
		notices.add(notice);
		cdao.saveSts_Object(notice);
	}
	
	public synchronized List<Integer> getOnlineInfo()
	{
		int allOnline = 0;
		int chang2 = 0;
		int chang4 = 0;
		int dating = 0;
		
		Iterator<MgsPlayer> it = this.userInfos.values().iterator();
		while(it.hasNext())
		{
			MgsPlayer p = it.next();
			if(p.offline)continue;
			allOnline++;
			if(p.getRoom() != null)
			{
				if(p.getRoom().getPlayerLimit() == 2)
					chang2++;
				else
					chang4++;	
			}else
			{
				dating++;
			}
		}
		List<Integer> list = MyArrays.asList(allOnline,chang2,chang4,dating);
		return list;
	}
/////////////////////////////////////////////////////////////////////////////////////
	//userInfos 是线程安全的，可以同时操作
	public Hashtable<String, MgsPlayer> getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(Hashtable<String, MgsPlayer> userInfos) {
		this.userInfos = userInfos;
	}
	
	public void jiechuJInyan(String nick)
	{
		Iterator<MgsPlayer> it = userInfos.values().iterator();
		try {
			while(it.hasNext())
			{
				MgsPlayer p = it.next();
				if(p.getBusiness().getPlayer().getNick().equals(nick))
				{
					p.mute = false;
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}