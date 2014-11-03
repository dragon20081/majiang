package server.command.cmd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;
import common.MyArrays;

import server.command.CMD;
import server.command.GlobalData;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.CheckTask;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.Business;
import business.CountDao;
import business.conut.Sts_JuqingDay;
import business.entity.MJ_Role;
import business.entity.MJ_User;
import business.entity.M_Prop;

/**
 *  修改剧情进度
 * @author xue
 */
public class CCMD11102 extends  CMD{
	
	public CountDao cdao;
	
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
		List<Object> list = this.getValues();
		for(int i  = 0; i  < list.size();i++)
		{
			Integer d  =  (Integer)(list.get(i));
			int num  = d.intValue();
			boolean b  =  modePlayerData(num,list.get(i+1));
			SCMD11102 scmd1 = new SCMD11102();
			if(!b)
			{
				scmd1.result  = SCMD11102.FAIL;
				scmd1.id = num;
				this.player.send(11102, scmd1.getBytes());
				return;
			}else
			{
				scmd1.result  = SCMD11102.SUCCESS;
				scmd1.id = num;
				this.player.send(11102, scmd1.getBytes());
			}
			i++;
		}
//		MJ_User user = this.player.getBusiness().getPlayer();
//		boolean b =  this.player.getBusiness().savePlayer(user);
//		SCMD11102 scmd2 = new SCMD11102();
//		scmd2.result  = SCMD11102.SUCCESS;
//		scmd2.id = num;
//		if(!b)	scmd2.result  = SCMD11102.FAIL;
//		this.player.send(11102, scmd2.getBytes());
	}
	
	private int loseR  = -1;
	public boolean modePlayerData(int type,Object obj)
	{
		boolean flag = true;
		List<Object> param  = (List<Object>) obj;
		try {
			MJ_User user  = this.player.getBusiness().getPlayer();
			CCMD11101  ccmd  = new CCMD11101();
			int cost = 0;
			switch(type)
			{
				case  1:   flag = modNick(user,(String)param.get(0)); break;
				case  2:	
						cost = (Integer) param.get(0);
						if(cost > 0)cost = -cost;
						flag = this.mod_gold(cost);		break;  //金币
				case  3: 	mod_juqing_1(user);		break;  //剧情1   初始 1，达到4，自动解锁形象100     每完成一章加金币100
				case  4:   	mod_juqing_2(user);     break;  //剧情2
				case  5:    mod_juqing_3(user);    	break;  //剧情3 
				case  6:    
						cost = (Integer) param.get(0);
						if(cost > 0)cost = -cost;
						flag = this.mod_dianquan(cost);break; // 修改钻石
				case  7:    mod_sex(user,(Integer)param.get(0));break; //修改性别
				case  9: 	mod_newTeach(user,(Integer)param.get(0));	break;
				case 11:   unlockRole(user,(Integer)param.get(0)); 		break;  //所有头像
				
				case 14:   
						boolean adults = (Boolean) param.get(0);
						user.setAdults(adults);
					break;  //所有头像
				case 12:    user.setOwnSkills(listToStr(param));  		break;  //拥有技能
				case 21: 	user.setLevel((Integer)param.get(0)); 		break;  //等级
				case 22: 	user.setScore((Integer)param.get(0)); 		break;  //积分
				case 23: 	equipImage(user,(Integer)param.get(0)); 		break;  //形象
				case 24:   

					boolean modeE = modeEquipSkill(user,param); if(!modeE)return false; 		break; //修改当前技能
				default:
					return false;
			}
			ChannelBuffer buf101  = ccmd.auto_deal(this.player, type);
			if(buf101 ==null) return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	public void mod_sex(MJ_User user,int type)
	{
		if(user.getSex() != -1)return;
		if(type == 0)
		{
			user.setSex(0);
		}else 
		{
			user.setSex(1);
		}
		//赠送形象
		CCMD11111 cmd111 = new CCMD11111();
		if(user.getSex() == 0) //女学生
		{
			unlockRole(user,2002);
			this.equipImage(user, 2002);
			user.setImage(2002);
			cmd111.auto_deal(player, "网络注册成功,赠送形象:女学生 ");
			
		}else if(user.getSex() == 1)
		{
			unlockRole(user,2001);
			this.equipImage(user, 2001);
			user.setImage(2001);
			cmd111.auto_deal(player, "网络注册成功,赠送形象:男学生 ");
		}else
		{
			return;
		}
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.setPlayer(player);
		cmd101.sendUserInfo(MyArrays.asList(7,11,23));
//		cmd101.auto_deal(this.player,7 );
		this.player.getBusiness().savePlayer(user);
	}
	public void mod_newTeach(MJ_User user,int newTeach)
	{
		user.setNewTeach(newTeach);
		this.player.getBusiness().savePlayer(user);
	}
	public void equipImage(MJ_User user,int imgId)
	{
		 Set<MJ_Role> roles = user.getRoles();
		 Iterator<MJ_Role> it = roles.iterator();
		 while(it.hasNext())
		 {
			 MJ_Role role = it.next();
			 if(role.getRoleId() == imgId)
			 {
				 user.setImage(imgId);
				 break;
			 }
		 }
	}
	public void mod_juqing_1(MJ_User user )
	{
		CheckTask ctask = new CheckTask();
		
		cdao = new CountDao();
		CCMD11101  ccmd  = new CCMD11101();
		
		if(user.getJuqing_1() < GlobalData.MAX_JUQING_1)
		{
			user.setJuqing_1(user.getJuqing_1()+1);
			
			Sts_JuqingDay day = this.getJuqingCountData();
			int beforeGold = user.getGold();
			int modGold = 0;
			switch(user.getJuqing_1())
			{
				case 2: 
					modGold = 100;
					user.setGold(user.getGold() + modGold);
					day.setJuqing1(day.getJuqing1() + 1);
					
					ctask.checkTaskJuqing(this.player, CheckTask.TASK_JUQING, 2, 1);
					break; 
				case 3: 
					modGold = 100;
					user.setGold(user.getGold() + modGold);
					day.setJuqing2(day.getJuqing2() + 1);
					ctask.checkTaskJuqing(this.player, CheckTask.TASK_JUQING, 3, 1);
					break; 
				case 4: 
					modGold = 100;
					user.setGold(user.getGold() + modGold);
					day.setJuqing3(day.getJuqing3() + 1);
					ctask.checkTaskJuqing(this.player, CheckTask.TASK_JUQING, 4, 1);
					break; 
				case 5: //第四章 100号形象
					modGold = 100;
					user.setGold(user.getGold() + modGold);
					day.setJuqing4(day.getJuqing4() + 1);
					ctask.checkTaskJuqing(this.player, CheckTask.TASK_JUQING, 5, 1);
					break;
			}
			this.player.getBusiness().savePlayer(user);
			ccmd.auto_deal(this.player, 2);
			modeJuqingDayOtherData(day);
			cdao.saveSts_Object(day);
			
			this.player.saveUserChargeRec(11102+"",beforeGold , modGold, user.getGold(), 0, 0, 0, "剧情进度1:"+user.getJuqing_1()+"金币修改");
			
			
			
		}
		if(user.getJuqing_1() == GlobalData.MAX_JUQING_1)
		{
			if(user.getJuqing_2() == 0)
			{
				user.setJuqing_2(1);
				ccmd.auto_deal(this.player, 4);
			}
			//保存剧情进度数据
		}
		if(user.getJuqing_1() == 4)
		{
		  //解锁2001头像
//			unlockRole(user,2001);
//			this.equipImage(user, 2001);
//			user.setImage(2001);
//			CCMD11101 cmd101 = new CCMD11101();
//			cmd101.auto_deal(this.player,23 );
//			this.unlockSkill(user, 5);
		}else if(user.getJuqing_1() == 5)
		{
			  //解锁100头像
				unlockRole(user,100);
		}
	}
	public void mod_juqing_2(MJ_User user )
	{
		cdao = new CountDao();
		CCMD11101  ccmd  = new CCMD11101();
		if(user.getJuqing_2() < GlobalData.MAX_JUQING_2)
		{
			user.setGold(user.getGold() + GlobalData.JUQING2_GOLD);
			ccmd.auto_deal(this.player, 2);			
			user.setJuqing_2(user.getJuqing_2()+1);
		}
		if(user.getJuqing_2() == GlobalData.MAX_JUQING_2)
		{
			if(user.getJuqing_3() == 0)
			{
				user.setJuqing_3(1);
				ccmd.auto_deal(this.player, 5);
			}
			Sts_JuqingDay day = this.getJuqingCountData();
			day.setJuqing1(day.getJuqing2() + 1);
			modeJuqingDayOtherData(day);
			cdao.saveSts_Object(day);
		}
	}
	public void mod_juqing_3(MJ_User user )
	{
		cdao = new CountDao();
		CCMD11101  ccmd  = new CCMD11101();
		if(user.getJuqing_3() < GlobalData.MAX_JUQING_3)
		{
			user.setJuqing_3(user.getJuqing_3()+1);
			user.setGold(user.getGold() + GlobalData.JUQING3_GOLD);
			ccmd.auto_deal(this.player, 2);
		}
		if(user.getJuqing_3() == GlobalData.MAX_JUQING_3)
		{
			Sts_JuqingDay day = this.getJuqingCountData();
			day.setJuqing1(day.getJuqing3() + 1);
			modeJuqingDayOtherData(day);
			cdao.saveSts_Object(day);
		}
	}
	
	public Sts_JuqingDay getJuqingCountData()
	{
		Sts_JuqingDay day = cdao.findTodayJuqing();
		if(day == null)
		{
			day = new Sts_JuqingDay();
			day.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
			day.setDay(ServerTimer.getDay());
			day.setTime1(ServerTimer.getDay());
			day.setTime2(ServerTimer.getMonth());
		}
		return day;
		
	}
	public void modeJuqingDayOtherData(Sts_JuqingDay day)
	{
		//总人数
		//总有头像数
		//总对战数
		int totalReg = 0;
		int totalHadRole = 0;
		int totalBattle = 0;
		
		totalReg = cdao.findTotalRegPlayer();
		totalHadRole = cdao.findTotalHadRole();
		totalBattle = cdao.findTotalJoinedBattle();
		day.setTotalPlayer(totalReg);
		day.setHistoryHadImg(totalHadRole);
		day.setHistoryNetBattle(totalBattle);
	}
	
	public boolean unlockRole(MJ_User user ,int roleId)
	{
		
		if(!user.isHadRole())user.setHadRole(true);
		CCMD11101  ccmd  = new CCMD11101();
		Set<MJ_Role> roles  =  user.getRoles();
		Iterator<MJ_Role> it  = roles.iterator();
		boolean alreadyHad = false;
		while(it.hasNext())
		{
			MJ_Role role  =  it.next();
			if(role.getRoleId() == roleId)
			{
				System.out.println("unlockRole fail: "+ roleId);
				alreadyHad  = true;
				break;
			}
		}
		if(!alreadyHad)
		{
			MJ_Role newRole  = new MJ_Role();
			newRole.init(roleId);
			newRole.setUser(user);
			roles.add(newRole);
			this.player.getBusiness().saveRole(newRole);
		}
		ccmd.auto_deal(this.player, 11);
		return !alreadyHad;
	}
	public boolean unlockSkill(MJ_User user ,int skillId)
	{
		Log.log("unlockSkill:" + skillId);
		CCMD11101  ccmd  = new CCMD11101();
		String[] ownSkills = user.getOwnSkills().split(",");
		List<Object> skills = new ArrayList<Object>();
		for(int i = 0; i < ownSkills.length;i++)
		{
			if(ownSkills[i].equals("")||ownSkills[i].equals(" "))
				continue;
			int tmpId = Integer.parseInt(ownSkills[i]);
			if(tmpId == skillId)
			{
				ccmd.auto_deal(this.player, 12);
				return false;
			}
			skills.add(tmpId);
		}
		skills.add(skillId);
		Log.log("unlockSkill arr:" + skills);
		String _r = listToStr(skills);
		Log.log("unlockSkill:" + _r);
		user.setOwnSkills(_r);
		this.player.getBusiness().savePlayer(user);
		ccmd.auto_deal(this.player, 12);
		return true;
	}
	/**
	 * @return  
	 */
	public boolean modNick(MJ_User user,String nick )
	{
		Business b =  this.player.getBusiness();
		MJ_User tmp  = b.findPlayerByNick(nick);
		if(tmp != null) return false;
		user.setNick(nick);
		b.savePlayer(user);
		return true;
		//验证名字的唯一性
		//验证名字的合法性(关键词)
	}
	public boolean  modeEquipSkill(MJ_User user,Object obj)
	{
		 List<Object> list  = (List<Object>) obj;
		 String ownSkill  =  user.getOwnSkills();
		 
		 String skills = "";
		 for(int i  = 0;i < list.size();i++)
		 {
			 String s  = ((Integer)list.get(i)).toString();
			 if("0".equals(s) || "-1".equals(s))continue;
			 if(ownSkill.indexOf(s)== -1)
			 {
				 return false;
			 }
			 skills += list.get(i)+",";
		 }
		 user.setEquipSkill(skills);
		 
		return true;
	}
	/**
	 * 修改金币
	 */
	public boolean mod_gold(int mod)
	{
	 	MJ_User user = this.player.getBusiness().getPlayer();
	 	if(mod < 0)
	 	{
	 		if(user.getGold() + mod >= 0)
	 		{
	 			user.setGold(user.getGold() + mod);
	 		}else
	 		{
	 			return false;
	 		}
	 	}else
	 	{
	 		Log.log("mod gold : " + mod);
	 		Log.log("mod gold  before gold: " + user.getGold());
			user.setGold(user.getGold() + mod);
	 	} 
	 	this.player.getBusiness().savePlayer(user);
	 	
	 	Log.log("mod gold user:" + user.getGold());
		CCMD11101  ccmd  = new CCMD11101();
		ccmd.auto_deal(this.player, 2);
		return true;
	}
	/**
	 * 修改点券
	 */
	public boolean mod_dianquan(int mod)
	{
	 	MJ_User user = this.player.getBusiness().getPlayer();
	 	if(mod < 0)
	 	{
	 		if(user.getDianQuan() + mod >= 0)
	 		{
	 			user.setDianQuan(user.getDianQuan() + mod);
	 		}else
	 		{
	 			return false;
	 		}
	 	}else
	 	{
			user.setDianQuan(user.getDianQuan() + mod);
	 	}
	 	this.player.getBusiness().savePlayer(user);
		CCMD11101  ccmd  = new CCMD11101();
		ccmd.auto_deal(this.player, 6);
		return true;
	}
	/**
	 * 刷新人机对战次数限制
	 */
	public boolean refreshDanji()
	{
	 	MJ_User user = this.player.getBusiness().getPlayer();
	 	int cost = user.getRefreshDia();
	 	if(cost > user.getDianQuan())return false;
	 	this.mod_dianquan(-cost);
	 	user.setDanJiChangs(CCMD10001.MAX_CHANG_DANJI);
	 	this.player.getBusiness().savePlayer(user);
	 	CCMD11101 cmd101 = new CCMD11101();
	 	cmd101.setPlayer(player);
	 	cmd101.auto_deal(MyArrays.asList(32), null);
		return true;
	}
	
	
	/**
	 * 修改道具数量
	 */
	public boolean mod_prop(MJ_User user,int propId ,int propNum)
	{
		M_Prop prop = user.getProps();
		boolean flag = false;
		
		propId = propId%100;
		switch(propId)
		{
			case 1:  //喇叭
				if(prop.getPro1Num() + propNum < 0) flag = false;
				else
				{
						prop.setPro1Num(prop.getPro1Num() + propNum);
						flag = true;
				}
				break;//喇叭  
			case 2: //邀请函
				
					if(prop.getPro2Num() + propNum < 0) flag = false;
					else
					{
						prop.setPro2Num(prop.getPro2Num() + propNum);
						flag = true;
					}
				break;
			case 3: //幸运药水
				
				if(prop.getPro3Num() + propNum < 0) flag = false;
				else
				{
					prop.setPro3Num(prop.getPro3Num() + propNum);
					flag = true;
				}
			break;
			case 4: //强运药水
				
				if(prop.getPro4Num() + propNum < 0) flag = false;
				else
				{
					prop.setPro4Num(prop.getPro4Num() + propNum);
					flag = true;
				}
			break;
		}
		if(flag)
		{
			this.player.getBusiness().saveProp(prop);
			CCMD11101 cmd101= new CCMD11101();
			cmd101.replay_prop(this.player, propId);
		}
		return flag;
	}
	/**
	 *  修改杯赛胜场记录
	 */
	public void mod_Cup(int cupType,int modNum)
	{
		MJ_User user = this.player.getBusiness().getPlayer();
		List<Integer> list = user.getCupScore();
		list.set(cupType, list.get(cupType) + modNum);
		user.setCupScore(list);
		this.player.getBusiness().savePlayer(user);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.auto_deal(player, 31);
	}
	/**
	 *  解锁杯赛
	 */
	public void unlock_Cup(int cupType)
	{
		MJ_User user = this.player.getBusiness().getPlayer();
		List<Integer> list = user.getCupScore();
		list.set(cupType, 0);
		user.setCupScore(list);
		this.player.getBusiness().savePlayer(user);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.setPlayer(player);
		cmd101.auto_deal(player, 31);
	}
	
	public String listToStr(List<Object> list)
	{
		String str = "";
		for(int i  = 0 ; i <  list.size();i++)
		{
			str  += (String)((list.get(i)).toString())+",";
		}
		return str;
		
	}
}
