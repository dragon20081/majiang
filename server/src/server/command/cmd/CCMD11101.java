package server.command.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PatternPai;
import server.command.SCMD3;
import server.mj.MgsPlayer;
import business.entity.MJ_Role;
import business.entity.MJ_User;
import business.entity.M_Prop;
import business.entity.M_Shop;

/**
 *  获取玩家个人信息
 * @author xue
 */
public class CCMD11101 extends  CMD{
	
	private PatternPai pattern = new PatternPai();

	
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
		SCMD3  scmd3  = new SCMD3();
		List<Object> list = this.getValues();
		for(int i  = 0; i  < list.size();i++)
		{
			Integer d  =  (Integer)(list.get(i));
			int num  = d.intValue();
			ChannelBuffer tmpbuf  = replay(num);			//根据类型回复
			if(tmpbuf == null)
			{
				try {
					throw new Exception("11101  null  type");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else
			{
				scmd3.list.add(tmpbuf);
			}
		}
		this.player.send(3, scmd3.getBytes());
	}
	public void sendUserInfo(List<Integer> list)
	{
		SCMD3  scmd3  = new SCMD3();
		for(int i  = 0; i  < list.size();i++)
		{
			Integer d  =  (Integer)(list.get(i));
			int num  = d.intValue();
			if(num == 7)
			{
				MJ_User user = this.player.getBusiness().getPlayer();
				if(user.getSex() == -1)continue;
			}
			ChannelBuffer tmpbuf  = replay(num);			//根据类型回复
			if(tmpbuf == null)
			{
				try {
					throw new Exception("11101  null  type");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else
			{
				scmd3.list.add(tmpbuf);
			}
		}
		this.player.send(3, scmd3.getBytes());
	}
	
	public ChannelBuffer auto_deal(MgsPlayer p,int id)
	{
		this.player  =  p;
		int num  = id;
		ChannelBuffer tmpbuf  = replay(num);			//根据类型回复
		SCMD3 scmd  = new SCMD3();
		scmd.list.add(tmpbuf);
		this.player.send(3, scmd.getBytes());
		return tmpbuf;
	}
	/**
			 	1	名字
				2	金币
				3	剧情1进度
				4	剧情2进度
				5	剧情3进度
				6  	点券
					
					
				11	解锁形象
				12	拥有技能
					
					
				21	网络等级
				22	网络积分
				
				31     商店商品
	 */
	private ChannelBuffer replay(int type)
	{
//		Log.info("type:" + type);
		MJ_User user  = this.player.getBusiness().getPlayer();
		MyArray arr =  new MyArray();
		 arr.push(type);
		switch(type)
		{
			case  1: 	arr.push(user.getNick());		break;    
			case  2:	arr.push(user.getGold());		break; 
			case  3: 	arr.push(user.getJuqing_1());	break; 
			case  4:   	arr.push(user.getJuqing_2());	break; 
			case  5:   	arr.push(user.getJuqing_3());   break; 
			case  6:    arr.push(user.getDianQuan());   break;
			case  7:    arr.push(user.getSex()); 		break;
			case  9: 	arr.push(user.getNewTeach()); 	break; 
			case 11:  	pushRoleSet(arr,user.getRoles()); 			break;
			//case -11:   pushRole(arr);break; //修改单个形象的经验， 等级
			case 12:  arr.push(strToList(user.getOwnSkills())); 	break;
			case 13: 	writeProps(arr,user.getProps());break; //全部道具
			case -13: break; //单个道具
			
			case 21: arr.push(user.getLevel());			break;  //-21
			case 22: arr.push(user.getScore());			break; //-22  积分增加
			case 23: arr.push(user.getImage());			break;
			case 24: arr.push(strToList(user.getEquipSkill()));		break; 
			
			case 31: 
//				Log.info("getCupScore:   "+user.getCupScore().toString());
				arr.push(user.getCupScore());
			case 32: arr.push(user.getDanJiChangs());
				break;
			case 33: 
				arr.push(user.getRefreshDia());
				break;
		//	case 31: writeShopItem(arr,user.getShopItems());break;
		}
		if(arr.length() > 0)
		{
//			Log.info("send 11101....");
			MyByteArray byteArr =  new MyByteArray();
			byteArr.write(arr);
			return this.player.coderCMD(11101, byteArr.getBuf());
		}
		return null;
	}
	
	private List<Integer> strToList(String str)
	{
		String[] str1 =  str.split(",");
		List<Integer> list  = new ArrayList<Integer>();
		for(int i = 0 ; i  < str1.length;i++)
		{
			if("".equals(str1[i]) ||" ".equals(str1[i]))continue;
			Integer skill  = 0;
			try {
				skill = Integer.parseInt(str1[i]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				continue;
			}
			list.add(skill);
		}
		return list;
	}
	private void pushRoleSet(MyArray arr ,Set<MJ_Role> roles)
	{
		List<List<Integer>> list  =  new ArrayList<List<Integer>>();
		Iterator<MJ_Role> it  = roles.iterator();
		while(it.hasNext())
		{
			List<Integer> tmp = new ArrayList<Integer>();
			MJ_Role r = it.next();
			tmp.add(r.getRoleId());
			tmp.add(r.getLevel());
			tmp.add(r.getExp());
			list.add(tmp);
		}
		arr.push(list);
	}
	/**
	 * 角色经验值修改
	 */
	public void mgs_ModRoleExp(MgsPlayer p,MJ_Role role,int modValue)
	{
		Log.log("增加角色经验-->" + p.getName());
		this.player = p;
		MyArray arr = new MyArray();
		arr.push(-11);
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.add(role.getRoleId());
		tmp.add(modValue);
		arr.push(tmp);
		MyByteArray byteArr =  new MyByteArray();
		byteArr.write(arr);
		this.player.send(11101, byteArr.getBuf());
	}
	public void mgs_modPlayerScore(MgsPlayer p,int modValue)
	{
		this.player = p;
		MyArray arr = new MyArray();
		arr.push(-22);
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.add(modValue);
		arr.push(tmp);
		MyByteArray byteArr =  new MyByteArray();
		byteArr.write(arr);
		this.player.send(11101, byteArr.getBuf());
	}
	public void msg_modPlayerLevel(MgsPlayer p,int modValue)
	{
		this.player = p;
		MyArray arr = new MyArray();
		arr.push(-21);
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.add(modValue);
		arr.push(tmp);
		MyByteArray byteArr =  new MyByteArray();
		byteArr.write(arr);
		this.player.send(11101, byteArr.getBuf());
	}
		
	private void writeShopItem(MyArray arr,List<M_Shop> list)
	{
		for(int i = 0 ; i < list.size();i++)
		{
			M_Shop shop  =  list.get(i); 
			List<Integer> item   =  new ArrayList<Integer>();
			if(shop.getOnSell() == 0)continue;
			item.add(shop.getProId());
			item.add(shop.getPrice_dianquan());
			item.add(shop.getProId());
			arr.push(item);
		}
		
	}
	public void auto_deal(List<Integer> list,ChannelBuffer buf)
	{
		SCMD3  scmd3  = new SCMD3();
		for(int i  = 0; i  < list.size();i++)
		{
			Integer d  =  (Integer)(list.get(i));
			int num  = d.intValue();
			ChannelBuffer tmpbuf  = replay(num);			//根据类型回复
			if(tmpbuf == null)
			{
				try {
					throw new Exception("11101  null  type");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else
			{
				scmd3.list.add(tmpbuf);
			}
		}
		if(buf != null)scmd3.list.add(buf);
		this.player.send(3, scmd3.getBytes());
	}
	public void writeProps(MyArray arr,M_Prop prop)
	{
		List<Integer> list  = new ArrayList<Integer>();
		list.add(1);
		list.add(prop.getPro1Num());
		
		List<Integer> list2  = new ArrayList<Integer>();
		list2.add(2);
		list2.add(prop.getPro2Num());
		
		List<Integer> list3  = new ArrayList<Integer>();
		list3.add(3);
		list3.add(prop.getPro3Num());
		
		
		List<Integer> list4  = new ArrayList<Integer>();
		list4.add(4);
		list4.add(prop.getPro4Num());
		
		List<Object> olist = new ArrayList<Object>();
		olist.add(list);
		olist.add(list2);
		olist.add(list3);
		olist.add(list4);
		arr.push(olist);
//		arr.push((new ArrayList<Object>()).add(list));
	}
	public void replay_prop(MgsPlayer p,int propId)
	{
		M_Prop prop = p.getBusiness().getPlayer().getProps();
		MyArray arr =  new MyArray();
		arr.push(-13); //14 单个道具
		List<Integer> list = new ArrayList<Integer>(); 
		propId = propId%100;
		 switch(propId)
		 {
		 	case 1:
		 		list.add(1); list.add(prop.getPro1Num());break;
			case 2: 	
		 		list.add(2); list.add(prop.getPro2Num());break;
			case 3: 	
		 		list.add(3); list.add(prop.getPro3Num());break;
			case 4: 	
		 		list.add(4); list.add(prop.getPro4Num());break;
		 }
		 arr.push(list);
		MyByteArray bytearr = new MyByteArray();
		bytearr.write(arr);
		p.send(11101, bytearr.getBuf());
	}
	

}
