package server.command.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;
import common.MyArrays;

import server.command.Algorithm_Hu;
import server.command.CMD;
import server.command.GroupShoupai;
import server.command.MaJiang_Fan;
import server.command.PatternPai;
import server.command.SortByFan;
import server.mj.CheckTask;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 * 前台单机算番\
 * 
 * [Arr_now,paiHu,peng,chi,gang,isGangshanghua,
						isHaidilao,isQianggang,isTianhe,isDihe,isTing,
						isTianting,this.Wind_Chang,ziFeng,isZimo]
 * @author xue
 */
public class CCMD11019 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11019.class.getName());
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
		//参数
		List<Object> values  = this.getValues();
		List<Object> shoupai  	= (List<Object>) values.get(0);
		int hupaiId 		 	= (Integer) 	 values.get(1);
		List<Object> peng  		= (List<Object>) values.get(2);
		List<Object> chi  		= (List<Object>) values.get(3);
		List<Object> gang  		= (List<Object>) values.get(4);
		boolean isGangshanghua  = (Boolean) getbool(values.get(5));
		boolean isHaidilao  	= (Boolean) getbool(values.get(6));
		boolean isQianggang  	= (Boolean) getbool(values.get(7));
		boolean isTianhe  		= (Boolean) getbool(values.get(8));
		boolean isDihe  		= (Boolean) getbool(values.get(9));
		boolean isTing  		= (Boolean) getbool(values.get(10));
		boolean isTianting  	= (Boolean) getbool(values.get(11));
		int changfeng 			= (Integer) values.get(12);
		int menfeng 			= (Integer) values.get(13);
		boolean isZimo  		= (Boolean) getbool(values.get(14)); 
		int lianzhuang 			= (Integer) values.get(15);
		
		Log.info("连庄: ---- >" + lianzhuang);
		//赋值
		this.player.setTianhe(isTianhe);
		this.player.setDihe(isDihe);
		this.player.setTing(isTing);
		this.player.setTianting(isTianting);
		this.player.setQianggang(isQianggang);
		this.player.setGangshanghua(isGangshanghua);
		
		this.player.setGang(this.getGangList(gang));
		this.player.setChi(this.getchiList(chi));
		this.player.setPeng(getIntegerList(peng));
		List<Integer>  arr_sp = getIntegerList(shoupai);
		arr_sp = MyArrays.sort(arr_sp);
		Algorithm_Hu hu = new Algorithm_Hu();
		hu.hupaiId    = hupaiId;
		hu.isZimo  = isZimo ;
		boolean b  =  hu.testHu(arr_sp);
		if(b)
		{
			arr_sp.add(hupaiId);
			 player.setShoupai(arr_sp);
			 caculateFan(player,hu.shoupai,hu.isAnqidui, changfeng, menfeng ,isHaidilao,lianzhuang);//
			 //发送结果至客户端
			 sendResultFan();
		}else
		{
			//掉线
			this.player.send(10002, null);
			CCMD11111 cmd111 = new CCMD11111();
			cmd111.auto_deal(player, "结算失败!");
			
			logger.info("cmd11019 error hu:"+this.player.getAllPaiStr());

		}
	}
	
	public void sendResultFan()
	{
		MaJiang_Fan result  =  this.player.getFan();
		if(result.getFanInfos().size() == 0)
		{
			result.setTotalFan(1);			
			result.getFanInfos().put("和", 1);
		}
		SCMD11009  scmd = new SCMD11009();
		ChannelBuffer buf = scmd.getBytes_single(result);
		this.player.send(11019, buf);
		
		
//		if(result.getTotalFan() >= 10 && this.player.cupOpen)
//		{
//			 CheckTask checkTask = new CheckTask();
//			checkTask.checkTaskAtterHuPai(player, CheckTask.TASK_MANGUAN);
//		}
		
	}
	
	public synchronized void caculateFan(MgsPlayer p,Map<Integer,GroupShoupai> map,boolean isAnqidui,int changfeng,int  menfeng,boolean lastOne,int lianzhuang)
	{
		List<MaJiang_Fan>  fans = new ArrayList<MaJiang_Fan>();
		Iterator<Integer> it  = map.keySet().iterator();
		while(it.hasNext())
		{
			int key  = it.next();
			GroupShoupai shoupai  = map.get(key);
			shoupai.p = p;
			shoupai.patternShoupai();
			MaJiang_Fan fan = new MaJiang_Fan();
			fan.init(p, shoupai.getAnke(), shoupai.getShun(), shoupai.getJiang(),
										shoupai.getTing(), isAnqidui, p.isGangshanghua(), lastOne, p.isQianggang(), 
										p.isTianhe(), p.isDihe(), p.isTing(),p.isTianting(), changfeng, menfeng,shoupai.hupaiId,lianzhuang);
			fan.caculateFan();
			fans.add(fan);
		}
		Collections.sort(fans, new SortByFan());
		for(MaJiang_Fan f : fans)
		{
			System.out.println(f.getTotalFan());
		}
		MaJiang_Fan result  =  fans.get(0);
		p.setFan(result);
		System.out.println(result);
	}
	
	public List<Integer> getIntegerList(List<Object> list)
	{
		List<Integer> tmp =  new ArrayList<Integer>();
		for(int i  =0 ; i < list.size();i++)
		{
			tmp.add((Integer) list.get(i));
		}
		return tmp;
		
	}
	public List<List<Integer>> getGangList(List<Object> list)
	{
		 List<List<Integer>> tmp = new ArrayList<List<Integer>>();
		for(int i  = 0 ;i < list.size();i++)
		{
			List<Object> tmp1  = (List<Object>) list.get(i);
			List<Integer> tmp2  = this.getIntegerList(tmp1);
			tmp.add(tmp2);
		}
		return tmp;
	}
	public List<int[]> getchiList(List<Object> list)
	{
		 List<int[]> result  = new ArrayList<int[]>();
		for(int i = 0;i<list.size();i++)
		{
			List<Object> tmp0  = (List<Object>) list.get(i);
			int[] tmp1 = new int[tmp0.size()];
			for(int j = 0 ; j < tmp1.length;j++)
			{
				tmp1[j]  = (Integer) tmp0.get(j);
			}
			result.add(tmp1);
		}
		return result;
	}
	public boolean getbool(Object o)
	{
		boolean b = (Boolean) o;
		return b;
	}

}
