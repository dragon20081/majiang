package business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.sun.jmx.remote.internal.ServerCommunicatorAdmin;

import server.command.cmd.CCMD11008;
import server.mj.ServerTimer;
import business.conut.MJUserCharge;
import business.conut.Sts_360Order;
import business.conut.Sts_Arpu;
import business.conut.Sts_BuyProp;
import business.conut.Sts_Changci;
import business.conut.Sts_Chat;
import business.conut.Sts_DJ_Count;
import business.conut.Sts_DayTaskCompletion;
import business.conut.Sts_GoldDia;
import business.conut.Sts_JuqingDay;
import business.conut.Sts_LongTaskCompletion;
import business.conut.Sts_MJDevice;
import business.conut.Sts_MuteBlacklist;
import business.conut.Sts_Notice;
import business.conut.Sts_Online;
import business.conut.Sts_Recharge;
import business.conut.Sts_RewardProp;
import business.conut.Sts_Urr;
import business.conut.Sts_UserCup;
import business.conut.Sts_UserDanjiCount;
import business.entity.MJ_User;
import business.entity.M_State;

import common.Log;

public class CountDao {

	
	private static final Logger logger = Logger.getLogger(CountDao.class.getName());
	/**
	 * 查看是否有这个玩家
	 * @return
	 */
	public boolean checkNickEnable(String nick)
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.nick =:NICK");
			query.setParameter("NICK", nick);
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
			ss.close();
			return false;
		}
    	ss.close();
    	if(result == 1)return true;
		return false;
	}
	/**
	 * 保存数据
	 * @param buyProp
	 */
	public void saveSts_Object(Object obj)
	{
	    Session ss = HSF_Play.getSession();
		 Transaction ts=ss.beginTransaction();
	    try {
			ss.saveOrUpdate(obj);
			ts.commit();
		} catch (Exception e) {
			e.getStackTrace();
			ts.rollback();
		}
		ss.close();
	}
	/**
	 * 删除数据
	 */
	public void deleteSts_Object(Object obj)
	{
	    Session ss = HSF_Play.getSession();
		 Transaction ts=ss.beginTransaction();
	    try {
			ss.delete(obj);
			ts.commit();
		} catch (Exception e) {
			Log.error(e.getStackTrace());
			ts.rollback();
		}
		ss.close();
	}
	/**
	 * 查找今日返回率对象
	 * @return
	 */
	public Sts_Urr findTodayRR()
	{
		Sts_Urr urr = null;
		String now  = ServerTimer.getDay();
		Session ss = HSF_Play.getSession();
		try {
			List list =  ss.createCriteria(Sts_Urr.class)
			.add(Restrictions.eq("date", now))
			.list();
			ss.close();
			if(list.size() > 0)
				urr  = (Sts_Urr) list.get(0);
			else
			{
				 urr=new Sts_Urr();
				 urr.setDate(ServerTimer.getDay());
				 urr.setAbsTime(ServerTimer.distOfDay(Calendar.getInstance()));
				 saveUserRR(urr);
			}
;
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return urr;
	}
	/**
	 * 查找玩家注册当日返回率对象
	 * @param regDay
	 * @return
	 */
	public Sts_Urr findByRegDay(String regDay)
	{
	  	String now  = regDay.split(" ")[0];
		Sts_Urr urr = null;
		Session ss = HSF_Play.getSession();
		try {
			List list =  ss.createCriteria(Sts_Urr.class)
			.add(Restrictions.eq("date", now))
			.list();
			ss.close();
			if(list.size() > 0)
				urr  = (Sts_Urr) list.get(0);
			else
			{
				 urr=new Sts_Urr();
				 urr.setDate(ServerTimer.getDay());
				 urr.setAbsTime(ServerTimer.distOfDay(Calendar.getInstance()));
				 saveUserRR(urr);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return urr;
	}
	public void saveUserRR(Sts_Urr rr)
	{
		 Session s = HSF_Play.getSession();
		 Transaction ts=s.beginTransaction();
		 try {
			s.saveOrUpdate(rr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 ts.commit();
		 s.close();
	}
    public  List getTop300Info(){
	    List list = null;
	    Session s = HSF_Play.getSession();
		Criteria cx=s.createCriteria(Sts_Urr.class);
		cx.addOrder(Order.desc("absTime"));
		cx.setMaxResults(300);
		list=cx.list();
		s.close();
	    return list;
 }
    
    public M_State getState()
    {

	    M_State state = null;
	    Session s = HSF_Play.getSession();
		Criteria cx=s.createCriteria(M_State.class);
	    List list = cx.list();
		s.close();
		if(list.size() > 0)
		{
			state  = (M_State) list.get(0);
		}else 
		{
			state  = new M_State();
			saveState(state);
		}

	    return state;
    }
    
	public void saveState(M_State state)
	{
		 Session s = HSF_Play.getSession();
		 Transaction ts=s.beginTransaction();
		 try {
			s.saveOrUpdate(state);
		} catch (Exception e) {
			Log.error(e.getStackTrace());
		}
		 ts.commit();
		 s.close();
	}
	
	/************************************************
	 *  平均在线
	 */
	public List<Sts_Online> find100Sts_OnLine()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_Online> list = null;
		try {
			list =	ss.createCriteria(Sts_Online.class)
				.addOrder(Order.desc("absDay"))
				.setMaxResults(100)
				.setFirstResult(0)
				.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	public Sts_Online findTodaySts_OnLine()
	{
		int dayTime = ServerTimer.distOfDay(Calendar.getInstance());
		Session ss = HSF_Play.getSession();
		List<Sts_Online> list = null;
		try {
			list =	ss.createCriteria(Sts_Online.class)
					.add(Restrictions.eq("absDay",dayTime))
					.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		if(list == null || list.size() == 0)return null;
		Sts_Online obj = list.get(0);
		return obj;
	}
	/************************************************
	 *  场次统计
	 */
	public List<Sts_Changci> find300Sts_Changci()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_Changci> list = null;
		try {
			list =	ss.createCriteria(Sts_Changci.class)
				.addOrder(Order.desc("absDay"))
				.setMaxResults(300)
				.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	public Sts_Changci findTodayChangci()
	{
		int now = ServerTimer.distOfDay(Calendar.getInstance());
		Session ss = HSF_Play.getSession();
		List<Sts_Changci> list = null;
		try {
			list =	ss.createCriteria(Sts_Changci.class)
					.add(Restrictions.eq("absDay", now))
				.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		if(list == null || list.size() == 0)return null;
		return list.get(0);
	}
	/************************************************
	 *  公告管理
	 */
	public List<Sts_Notice> findSts_Notice()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_Notice> list = null;
		try {
			list =	ss.createCriteria(Sts_Notice.class)
					.addOrder(Order.desc("absDay"))
				.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	/************************************************
	 *  充值信息
	 */
	//查询单个玩家充值记录
	public List<Sts_Recharge> findPlayerRecharge(int uid,String account,String nick)
	{
		List<Sts_Recharge> list = null;
	    Session ss = HSF_Play.getSession();
	    Criteria cr =  ss.createCriteria(Sts_Recharge.class);
		try {
			if(uid != -1)
			{
				list = cr.add(Restrictions.eq("uid", uid))
						.addOrder(Order.desc("absDay"))
				.list();
			}else if(account != null)
			{
				list = cr.add(Restrictions.eq("account", account))
							.addOrder(Order.desc("absDay"))
							.list();
			}else if(nick != null)
			{
				list = cr.add(Restrictions.eq("nick", nick))
							.addOrder(Order.desc("absDay"))
							.list();
			}
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	//根据月份,指定日期查询充值记录
	public List<Sts_Recharge> findRechargeByDate(String dayStr)
	{
		List<Sts_Recharge> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_Recharge.class)
			.add(Restrictions.like("day", dayStr, MatchMode.START))
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error("fun findRechargeByDate:"+e.getMessage());
		}
	    ss.close();
		return list;
	}
	//所有的充值记录
	public List<Sts_Recharge> findAllRecharge()
	{
		List<Sts_Recharge> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_Recharge.class)
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	
    public  List UserRechagecount(){
   	 
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql="select * from ((select uid,account,sum(dia1)," +
		 		"sum(dia2),sum(dia3),sum(dia4),sum(dia5)," +
		 		"sum(dia6)," +
		 		"sum(money)as numje from sts_recharge group by uid)as b1)" +
		 		"order by b1.numje desc;";
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 return list;
	 }
    //当月
   public  List countRechargeByMonth(String time1){
   	 
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql=
					"select time1,sum(dia1),sum(dia2),sum(dia3)," +
			 		"sum(dia4),sum(dia5),sum(dia6)," +
			 		"sum(money) from " +
			 		"((select uid,account,dia1,dia2,dia3,dia4,dia5,dia6," +
			 		"time1,money from sts_recharge where " +
			 		"time2=?)as b1) group by time1 order by time1 desc;";
		 Query query = s.createSQLQuery(sql);
		 query.setString(0,time1);
		 list=query.list();
		 s.close();
		 return list;
	 }
   //所有月份统计
   public  List countRechargeAllMonth(){
	   	 
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql=
				"select time2,sum(dia1),sum(dia2),sum(dia3)," +
		 		"sum(dia4),sum(dia5),sum(dia6)," +
		 		"sum(money) from " +
		 		" sts_recharge" +
		 		" group by time2 order by time2 desc;";
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 return list;
	 }
   //查询玩家是否有过充值记录
   public int countPlayerCharge(int uid)
   {
	    Session ss = HSF_Play.getSession();
	    int result = 0;
	    try {
	    	Query query =ss.createQuery("select count(*) from Sts_Recharge s where s.uid =:UID");
	    	query.setParameter("UID", uid);
	    	List list = query.list();
	    	result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return result;
	   
   }
	/************************************************
	 *  聊天信息
	 */
	public  List<Sts_Chat> findPlayerChats(int uid,String Accout,String nick,int lowTime,int upTime)
	{
		SimpleExpression expr = null ;
		if(uid != -1)
			expr = Restrictions.eq("uid", uid);
		if(Accout != null)
			expr = Restrictions.eq("account", Accout);
		if(nick != null)
			expr = Restrictions.eq("nick", nick);
		
	    Session ss = HSF_Play.getSession();
	    List<Sts_Chat> list  = null;
	    try {
	    	if((lowTime == 0|| upTime == 0) && expr != null)
	    	{
	    		list = ss.createCriteria(Sts_Chat.class)
	    				.add(expr)
	    				.addOrder(Order.desc("absDay"))
	    				.setMaxResults(300)
	    				.list();
	    	}else if(expr != null)
	    	{
	    		list = ss.createCriteria(Sts_Chat.class)
	    				.add(expr)
	    				.addOrder(Order.desc("absDay"))
	    				.add(Restrictions.between("absDay", lowTime, upTime))
	    				.setMaxResults(300)
	    				.list();
	    	}else 
	    	{
	    		list = ss.createCriteria(Sts_Chat.class)
	    				.addOrder(Order.desc("absDay"))
	    				.add(Restrictions.between("absDay", lowTime, upTime))
	    				.setMaxResults(300)
	    				.list();
	    	}
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	//最新300条聊天记录
	public List<Sts_Chat> find300LeatestChat()
	{
	    Session ss = HSF_Play.getSession();
	    List<Sts_Chat> list = null;
	    try {
			list = ss.createCriteria(Sts_Chat.class)
			.addOrder(Order.desc("absDay"))
			.setMaxResults(300)
			.list();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
	    ss.close();
	    return list;
	}
	/************************************************
	 *  购买道具信息
	 */
	//根据月份,指定日期查询购买记录
	public List<Sts_BuyProp> findBuyPropByDay(String dayStr)
	{
		List<Sts_BuyProp> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_BuyProp.class)
			.add(Restrictions.like("day", dayStr, MatchMode.START))
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	/************************************************
	 *  arpu信息
	 */
	//查询前300条ARPU
	public List<Sts_Arpu> find300Arpu()
	{
		List<Sts_Arpu> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_Arpu.class)
			.addOrder(Order.desc("absDay"))
			.setMaxResults(300)
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	
	//根据月份,指定日期查arpu记录
	public List<Sts_Arpu> findArpuByDay(String dayStr)
	{
		List<Sts_Arpu> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_Arpu.class)
			.add(Restrictions.like("day", dayStr, MatchMode.START))
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	public Sts_Arpu findArpuToday()
	{
		String tday = ServerTimer.getDay();
	    Session ss = HSF_Play.getSession();
		Sts_Arpu arpu = null;
		try {
			List<Sts_Arpu> list = ss.createCriteria(Sts_Arpu.class)
			.add(Restrictions.eq("day", tday))
			.list();
			if(list.size() > 0)
			{
				arpu = list.get(0);
				ss.close();
				return arpu;
			}
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
		ss.close();
		return null;
	}
	
	//按月分组，查询出每个月的arpu
	   //所有月份统计
	   public  List countArpuForMonth(){
		   	 
			 List list=null;
			 Session s=HSF_Play.getSession();
			 String sql=
					"select timeMoth,sum(chargeNum),sum(addCharge),sum(amount) " +
			 		" from " +
			 		" sts_arpu" +
			 		" group by timeMoth order by timeMoth desc;";
			 Query query = s.createSQLQuery(sql);
			 list=query.list();
			 s.close();
			 return list;
		 }

	/************************************************
	 *  禁言 黑名单 列表
	 */
	//获得禁言列表
	public List<Sts_MuteBlacklist> findMuteLit()
	{
		List<Sts_MuteBlacklist> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_MuteBlacklist.class)
			.add(Restrictions.gt("muteDays",0))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	
	//获得黑名单
	public List<Sts_MuteBlacklist> findBlackLit()
	{
		List<Sts_MuteBlacklist> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_MuteBlacklist.class)
			.add(Restrictions.eq("black",true))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	//根据NICK查新记录
	public Sts_MuteBlacklist findMute_Black(String nick)
	{
		Sts_MuteBlacklist muteblack = null;
	    Session ss = HSF_Play.getSession();
	    try {
			List<Sts_MuteBlacklist> list = ss.createCriteria(Sts_MuteBlacklist.class)
			.add(Restrictions.eq("nick", nick))
			.list();
			if(list.size() >0)
				muteblack = list.get(0);
			
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return muteblack;
	}
	/************************************************
	 *  剧情进度统计
	 */
	//根据月份,指定日期查剧情进度记录
	public List<Sts_JuqingDay> findJuqing(String dayStr)
	{
		List<Sts_JuqingDay> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_JuqingDay.class)
			.add(Restrictions.like("day", dayStr, MatchMode.START))
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error("findJuqing:"+e.getMessage());
		}
	    ss.close();
		return list;
	}
	public Sts_JuqingDay findTodayJuqing()
	{
		String nowDay = ServerTimer.getDay();
		Sts_JuqingDay day = null;
		List<Sts_JuqingDay> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_JuqingDay.class)
			.add(Restrictions.eq("time1", nowDay))
			.list();
			if(list.size() > 0)
				day = list.get(0);
		} catch (HibernateException e) {
			Log.error("findJuqing:"+e.getMessage());
		}
	    ss.close();
		return day;
	}
	
	//查询所有剧情进度
	public List<Sts_JuqingDay> findAllJuqing()
	{
		List<Sts_JuqingDay> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_JuqingDay.class)
			.addOrder(Order.desc("absDay"))
			.setMaxResults(300)
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	//查询每月剧情进度统计
	public List findAllJuqing_Month()
	{
		Session ss = HSF_Play.getSession();
		List list  = null;
		 String sql=
					"select time2,sum(juqing1),sum(juqing2),sum(juqing3) from" +
			 		"((select juqing1,juqing2,juqing3,time2 from sts_juqingday)as b1)" +
			 		" group by time2 order by time2 desc;";
		Query query  = ss.createSQLQuery(sql);
		list=query.list();
		ss.close();
		return list;
	}
	
	/************************************************
	 *  金币统计
	 */	
	//根据月份,指定日期查寻金币钻石记录
	public List<Sts_GoldDia> findGDByDate(String dayStr)
	{
		List<Sts_GoldDia> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_GoldDia.class)
			.add(Restrictions.like("day", dayStr, MatchMode.START))
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	//查询所有金币钻石记录
	public List<Sts_GoldDia> findAllGD()
	{
		List<Sts_GoldDia> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_GoldDia.class)
			.addOrder(Order.desc("absDay"))
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	public Sts_GoldDia findGD_today()
	{
		Sts_GoldDia gd = null;
		List<Sts_GoldDia> list = null;
		String nowday = ServerTimer.getDay();
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_GoldDia.class)
			.add(Restrictions.eq("day", nowday))
			.list();
			if(list.size() > 0)
				gd = list.get(0);
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return gd;
	}
	/************************************************
	 *  金币钻石异常统计
	 */	
	public int CountPlayerByGold(int g)
	{
	    Session ss = HSF_Play.getSession();
	    int result = 0;
	    try {
	    	Query query =ss.createQuery("select count(*) from MJ_User s where s.gold >=:GoldNum");
	    	query.setParameter("GoldNum", g);
	    	List list = query.list();
	    	result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return result;
	}
	/************************************************
	 *  金币钻石异常统计
	 */	
	public int CountPlayerByDia(int dia)
	{
	    Session ss = HSF_Play.getSession();
	    int result = 0;
	    try {
	    	Query query =ss.createQuery("select count(*) from MJ_User s where s.dianQuan >=:diaNum");
	    	query.setParameter("diaNum", dia);
	    	List list = query.list();
	    	result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return result;
	}
	/**
	 * 统计道具
	 */
	//单日单个道具购买
	public List countPropBuy_day()
	{
		Session ss = HSF_Play.getSession();
		List list  = null;
		 String sql=
					"select time1,sum(prop1),sum(prop2),sum(prop3),sum(prop4),sum(prop5),sum(prop6),sum(prop7),sum(prop8),sum(prop9),sum(prop10),sum(prop11),sum(prop12),sum(prop13),sum(prop14),sum(prop15),sum(prop16),sum(prop17) from" +
			 		"((select prop1,prop2,prop3,prop4,prop5,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,time1 from sts_buyprop)as b1)" +
			 		" group by time1 order by time1 desc;";
		Query query  = ss.createSQLQuery(sql);
		list=query.list();
		ss.close();
		return list;
	}
	//单日单个道具使用
	public List countPropUse_day(){
		
		Session ss = HSF_Play.getSession();
		List list  = null;
		 String sql=
					"select time1,sum(prop1),sum(prop2),sum(prop3),sum(prop4) from" +
			 		"((select prop1,prop2,prop3,prop4,time1 from sts_useprop)as b1)" +
			 		" group by time1 order by time1 desc;";
		Query query  = ss.createSQLQuery(sql);
		list=query.list();
		ss.close();
		return list;
	}
	//单月单个道具购买
	public List countPropBuy_month()
	{
		Session ss = HSF_Play.getSession();
		List list  = null;
		 String sql=
					"select time2,sum(prop1),sum(prop2),sum(prop3),sum(prop4),sum(prop5),sum(prop6),sum(prop7),sum(prop8),sum(prop9),sum(prop10),sum(prop11),sum(prop12),sum(prop13),sum(prop14),sum(prop15),sum(prop16),sum(prop17) from" +
			 		"((select prop1,prop2,prop3,prop4,prop5,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,time2 from sts_buyprop)as b1)" +
			 		" group by time2 order by time2 desc;";
		Query query  = ss.createSQLQuery(sql);
		list=query.list();
		ss.close();
		return list;
		
	}
	//单月单个道具使用
	public List countPropUse_month()
	{
		Session ss = HSF_Play.getSession();
		List list  = null;
		 String sql=
					"select time2,sum(prop1),sum(prop2),sum(prop3),sum(prop4)from" +
			 		"((select prop1,prop2,prop3,prop4,time2 from sts_useprop)as b1)" +
			 		" group by time2 order by time2 desc;";
		Query query  = ss.createSQLQuery(sql);
		list=query.list();
		ss.close();
		return list;
	}
	//总的统计   道具购买次数 道具使用次数  道具使用率
	/**
	 * 总人数 总头像  总参与对战人数
	 */
	//总人数
	public int findTotalRegPlayer()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	public int findTotalHadRole()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.hadRole =:HADROLE ");
			query.setParameter("HADROLE", true);
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	public int findTotalJoinedBattle()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.joinedNetBattle =:JOINEDBATTLE ");
			query.setParameter("JOINEDBATTLE", true);
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	/**
	 * 服务器几金币钻石统计
	 */
	public List<Sts_GoldDia> findAllGoldCount()
	{
	    List<Sts_GoldDia> list = null;
	    Session s = HSF_Play.getSession();
		Criteria cx=s.createCriteria(Sts_GoldDia.class);
		cx.addOrder(Order.desc("absDay"));
		cx.setMaxResults(300);
		list=cx.list();
		s.close();
	    return list;
	}
	/**
	 * 服务器金币异常检测
	 */
	public int countGold10()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.gold between 100000 and 1000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	public int countGold100()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.gold between 1000000 and 10000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	public int countGold1000()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.gold >= 10000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	
	public int countDia10()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.dianQuan between 100000 and 1000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
    	
	}
	public int countDia100()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.dianQuan between 1000000 and 10000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	public int countDia1000()
	{
		int result = 0;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select count(*) from MJ_User s where s.dianQuan >= 10000000");
			List list = query.list();
			result = ((Long) list.get(0)).intValue();
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
    	return result;
	}
	/**
	 * 查询总的金币钻石数量
	 */
	public Object[] findTotalGoldDia()
	{
		 List list=null;
		 Object[] arr = null;
		 Session s=HSF_Play.getSession();
		 String sql=
				 	"select sum(gold),sum(dianQuan) from mj_user;"; 
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 if(list.size() > 0)
			 arr = (Object[]) list.get(0);
		 return arr;
	}
	public int findTotalDia()
	{
		return 0;
	}
	/**
	 * 金币钻石流动性统计
	 * @return
	 */
	public List<MJUserCharge> find500FlowRec()
	{
		 List<MJUserCharge> list=null;
		 Session ss=HSF_Play.getSession();
		 try {
			list = ss.createCriteria(MJUserCharge.class)
					 .addOrder(Order.desc("time"))
					 .setMaxResults(500)
					 .list();
		} catch (HibernateException e) {
			Log.error("find500FlowRec"+e.getMessage());
		}
		ss.close();
		return list;
	}
	
	public List<MJUserCharge> findPlayerChargeFlow(int uid,String account,String nick)
	{
		List<MJUserCharge> list = null;
	    Session ss = HSF_Play.getSession();
	    Criteria cr =  ss.createCriteria(MJUserCharge.class);
		try {
			if(uid != -1)
			{
				list = cr.add(Restrictions.eq("uid", uid))
						.addOrder(Order.desc("time"))
				.list();
			}else if(account != null)
			{
				list = cr.add(Restrictions.eq("name", account))
							.addOrder(Order.desc("time"))
							.list();
			}else if(nick != null)
			{
				list = cr.add(Restrictions.eq("nickName", nick))
							.addOrder(Order.desc("time"))
							.list();
			}
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
		ss.close();
		return list;
	}
	/**
	 * 设备统计
	 */
	/**
	 * 查找今日返回率对象
	 * @return
	 */
	public Sts_MJDevice findTodayDevice()
	{
		Sts_MJDevice device = null;
		String now  = ServerTimer.getDay();
		Session ss = HSF_Play.getSession();
		try {
			List list =  ss.createCriteria(Sts_MJDevice.class)
			.add(Restrictions.eq("time1", now))
			.list();
			ss.close();
			if(list.size() > 0)
				device  = (Sts_MJDevice) list.get(0);
			else
			{
				device=new Sts_MJDevice();
				device.setTime1(ServerTimer.getDay());
				device.setTime2(ServerTimer.getMonth());
				device.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
				 this.saveSts_Object(device);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return device;
	}
	
	//查询300条设备统计
	public List<Sts_MJDevice> findAllSts_Device()
	{
		List<Sts_MJDevice> list = null;
	    Session ss = HSF_Play.getSession();
	    try {
			list = ss.createCriteria(Sts_MJDevice.class)
			.addOrder(Order.desc("absDay"))
			.setMaxResults(300)
			.list();
		} catch (HibernateException e) {
			Log.error(e.getStackTrace());
		}
	    ss.close();
		return list;
	}
	
	public Sts_UserCup findUserCup(int uid,String day)
	{
		Sts_UserCup cup = null;
	    Session ss = HSF_Play.getSession();
	    try {
			List list = ss.createCriteria(Sts_UserCup.class)
					.add(Restrictions.eq("uid", uid))
					.add(Restrictions.eq("day", day))
			.list();
			if(list.size() > 0)
				cup  = (Sts_UserCup) list.get(0);
			
		} catch (HibernateException e) {
			logger.info("error:  findUserCup "+ e.getMessage());
		}
	    ss.close();
		return cup;
	}
	
	
	//查询当天日常任务完成情况
	public Sts_DayTaskCompletion findTodayDayTask()
	{
		Sts_DayTaskCompletion dayTask = null;
	    Session ss = HSF_Play.getSession();
	    try {
	    	List<Sts_DayTaskCompletion> list = ss.createCriteria(Sts_DayTaskCompletion.class)
	    		 .add(Restrictions.eq("timeDay",ServerTimer.getDay()))
			.list();
	    	if(list.size()>0)
	    	{
	    		dayTask = list.get(0);
	    	}else
	    	{
	    		dayTask = new Sts_DayTaskCompletion();
	    		dayTask.setTimeDay(ServerTimer.getDay());
	    		dayTask.setTimeMonth(ServerTimer.getMonth());
	    		dayTask.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
	    	}
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
	    ss.close();
		return dayTask;
	}
	
	/**
	 * 查询最新的100条每日任务完成记录
	 */
	public List<Sts_DayTaskCompletion> findTop100DayTask()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_DayTaskCompletion> list = null;
		try {
			list =	ss.createCriteria(Sts_DayTaskCompletion.class)
				.addOrder(Order.desc("absDay"))
				.setMaxResults(100)
				.setFirstResult(0)
				.list();
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
		ss.close();
		return list;
	}
	//查询当永久任务完成情况
	public Sts_LongTaskCompletion findLongTask()
	{
		Sts_LongTaskCompletion longTask = null;
	    Session ss = HSF_Play.getSession();
	    try {
	    	List<Sts_LongTaskCompletion> list = ss.createCriteria(Sts_LongTaskCompletion.class)
			.list();
	    	if(list.size()>0)
	    	{
	    		longTask = list.get(0);
	    	}else
	    	{
	    		longTask = new Sts_LongTaskCompletion();
	    	}
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
	    ss.close();
		return longTask;
	}
	/*
	 * 查询当天的dj_count;
	 */
	public Sts_DJ_Count findDJCount()
	{
		Sts_DJ_Count count = null;
	    Session ss = HSF_Play.getSession();
	    try {
	    	List<Sts_DJ_Count> list = ss.createCriteria(Sts_DJ_Count.class)
	    			.add(Restrictions.eq("timeDay", ServerTimer.getDay()))
			.list();
	    	if(list.size()>0)
	    	{
	    		count = list.get(0);
	    	}else
	    	{
	    		count = new Sts_DJ_Count();
	    		count.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
	    		count.setTimeDay(ServerTimer.getDay());
	    		count.setTimeMonth(ServerTimer.getMonth());
	    	}
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
	    ss.close();
		return count;
	}
	/**
	 * 分组查询出每天， 每种人机的开始，结束的场数
	 */
	public List<Integer> findDJDataGroup(int danjiId)
	{
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql= "select timeDay,danjiId,b1 from( select timeDay,danjiId,count(*) as b1 from sts_userdanji group by timeDay,danjiId ) as b2 where danjiId = 2; ";

		 Query query = s.createSQLQuery(sql);
		 query.setInteger(0,danjiId);
		 list=query.list();
		 s.close();
		 return list;
	}
	/**
	 * 查询前100天人机次数统计
	 */
	public List<Sts_DJ_Count> findTop100DJCount()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_DJ_Count> list = null;
		try {
			list =	ss.createCriteria(Sts_DJ_Count.class)
				.addOrder(Order.desc("absDay"))
				.setMaxResults(100)
				.setFirstResult(0)
				.list();
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
		ss.close();
		return list;
	}
	/**
	 * 查询当天的单机开关统计
	 */
	public Sts_UserDanjiCount findTodyDanjiCount()
	{
		
		Sts_UserDanjiCount dayTask = null;
	    Session ss = HSF_Play.getSession();
	    try {
	    	List<Sts_UserDanjiCount> list = ss.createCriteria(Sts_UserDanjiCount.class)
	    		 .add(Restrictions.eq("timeDay",ServerTimer.getDay()))
			.list();
	    	if(list.size()>0)
	    	{
	    		dayTask = list.get(0);
	    	}else
	    	{
	    		dayTask = new Sts_UserDanjiCount();
	    		dayTask.setTimeDay(ServerTimer.getDay());
	    		dayTask.setTimeMonth(ServerTimer.getMonth());
	    		dayTask.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
	    	}
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
	    ss.close();
		return dayTask;
	}
	
	/**
	 * 查询前100单机每日统计
	 */
	public List<Sts_UserDanjiCount> findTop100DJDay()
	{
		Session ss = HSF_Play.getSession();
		List<Sts_UserDanjiCount> list = null;
		try {
			list =	ss.createCriteria(Sts_UserDanjiCount.class)
				.addOrder(Order.desc("absDay"))
				.setMaxResults(100)
				.setFirstResult(0)
				.list();
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
		ss.close();
		return list;
	}
	/**
	 * 查询所有月份的人机统计
	 */
	public List findMothDanji()
	{
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql=
				"select timeMonth,sum(dan1_open),sum(dan1_close),sum(dan2_open)," +
		 		"sum(dan2_close),sum(dan3_open),sum(dan3_close)," +
		 		"sum(dan4_open),sum(dan4_close),sum(dan5_open)," +		
		 		"sum(dan5_close),sum(dan6_open),sum(dan6_close) " +		
		 		" from " +
		 		" sts_danjicount" +
		 		" group by timeMonth order by timeMonth desc;";
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 return list;
		
	}
	/**
	 * 查询杯赛记录
	 * 根据杯赛ID查询 求和
	 */
	public List findStsCupById(int cupId)
	{
		 List list=null;
		 Session s=HSF_Play.getSession();
//		 String sql= " select timeDay,count(*)," +
//		 		"(select count(*) from sts_usercup where cupId =? and endFlag = 1)," +
//		 		"(select count(*) from sts_usercup where cupId =? and endFlag = -1)," +
//		 		"sum(consumeProp1),sum(consumeProp2) " +
//		 		"from ( select * from sts_usercup where cupId =?) as t1 group by cupId;";
		 
		 
		 String sql = "select timeDay,count(*),sum(endFlag = 1) ,sum(endFlag = 2) ,sum(consumeProp1),sum(consumeProp2)" +
		 		" from ( select * from sts_usercup where cupId =?) as t1 group by timeDay;";
		 
		 Query query = s.createSQLQuery(sql);
		 query.setInteger(0,cupId);
		 list=query.list();
		 s.close();
		 return list;
	}
	
	
	/**
	 * 插叙当天道具奖励
	 * @return
	 */
	public Sts_RewardProp findTodayRewardProp(MJ_User user)
	{
		Sts_RewardProp prop = null;
	    Session ss = HSF_Play.getSession();
	    try {
	    	List<Sts_RewardProp> list = ss.createCriteria(Sts_RewardProp.class)
	    		 .add(Restrictions.eq("time1",ServerTimer.getDay()))
			.list();
	    	if(list.size()>0)
	    	{
	    		 prop = list.get(0);
	    	}else
	    	{
	    		 prop = new Sts_RewardProp();
	    		 prop.setTime1(ServerTimer.getDay());
	    		 prop.setTime2(ServerTimer.getMonth());
	    		 prop.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
	    		 prop.setDay(ServerTimer.getNowString());
	    		 prop.setUid(user.getUid());
	    		 prop.setAccount(user.getName());
	    		 prop.setNick(user.getNick());
	    	}
		} catch (HibernateException e) {
			logger.info(e.getMessage());
		}
	    ss.close();
		return prop;
	}
	/**
	 * 查询奖励道具记录 日
	 */
	public List findDaysRewardProp()
	{
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql= "select time1,sum(prop1),sum(prop2),sum(prop3),sum(prop4) from sts_rewardprop group by time1  order by time1 desc; ";
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 return list;
	}
	/**
	 * 查询奖励道具记录 月
	 */
	public List findMonthRewardProp()
	{
		 List list=null;
		 Session s=HSF_Play.getSession();
		 String sql= "select time2,sum(prop1),sum(prop2),sum(prop3),sum(prop4) from sts_rewardprop group by time2  order by time2 desc; ";
		 Query query = s.createSQLQuery(sql);
		 list=query.list();
		 s.close();
		 return list;
	}
	
	/**
	 * 根据订单号查询出360订单
	 */
	public Sts_360Order find360OrderById(String app_order_id)
	{
		if(app_order_id == null) return null;
		Sts_360Order order = null;
		Session ss = HSF_Play.getSession();
		try {
			List<Sts_360Order> list =  ss.createCriteria(Sts_360Order.class)
			.add(Restrictions.eq("app_order_id", app_order_id))
			.list();
			
			if(list.size() > 0)
				order  = list.get(0);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		ss.close();
		return order;
	}
	/**
	 * 查询玩家所有未前台确认的订单 
	 */
	public List<Sts_360Order>  findUserUnVerifiedOrder(int uid)
	{
		Session ss = HSF_Play.getSession();
		List<Sts_360Order> list =  ss.createCriteria(Sts_360Order.class)
		.add(Restrictions.eq("uid", uid))
		.add(Restrictions.eq("verify_client", false))
		.add(Restrictions.eq("verify_360", "verified"))
		.list();
		
		logger.info("findUserUnVerifiedOrder: list size:" +list.size());
		return list;
	}
}
