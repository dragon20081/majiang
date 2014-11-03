package server.command.cmd;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MaJiang_Fan;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;

import common.DecodeCMD;
import common.Log;


/**
 *  胡
 * @author xue
 *
 */
public class SCMD11009 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11009.class.getName());
	
	public static  int   PASSABLE = 1;
	public static  int   PASS_NOT = 2;
	
	private  List<MgsPlayer> hu_players;
	@Override
	public ChannelBuffer getBytes() {
		
		MyArray arr = new MyArray();
		arr.push(hu_players.size());
		for(int i = 0 ; i < hu_players.size();i++)
		{
			MgsPlayer tmpp = hu_players.get(i);
			arr.push(tmpp.getRoomId());
			Log.log("胡牌:"+tmpp.getRoomId()+"|"+tmpp.getLocId());
			MaJiang_Fan fan = tmpp.getFan();
			Map<String,Integer> faninfo  =fan.getFanInfos();
			if(faninfo.size() == 0)
			{
				fan.setTotalFan(1);			
				faninfo.put("和", 1);
			}
			
			
			arr.push(faninfo.size());
			Iterator<String> it = faninfo.keySet().iterator();
			while(it.hasNext())
			{
				String key  = it.next();
				Integer value = faninfo.get(key);
				int fanId  	=  this.getFanId(key);
				if(fanId == 0)continue;
				arr.push(fanId);
				arr.push(value);
			}
		}
		MyByteArray byteArray =  new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	public ChannelBuffer getBytes_single(MaJiang_Fan fan1)
	{
	
		MyArray arr = new MyArray();
			MaJiang_Fan fan = fan1;
			Map<String,Integer> faninfo  =fan.getFanInfos();
			Iterator<String> it = faninfo.keySet().iterator();
			while(it.hasNext())
			{
				String key  = it.next();
				Integer value = faninfo.get(key);
				int fanId  	=  this.getFanId(key);
				if(fanId  < 1)
				{
					try {
						throw new Exception("scmd11009 fanId: "+fanId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				}
				arr.push(fanId);
				arr.push(value);
			}
		MyByteArray byteArray =  new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public List<MgsPlayer> getHu_players() {
		return hu_players;
	}
	public void setHu_players(List<MgsPlayer> hu_players) {
		this.hu_players = hu_players;
	}
	public int getFanId(String key)
	{
		Map<String,Integer> map  =  new HashMap<String, Integer>();
		map.put("听牌",9);
		map.put("天听",10);
		map.put("杠",11);
		map.put("四归一",12);
		map.put("场风刻",13);
		map.put("门风刻",14);
		map.put("三元刻",15);
		map.put("平和",16);
		map.put("断幺九",17);
		map.put("门前清",18);
		map.put("一般高",19);
		map.put("暗七对",20);
		map.put("碰碰和",21);
		map.put("三色同顺",22);
		map.put("三色同刻",23);
		map.put("三暗刻",24);
		map.put("全带幺",25);
		map.put("混幺九",26);
		map.put("混一色",27);
		map.put("清一色",28);
		map.put("小三元",29);
		map.put("一气贯通",30);
		map.put("杠上开花",31);
		map.put("海底捞月",32);
		map.put("抢杠胡",33);
		map.put("天和",34);
		map.put("地和",35);
		map.put("大三元",36);
		map.put("小四喜",37);
		map.put("大四喜",38);
		map.put("四暗刻",39);
		map.put("十三幺",40);
		map.put("九宝莲灯",41);
		map.put("十八学士",42);
		map.put("字一色",43);
		map.put("清老头",44);
		map.put("绿一色",45);
		
		map.put("三元刻_中",46);
		map.put("三元刻_发",47);
		map.put("三元刻_白",48);
		map.put("和",49);
		map.put("连庄",50);
		int _t = 0;
		if(map.get(key) == null)
		{
			System.out.println("map key null");
		}else
		{
			 _t =   map.get(key);
		}
		return  _t-8;
	}
	
}
