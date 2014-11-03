package business;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import server.mj.Global;
import server.mj.ServerTimer;
import business.conut.Sts_Urr;
import business.conut.Sts_UserOperate;
import business.entity.MJ_Cup;
import business.entity.MJ_DanJil;
import business.entity.MJ_DataFan;
import business.entity.MJ_DayTask;
import business.entity.MJ_PayInfo;
import business.entity.MJ_Resource;
import business.entity.MJ_Role;
import business.entity.MJ_Skill;
import business.entity.MJ_TmpResource;
import business.entity.MJ_User;
import business.entity.M_Prop;
import business.entity.M_Shop;

public class Business {

	public static  int MAX_REG   =  1000000;
	public static  int MAX_LOGIN =  2000;
	private MJ_User player   =  null;
	UserDao dao = new UserDao();
	/**
	 * 
	 * @param name  用户名
	 * @return   1 登陆成功  2 超过最大注册人数  3 超过最大登陆人数      0 发生异常错误    7密码错误  8用户名可用 9 没有这个用户
	 * 
	 * type   1 查询是否有这个用户   2.注册并登陆
	 */
	public int login_auto(String name,String pwd,int type,String pingtai)
	{
		type = 2;
		String curTime = ServerTimer.getNowString();
		try {
			
//			List<M_Shop> shops = findShopItem();
			player  =  this.findPlayerByName(name);
			if(player  == null)
			{
				if(!pingtai.equals("360"))
				{
					if(type == 1)return 8;
					if(type == 2)return 9;
				}
				if(this.getAllUserCount() < Business.MAX_REG){
					player = new MJ_User();
					player.setName(name);
					player.setPwd(pwd);
					player.setRegTime(curTime);
					player.setLastLogin(curTime);
//					player.setShopItems(shops);
					this.initPlayer(player);
					this.savePlayer(player);
					//新注册统计
					CountDao cdao = new CountDao();
					Sts_Urr urr = cdao.findTodayRR();
					urr.setNewReg(urr.getNewReg() + 1);
					urr.setNewDLogin(urr.getNewDLogin() + 1);
					urr.setdLogin(urr.getdLogin() + 1);
					
					urr.setOncep(urr.getOncep() + 1);
					cdao.saveUserRR(urr);
					
//					rate_of_return1(player);
				}else{
					return 2;
				}
				if(Global.players.size() < Business.MAX_LOGIN){
					return 1;
				}else{
					return 3;
				}
				
			}else{
				
				if(!player.getPwd().equals(pwd)) 
				{
					player = null;
					return 7;  //密码错误
				}
				//标记是否是今天首次登陆
				String now = ServerTimer.getDay();
				String lastLogin = "";
				int a = player.getLastLogin().length();
				if(player.getLastLogin().length()>=10)lastLogin = player.getLastLogin().substring(0, 10);
				if(!now.equals(lastLogin)) 	
				{
					player.firstLogin_today = true;
					player.setOnLineTime(0);
				}
				
				rate_of_return1(player);
				
				player.setLastLogin(curTime);
				if(player.getProps() == null)
				{
					M_Prop p = new M_Prop(); 
					p.setUser(player);
					player.setProps(p);
				}
				this.savePlayer(player);
				if(Global.players.size() < Business.MAX_LOGIN){
					return 1;
				}else{
					return 3;
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean login_360(String name)
	{
		String curTime = ServerTimer.getNowString();
		try {
			
			player  =  this.findPlayerByName(name);
			if(player  == null)
			{
					player = new MJ_User();
					player.setName(name);
					player.setPwd("");
					player.setRegTime(curTime);
					player.setLastLogin(curTime);
					this.initPlayer(player);
					this.savePlayer(player);
					//新注册统计
					CountDao cdao = new CountDao();
					Sts_Urr urr = cdao.findTodayRR();
					urr.setNewReg(urr.getNewReg() + 1);
					urr.setNewDLogin(urr.getNewDLogin() + 1);
					urr.setdLogin(urr.getdLogin() + 1);
					urr.setOncep(urr.getOncep() + 1);
					cdao.saveUserRR(urr);
				
			}else{
				String now = ServerTimer.getDay();
				String lastLogin = "";
				if(player.getLastLogin().length()>=10)lastLogin = player.getLastLogin().substring(0, 10);
				if(!now.equals(lastLogin)) 	
				{
					player.firstLogin_today = true;
					player.setOnLineTime(0);
				}
				rate_of_return1(player);
				
				player.setLastLogin(curTime);
				if(player.getProps() == null)
				{
					M_Prop p = new M_Prop(); 
					p.setUser(player);
					player.setProps(p);
				}
				this.savePlayer(player);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * @param name
	 * @param pass 
	 * @return  4 密码错误  3登陆人数超上限  或者用户名不存在  0 错误  1 登陆成功
	 */
	public int login_normal(String name, String pass){

		String curTime = ServerTimer.getNowString();
		try {
			player  =  this.findPlayerByName(name);
			if(player  == null) return 3;
				if(!player.getPwd().equals(pass))		return 4;
				player.setLastLogin(curTime);
//				List<M_Shop> shops = findShopItem();
//				player.setShopItems(shops);
				this.savePlayer(player);
				System.out.println(Global.players.size());
				if(Global.players.size() < Business.MAX_LOGIN){
					return  1;
				}else{
					return  3;
				}
		} catch (HibernateException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void saveUserOperate(String operateStr)
	{
		if(this.player == null)return;
		//保存日志
		Sts_UserOperate operate = new Sts_UserOperate();
		operate.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		operate.setDay(ServerTimer.getNowString());
		operate.setUid(this.player.getUid());
		operate.setOperate(operateStr);
		this.saveDataObject(operate);
		
	}
	
	/**
	 * 机器人登陆
	 * @param name
	 * @return
	 */
	public int robotLogin(String name)
	{
//		int curTime = ServerTimer.getInstance().days;
//		try {
//			
//			player  =  this.findPlayerByName(name);
//			if(player  == null)
//			{
//				player = new MJ_User();
//				player.setName(name);
//				player.setRegTime(curTime);
//				this.initRobot(player);
//				this.savePlayer(player);
//			}else{
//				player.setLastLogin(curTime);
//				this.savePlayer(player);
//			}
//		} catch (HibernateException e) {
//			e.printStackTrace();
//		}
		return 0;
	}
	/**
	 * 根据昵称查询玩家战绩
	 */
	public List<Integer> queryZhanJi(String nick)
	{
		List<Integer> list  = dao.findZhanji(nick);
		return list;
	}
	
	
	/**
	 * @param name
	 * @param pass
	 * @return    0  发生错误   1 成功   2 超过最大注册人数  3.账号已存在    4邮箱已存在
	 */
	public int register(String name, String pwd,String email)
	{
		//格式判断， 验证 3个字符串的格式
		MJ_User player   =  null;
		String curTime = ServerTimer.getNowString();
		try {
			player  =  findPlayerByName(name);
			if(player != null)	return 3;
			if(!email.equals(""))
			{
				player  = findPlayerByEmai(email);
				if(player != null)	return 4;
			}
				player = new MJ_User();
				player.setName(name);
				player.setPwd(pwd);
				player.setRegTime(curTime);
		//			player.setShopItems(shops);
				this.initPlayer(player);
				this.savePlayer(player);
				//新注册统计
				CountDao cdao = new CountDao();
				Sts_Urr urr = cdao.findTodayRR();
				urr.setNewReg(urr.getNewReg() + 1);
//				urr.setOncep(urr.getOncep() + 1);
				cdao.saveUserRR(urr);
				
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	/**
	 * 找回密码， 根据用户给定的邮箱或者账户名，讲密码发送至注册邮箱
	 * @param args  邮箱或账号
	 */
	public int findPassword(String args)
	{
		int flag  = this.dao.findPassword(args);
		return flag;
	}
	/**
	 *  根据账号密码修改原来的密码
	 * @param account  账号
	 * @param prePwd   原密码
	 * @param newPwd   新密码
	 * @return
	 */
	public int changePassword(String account, String prePwd,String newPwd)
	{
		int flag  = this.dao.changePassword(account, prePwd, newPwd);
		return flag;
	}
	public MJ_User  findPlayerByName(String name)
	{
		MJ_User user; 
		Session ss  = HSF_Play.getSession();
		user  = dao.findPlayerByName(name, ss);
		ss.close();
		return user;
	}
	public MJ_User findPlayerByUid(int uid)
	{
		MJ_User user; 
		user  = dao.findPlayerByUid(uid);
		return user;
	}
	public MJ_User  findPlayerByNick(String nick)
	{
		MJ_User user; 
		Session ss  = HSF_Play.getSession();
		user  = dao.findPlayerByNick(nick, ss);
		ss.close();
		return user;
	}
	public MJ_User  findPlayerByEmai(String email)
	{
		MJ_User user; 
		Session ss  = HSF_Play.getSession();
		user  = dao.findPlayerByEmail(email, ss);
		ss.close();
		return user;
	}
	public List  findPlayerLikeNick(String nick)
	{
		List<MJ_User> list  = null;
		Session ss  = HSF_Play.getSession();
		list  = dao.findPlayersLikeNick(nick, ss);
		ss.close();
		return list;
	}
	public boolean savePlayer(MJ_User user)
	{
		boolean flag  = false;
		Session ss  =  HSF_Play.getSession();
		ss.beginTransaction();
		try {
			dao.savePlayer(user, ss);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		ss.getTransaction().commit();
		ss.close();
		flag = true;
		return flag;
	}
	public boolean saveDataObject(Object obj)
	{
		boolean flag  = false;
		Session ss  =  HSF_Play.getSession();
		ss.beginTransaction();
		try {
			dao.saveDataObject(obj, ss);
			ss.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			ss.getTransaction().rollback();
		}
		ss.close();
		return flag;
	}
	public boolean saveRole(MJ_Role role)
	{
		boolean flag  = false;
		Session ss  =  HSF_Play.getSession();
		ss.beginTransaction();
		try {
			dao.saveRole(role, ss);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		ss.getTransaction().commit();
		ss.close();
		flag = true;
		return flag;
	}
	public boolean saveProp(M_Prop prop)
	{
		boolean flag  = false;
		Session ss  =  HSF_Play.getSession();
		ss.beginTransaction();
		try {
			dao.saveProp(prop, ss);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		ss.getTransaction().commit();
		ss.close();
		flag = true;
		return flag;
	}
	
	/**
	 * 商店数据查询
	 * @return
	 */
	public List<M_Shop> findShopItem()
	{
		List<M_Shop> list = new ArrayList<M_Shop>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findShop(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	/**
	 * 资源数据查询
	 * @return
	 */
	public List<MJ_Resource> findResourceItem()
	{
		List<MJ_Resource> list = new ArrayList<MJ_Resource>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findResource(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	/**
	 * 临时资源数据查询
	 * @return
	 */
	public List<MJ_TmpResource> findTmpResourceItem()
	{
		List<MJ_TmpResource> list = new ArrayList<MJ_TmpResource>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findTmpResource(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	
	/**
	 * 查询充值数据
	 */
	public List<MJ_PayInfo> findPayItem()
	{
		List<MJ_PayInfo> list = new ArrayList<MJ_PayInfo>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findPayInfo(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	/**
	 * 查询任务列表
	 */
	public List<MJ_DayTask> findTaskList()
	{
		List<MJ_DayTask> list = new ArrayList<MJ_DayTask>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findTasks(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	/**
	 * 查询技能数据列表
	 */
	public List<MJ_Skill> findSkillList()
	{
		List<MJ_Skill> list = new ArrayList<MJ_Skill>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findSkills(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	
	/**
	 * 查询番数据
	 */
	public List<MJ_DataFan> findFanList()
	{
		List<MJ_DataFan> list = new ArrayList<MJ_DataFan>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findFans(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	
	public List<MJ_DanJil> findDanjiList()
	{
		List<MJ_DanJil> list = new ArrayList<MJ_DanJil>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findDanji(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	
	public List<MJ_Cup> findCupList()
	{
		List<MJ_Cup> list = new ArrayList<MJ_Cup>();
		Session ss  = HSF_Play.getSession();
		try {
			list  = dao.findCup(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss.close();
		return list;
	}
	
	public int getAllUserCount()
	{
		return 0;
	}
	/**
	 * @param user  ·初始化一个新的user
	 */
	public void initPlayer(MJ_User user)
	{
		user.setJuqing_1(1);
		user.setRoles(new HashSet<MJ_Role>());
//		MJ_Role role = new MJ_Role();
//		role.setRoleId(2001);
//		role.setUser(user);
//		user.getRoles().add(role);

		M_Prop prop = new M_Prop();
		prop.setPro2Num(1);
		prop.setPro1Num(2);
		user.setProps(prop);
		prop.setUser(user);
	}
	
	public void initRobot(MJ_User user)
	{
		user.setNick( user.getName());
		user.setRobot(true);
	}
	public MJ_User getPlayer() {
		return player;
	}
	public void setPlayer(MJ_User player) {
		this.player = player;
	}
	/////////////////////////////////////////////////////////
	//*********************数据统计*************************
	public  void rate_of_return1(MJ_User user){
		//获取系统当前时间
    	String currentTime = ServerTimer.getDay();
    	String lastLogin  =  user.getLastLogin().split(" ")[0];
    	String  regTime = user.getRegTime().split(" ")[0];
    	Calendar regC = ServerTimer.getCalendarFromString(regTime);
    	if(!currentTime .equals(lastLogin)){ //今天第一次登陆
    		CountDao cDao = new CountDao();
    		Sts_Urr todayRR  = null;
    		todayRR=cDao.findTodayRR();
    		//新增独立用户数
        	if( lastLogin.equals(""))
        	{
        		todayRR.setOncep(todayRR.getOncep() + 1);
        		todayRR.setNewDLogin(todayRR.getNewDLogin()+1);
        	}
    		//独立登录人数
        	todayRR.setdLogin(todayRR.getdLogin()+1);
    		//1到30天返回数...
    	    int dayts= ServerTimer.distFromNowDay(regC);
    	    
    	    
    	    Sts_Urr regRR=cDao.findByRegDay(user.getRegTime());
    	    if(dayts<=30&&dayts>=1){
    	    	
		    	    	Method[] method=todayRR.getClass().getDeclaredMethods();
		    	    	String methodname="setDay"+String.valueOf(dayts);
		    	    	String getname="getDay"+String.valueOf(dayts);
		    	    	Method  setmMethod=null;
		    	    	Method  getmMethod=null;
		    	    	for(int i=0;i<method.length;i++){
		    	    		if(method[i].getName().equals(methodname)||
		    	    				method[i].getName().equals(getname)){
		    	    			 if(method[i].getName().substring(0,1).equals("s")){
		    	    				 setmMethod=method[i];
		    	    			 }else{
		    	    				 getmMethod=method[i];
		    	    			 }
		    	    		}
		    	    	}
		    	    int dayx;
					try {
						dayx = (Integer) getmMethod.invoke(todayRR);
						setmMethod.invoke(todayRR,dayx+1); 
					} catch(Exception e){
					}
			     //一次性用户   once p 是在哪里设值
			     if(regRR!=null&&((user.getRegTime().split(" ")[0].equals(lastLogin)||lastLogin.equals("")))){
			    	 regRR.setOncep(regRR.getOncep() -1);
			     }    
    	    }
    	    if(dayts>30){
    	    	todayRR.setEarlier(todayRR.getEarlier()+ 1);
    		     if(regRR!=null&&((user.getRegTime().split(" ")[0].equals(lastLogin)||lastLogin.equals("")))){
			    	 regRR.setOncep(regRR.getOncep() -1);
    	    	}
    	    }
    	    if(regRR!=null){
    	    	cDao.saveUserRR(regRR);
    	    }
    	    cDao.saveUserRR(todayRR);
    	}
	}
	
}
