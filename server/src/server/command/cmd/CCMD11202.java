package server.command.cmd;
/**
 * 获取资源ID对应的资源  类型 内容    1：数据  2 资源
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;
import common.MyArrays;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import business.entity.MJ_Cup;
import business.entity.MJ_DanJil;
import business.entity.MJ_DataFan;
import business.entity.MJ_DayTask;
import business.entity.MJ_PayInfo;
import business.entity.MJ_Resource;
import business.entity.MJ_Skill;
import business.entity.MJ_TmpResource;
import business.entity.M_Shop;

public class CCMD11202 extends CMD{

	
	
	private static final Logger logger = Logger.getLogger(CCMD11202.class.getName());
	private List<Object> roles = new ArrayList<Object>();
	private List<Object> skills = new ArrayList<Object>();
	private List<Object> props = new ArrayList<Object>();
	private List<Object> gold = new ArrayList<Object>();
	
	private List<Object> pays = new ArrayList<Object>();
	
	@Override
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		int type = this.getIntVaule(0);
		String rid = this.getStrValue(1); 
		System.out.println("202  type:" + type);
		if(type == 1)
		{
			getTmpResource(rid);
			return;
		}
		MyArray arr = new MyArray();
		if(rid == null){
			arr.push("null");
			arr.push(1);
			arr.push("");
			MyByteArray bytebuf = new MyByteArray();
			bytebuf.write(arr);
			this.player.send(11202,bytebuf.getBuf());
			return;
		}
		MJ_Resource r = Global.resMap.get(rid);
		if(r == null) return;
		
		logger.info("MJ_Resource : "+r.getName());
		
		if(r.getVersion() == 0)
		{
			arr.push(rid);
			arr.push(1);
			arr.push(null);
			MyByteArray bytebuf = new MyByteArray();
			bytebuf.write(arr);
			this.player.send(11202,bytebuf.getBuf());
			return;
		}

		if(r.getName().equals("商品")) //商店数据
		{
			arr.push(rid);
			arr.push(1);
			//商品详情
			List<Object> all = new ArrayList<Object>();
			 getShopItemList(Global.shopItem_roles);
			 getShopItemList(Global.shopItem_props);
			 getShopItemList(Global.shopItem_skills);
			 getShopItemList(Global.shopItem_gold);
			 getPayItemList(Global.payItems);
			 all.add(roles);
			 all.add(skills);
			 all.add(gold);
			 all.add(props);
			 all.add(pays);
			 arr.push(all);
		}else if(r.getName().equals("任务"))
		{
			arr.push(rid);
			arr.push(1);
			//任务列表
			List<Object> task = getTaskList(Global.tasks);
			arr.push(task);
		}else if(r.getName().equals("技能"))
		{
			arr.push(rid);
			arr.push(1);
			//技能列表
			List<Object> skill = getSkillList(Global.skills);
			arr.push(skill);
		}else if(r.getName().equals("番"))
		{
			arr.push(rid);
			arr.push(1);
			//番列表
			List<Object> fan = getFanList(Global.fans);
			arr.push(fan);
		}else if(r.getName().equals("SWF"))
		{
			arr.push(rid);
			arr.push(3);
			List<Object> swf = new ArrayList<Object>();
			swf.add(r.getUrl());
			arr.push(swf);
		}else if(r.getName().equals("MP3"))
		{
			arr.push(rid);
			arr.push(4);
			List<Object> mp3 = new ArrayList<Object>();
			mp3.add(r.getUrl());
			arr.push(mp3);
		}else if(r.getName().equals("单机"))
		{
			arr.push(rid);
			arr.push(1);
			List<Object> danji =getDanjiList(Global.danji);
			arr.push(danji);
		}else if(r.getName().equals("杯赛"))
		{
			arr.push(rid);
			arr.push(1);
			List<Object> cups =getCupList(Global.cups);
			arr.push(cups);
		}else
		{
			arr.push(rid);
			arr.push(2);
			List<Object> task = new ArrayList<Object>();
			task.add(r.getUrl());
			arr.push(task);
		}
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		this.player.send(11202,bytebuf.getBuf());
	}
	
	public void getShopItemList( Map<Integer,M_Shop> shopItems)
	{
		 Iterator<M_Shop> it1 = shopItems.values().iterator();
		 while(it1.hasNext())
		 {
			 M_Shop item = it1.next();
			 if(item.getOnSell() != 1)continue;
			 List<Integer> tmpList = MyArrays.asList(item.getProId(),item.getPrice_dianquan(),item.getPrice_gold(),item.getPropNum(),item.getFlag());
			 if(item.getType().equals("形象"))
				 roles.add(tmpList);
			 else if(item.getType().equals("技能"))
				 skills.add(tmpList);				 
			 else if(item.getType().equals("道具"))
				 props.add(tmpList);
			 else if(item.getType().equals("金币"))
				 gold.add(tmpList);
		 }	
	}
	public void getPayItemList(Map<Integer,MJ_PayInfo> payItems)
	{
		 String pingtai  = this.player.platform;
		 
		 Iterator<MJ_PayInfo> it = payItems.values().iterator();
		 while(it.hasNext())
		 {
			 MJ_PayInfo item = it.next();
			 if("IOS".equals(pingtai))
			 {
				if(!item.pingtai.equals("IOS"))continue; 
			 }else if("AND".equals(pingtai))
			 {
				 if(!item.pingtai.equals("ANDROID"))continue; 
			 }else if("AIYOUXI".equals(pingtai))
			 {
				 if(!item.pingtai.equals("AIYOUXI"))continue; 
			 }else if("360".equals(pingtai))
			 {
				 if(!item.pingtai.equals("ANDROID"))continue; 
			 }
			 List<Object> tmpList = new ArrayList<Object>();
			 tmpList.add(item.getPayId());
			 tmpList.add(item.getPayStr_android());
			 tmpList.add(item.getPayStr_apple());
			 tmpList.add(item.getDia());
			 tmpList.add(item.getRmb());
			 tmpList.add(item.getFlag());
			 
			 pays.add(tmpList);
		 }
		 Log.info(pays.toString());
	}
	public List<Object> getTaskList(Map<Integer,MJ_DayTask> tasks)
	{
		
		List<Object> all = new ArrayList<Object>();
		List<Object> list_type1 = new ArrayList<Object>();
		List<Object> list_type2 = new ArrayList<Object>();
		
		Iterator<MJ_DayTask> it  = tasks.values().iterator();
		while(it.hasNext())
		{
			MJ_DayTask task = it.next();
			List<Object> tmp = new ArrayList<Object>();
			
			tmp.add(task.getTaskId());
			tmp.add(task.getTaskDesc());
			tmp.add(task.getGold());
			tmp.add(task.getDia());
			
			String[] props = task.getProps().split(",");
			List<Integer> propList = new ArrayList<Integer>();
			for(int i = 0 ; i < props.length; i++)
			{
				if(props[i].equals("") || props[i].equals(" ")) continue;
				propList.add(Integer.parseInt(props[i]));
			}
			tmp.add(propList);
			
			if(task.getTaskType() == 1)
				list_type1.add(tmp);
			else 	
				list_type2.add(tmp);
		}
		all.add(list_type1);
		all.add(list_type2);
		Log.log("ccmd11202 "+all);
		return all;
	}
	public  List<Object> getSkillList(Map<String,MJ_Skill> skills)
	{
		List<Object> all = new ArrayList<Object>();
		
		all.add(null);
		Iterator<MJ_Skill> it  = skills.values().iterator();
		while(it.hasNext())
		{
			MJ_Skill skill = it.next();
			List<Object> tmp = new ArrayList<Object>();
			tmp.add(skill.getName());
			tmp.add(skill.getCost0());
			tmp.add(skill.getAnimeType());
			tmp.add(skill.getDesc0());
			tmp.add(skill.getCondition0());
			tmp.add(skill.getAttr0());
			tmp.add(skill.getAttr0());
			all.add(tmp);
		}
		return all;
	}
	
	public  List<Object> getFanList(Map<String,MJ_DataFan> fans)
	{
		List<Object> all = new ArrayList<Object>();
		
		Iterator<MJ_DataFan> it  = fans.values().iterator();
		while(it.hasNext())
		{
			MJ_DataFan fan = it.next();
			List<Object> tmp = new ArrayList<Object>();
			
			String name = fan.getName();
			if(name.equals("空1") || name.equals("空2"))
			{
				all.add(null);
				continue;
			}
			tmp.add(fan.getName());
			tmp.add(fan.getFan());
			tmp.add(fan.getVoice0());
			all.add(tmp);
		}
		return all;
	}
	/**
	 *  type ==  9   type 数组
	 */
	public void getTmpResource(String rid)
	{
		System.out.println("getTmpResource rid:" + rid);
		MJ_TmpResource tmpr = Global.tmpresMap.get(rid);
		if("资源包".equals(tmpr.getName()))
		{
			//先所有名字平起来的字符串， 再所有版本号拼起来的字符串
			MyArray arr = new MyArray();
			arr.push(tmpr.getRid());
			arr.push(9);
			List<Object> tmpList = new ArrayList<Object>();
			tmpList.add(tmpr.getUrl());
			tmpList.add(getVersionStr(tmpr.getUrl()));
			tmpList.add(tmpr.getSize());
			tmpList.add(tmpr.getInfo());
			arr.push(tmpList);
			
			MyByteArray bytebuf = new MyByteArray();
			bytebuf.write(arr);
			
			this.player.send(11202,bytebuf.getBuf());
			System.out.println("202:" + arr.getList().toString());
		}else
		{
			int type = 1;
			if(tmpr.getName().equals("SWF"))
			{
				type = 3;
			}else if(tmpr.getName().equals("MP3"))
			{
				type = 4;
			}else
			{
				type = 2;
			}
			MyArray arr = new MyArray();
			arr.push(tmpr.getRid());
			arr.push(type);
			List<Object> tmplist = new ArrayList<Object>();
			tmplist.add(tmpr.getUrl());
			arr.push(tmplist);
			
			MyByteArray bytebuf = new MyByteArray();
			bytebuf.write(arr);
			this.player.send(11202,bytebuf.getBuf());
		}
		
	}
	public String getVersionStr(String str)
	{
		
		Map<String,MJ_TmpResource> r = Global.tmpresMap;
		String[] names = str.split(",");
		String version = "";
		
		for(int i = 0; i < names.length;i++)
		{
			if(names[i].equals("") || names[i].equals(" "))continue;
			MJ_TmpResource tmp = r.get(names[i]);
			version  += tmp.getVersion();
			if( i < names.length - 1)
				version += ",";
		}
		return version;
	}
	
	
	public  List<Object> getDanjiList(Map<Integer,MJ_DanJil> danji)
	{
		List<Object> all = new ArrayList<Object>();
		
		all.add(null);
		Iterator<MJ_DanJil> it  = danji.values().iterator();
		while(it.hasNext())
		{
			MJ_DanJil tmpDanji = it.next();
			List<Object> tmp = new ArrayList<Object>();
//			tmp.add(tmpDanji.getId());
			tmp.add(tmpDanji.getBei());
			tmp.add(tmpDanji.getCost());
			tmp.add(tmpDanji.getQuan());
			tmp.add(tmpDanji.getStartPoint());
			tmp.add(tmpDanji.getPan());
			tmp.add(tmpDanji.isSkill());
			all.add(tmp);
		}
		return all;
	}
	
	public  List<Object> getCupList(Map<Integer,MJ_Cup> cups)
	{
		List<Object> all = new ArrayList<Object>();
		all.add(null);
		Iterator<MJ_Cup> it  = cups.values().iterator();
		while(it.hasNext())
		{
			MJ_Cup tmpCup = it.next();
			List<Object> tmp = new ArrayList<Object>();
			tmp.add(tmpCup.getId());
			tmp.add(tmpCup.getName());
			tmp.add(tmpCup.getInfo());
			tmp.add(tmpCup.getRule());
			tmp.add(tmpCup.getCost());
			tmp.add(tmpCup.getDifficulty());
			tmp.add(tmpCup.getCondition());
			tmp.add(tmpCup.getScopePlayer());
			tmp.add(tmpCup.getPlayerNum());
			all.add(tmp);
		}
		return all;
	}
}
