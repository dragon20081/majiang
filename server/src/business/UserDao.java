package business;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import server.command.CommandMap;

import common.Log;

import business.entity.MJ_Cup;
import business.entity.MJ_DanJil;
import business.entity.MJ_DataFan;
import business.entity.MJ_DayTask;
import business.entity.MJ_Device;
import business.entity.MJ_PayInfo;
import business.entity.MJ_Resource;
import business.entity.MJ_Role;
import business.entity.MJ_Skill;
import business.entity.MJ_TmpResource;
import business.entity.MJ_User;
import business.entity.M_Prop;
import business.entity.M_Shop;

public class UserDao {

	private static final Logger logger = Logger.getLogger(UserDao.class.getName());
	@SuppressWarnings("unchecked")
	public  MJ_User findPlayerByName(String name,Session ss)
	{
		MJ_User player   = null;
		try {
			List<MJ_User> list  =  ss.createCriteria(MJ_User.class)
			.add(Restrictions.eq("name", name))
			.list();
			if(list.size()!= 0) player  =  (MJ_User)list.get(0);
		} catch (HibernateException e) {
					logger.info(e.getMessage());
			logger.info(e.getMessage());
		}
		return player;
	}
	@SuppressWarnings("unchecked")
	public  MJ_User findPlayerByEmail(String email,Session ss)
	{
		MJ_User player   = null;
		try {
			List<MJ_User> list  =  ss.createCriteria(MJ_User.class)
			.add(Restrictions.eq("email", email))
			.list();
			if(list.size()!= 0) player  =  (MJ_User)list.get(0);
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return player;
	}
	
	@SuppressWarnings("unchecked")
	public  List<M_Shop> findShop(Session ss)
	{
		List<M_Shop> list = null;
		try {
			list  =  ss.createCriteria(M_Shop.class)
			.addOrder(Order.asc("proId"))
			.list();
			
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	public List<MJ_Resource> findResource(Session ss)
	{
		List<MJ_Resource> list = null;
		try {
			list  =  ss.createCriteria(MJ_Resource.class)
			.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public List<MJ_TmpResource> findTmpResource(Session ss)
	{
		List<MJ_TmpResource> list = null;
		try {
			list  =  ss.createCriteria(MJ_TmpResource.class)
			.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public List<MJ_PayInfo> findPayInfo(Session ss)
	{
		List<MJ_PayInfo> list = null;
		try {
			list  =  ss.createCriteria(MJ_PayInfo.class)
			.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public List<MJ_DayTask> findTasks(Session ss)
	{
		List<MJ_DayTask> list = null;
		try {
			list  =  ss.createCriteria(MJ_DayTask.class)
					.addOrder(Order.asc("taskId"))
					.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	public List<MJ_Skill> findSkills(Session ss)
	{
		List<MJ_Skill> list = null;
		try {
			list  =  ss.createCriteria(MJ_Skill.class)
					.addOrder(Order.asc("id"))
					.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	public List<MJ_DataFan> findFans(Session ss)
	{
		List<MJ_DataFan> list = null;
		try {
			list  =  ss.createCriteria(MJ_DataFan.class)
					.addOrder(Order.asc("id"))
					.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public List<MJ_DanJil> findDanji(Session ss)
	{
		List<MJ_DanJil> list = null;
		try {
			list  =  ss.createCriteria(MJ_DanJil.class)
					.addOrder(Order.asc("id"))
					.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public List<MJ_Cup> findCup(Session ss)
	{
		List<MJ_Cup> list = null;
		try {
			list  =  ss.createCriteria(MJ_Cup.class)
					.addOrder(Order.asc("id"))
					.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public  MJ_User findPlayerByUid(int uid)
	{
		Session ss = HSF_Play.getSession();
		MJ_User player   = null;
		try {
			List<MJ_User> list  =  ss.createCriteria(MJ_User.class)
			.add(Restrictions.eq("uid", uid))
			.list();
			if(list.size()!= 0) player  =  (MJ_User)list.get(0);
		} catch (HibernateException e) {
					logger.info(e.getMessage());
			Log.error(e.getMessage());
		}
		return player;
	}
	@SuppressWarnings("unchecked")
	public  MJ_User findPlayerByNick(String nick,Session ss)
	{
		MJ_User player   = null;
		try {
			List<MJ_User> list  =  ss.createCriteria(MJ_User.class)
			.add(Restrictions.eq("nick", nick))
			.list();
			if(list.size()!= 0) player  =  (MJ_User)list.get(0);
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return player;
	}
	public List findPlayersLikeNick(String nick,Session ss)
	{
		List<MJ_User> list  = null;
		try {
			 list  =  ss.createCriteria(MJ_User.class)
			.add(Restrictions.like("nick","%"+nick+"%"))
			.list();
		} catch (HibernateException e) {
					logger.info(e.getMessage());
		}
		return list;
	}
	
	public void savePlayer(MJ_User p,Session ss)
	{
		ss.saveOrUpdate(p);
	}
	public void saveDataObject(Object obj,Session ss)
	{
		ss.saveOrUpdate(obj);
	}
	
	public void saveObject(Object obj)
	{
	    Session ss = HSF_Play.getSession();
		 Transaction ts=ss.beginTransaction();
	    try {
			ss.saveOrUpdate(obj);
			ts.commit();
		} catch (Exception e) {
			Log.error(e.getStackTrace());
			ts.rollback();
		}
		ss.close();
	}
	public void saveProp(M_Prop prop,Session ss)
	{
		ss.saveOrUpdate(prop);
	}
	public void saveRole(MJ_Role role,Session ss)
	{
		ss.saveOrUpdate(role);
	}
	//修改密码，返回false表示用户名为空，返回true表示修改成功
	/**
	 *   7 未找到这个用户6 修改成功  5 密码错误
	 */
	@SuppressWarnings("unchecked")
	public int changePassword(String account, String oldPwd, String newPwd){
		int flag = 0;
		Session s = HSF_Play.getSession();
		s.beginTransaction();
		List<MJ_User> list = s.createCriteria(MJ_User.class).
			add(Restrictions.eq("name", account)).list();
		if(list.size() == 0)
			list = s.createCriteria(MJ_User.class).
					add(Restrictions.eq("email", account)).list();
		
		if(list.size() == 0){
			flag = 7;
		}else{
			MJ_User user = list.get(0);
			if(user.getPwd().equals(oldPwd)){
				user.setPwd(newPwd);
				flag = 6;
			}else{
				flag = 5;
			}
		}
		s.getTransaction().commit();
		s.close();
		return flag;
	}
	////////////////////////////////////////////////////////
	/**
	 *  找回密码， 根据 账号,邮箱 查询出用户密码， 通过邮件将密码发送至注册邮箱
	 *  8 账号或邮箱错误  9 成功
	 */
	@SuppressWarnings("unchecked")
	public int findPassword(String proper){
		int flag = 0;
		Session s = HSF_Play.getSession();
		s.beginTransaction();
		List<MJ_User> list = s.createCriteria(MJ_User.class).
					add(Restrictions.eq("name", proper)).list();
		if(list.size() == 0)
			list = s.createCriteria(MJ_User.class).
					add(Restrictions.eq("email", proper)).list();
			if(list.size() == 0){
				flag = 8;
			}else{
				MJ_User user = list.get(0);
				SendEmailTimer timer  = new SendEmailTimer();
				timer.setPwd(user.getPwd());
				timer.setEmail(user.getEmail());
				timer.start();
				flag = 9;
			}

		s.getTransaction().commit();
		s.close();
		return flag;
	}
	/**
	 * 根据昵称查询玩家战绩
	 */
	public List<Integer> findZhanji(String nick)
	{
		List<Integer> list = null;
	    Session ss = HSF_Play.getSession();
    	try {
			Query query =ss.createQuery("select panTotal,panHu,panHu10,panHu20,panHu30 from MJ_User s where s.nick =:NICK");
			query.setParameter("NICK", nick);
			List tmp = query.list();
			Object[] arr = null;
			if(tmp.size() > 0)
				arr = (Object[]) tmp.get(0);
			if(arr != null)
			{
				list = new ArrayList<Integer>();
				for(int i = 0 ; i < arr.length ; i++)
				{
						int value = ((Integer)arr[i]);
						list.add(value);
				}
			}
		} catch (HibernateException e) {
			Log.error(e.getMessage());
		}
    	ss.close();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public  MJ_Device findDevice(int deviceId)
	{
		Session ss = HSF_Play.getSession();
		MJ_Device device   = null;
		try {
			List<MJ_Device> list  =  ss.createCriteria(MJ_Device.class)
			.add(Restrictions.eq("deviceId", deviceId))
			.list();
			if(list.size()!= 0) device  =  (MJ_Device)list.get(0);
		} catch (HibernateException e) {
					logger.info(e.getMessage());
			Log.error(e.getMessage());
		}
		return device;
	}
	
}
