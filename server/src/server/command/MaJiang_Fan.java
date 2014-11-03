package server.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import business.entity.MJ_DataFan;

import server.mj.Global;
import server.mj.MgsPlayer;

import common.Log;
import common.MyArrays;

/**
 * 
 * 
 * 所有牌都需要有顺序
 * @author xue
 */
public class MaJiang_Fan {
	
	
	private int totalFan = 0;
	private Map<String,Integer> fanInfos;
	
	
//	private int hupaiId;
	private List<Integer> tingpaiId;
	private boolean anqidui   =  false;
	private boolean lastPai   =  false;
	private boolean qianggang =  false;
	private boolean tianhe    =  false;
	private boolean dihe      =  false;
	private boolean ting      =  false;
	private boolean tianting  =  false;
	private boolean gangShangHua  = false;  //无
	private int changfeng     =  0;
	private int menfeng; 
	private MgsPlayer p;
	private List<Integer> anke;
	private List<List<Integer>> shun;
	private List<Integer> jiang;
	private List<String> outRule;
	private int huipaiId;
	private int numLIanzhuang = 0;
	
	
	public void init(MgsPlayer p,List<Integer> anke,List<List<Integer>> shun,List<Integer> jiang,List<Integer> tingpaiId,boolean anqidui,boolean justGang,boolean lastPai,boolean qianggang,boolean tianhe,boolean dihe,boolean ting,boolean tianting  ,int changfeng,int menfeng,int huId,int lian)
	{
		this.p      = p;
		this.anke   = anke;
		this.shun   = shun;
		this.jiang  = jiang;
		this.tingpaiId = tingpaiId;
		this.anqidui   = anqidui;
		this.gangShangHua  = justGang;
		this.lastPai   = lastPai;
		this.qianggang = qianggang;
		this.tianhe    = tianhe;
		this.dihe      = dihe;
		this.ting      = ting;
		this.tianting  = tianting;
		this.changfeng = changfeng;
		this.menfeng   = menfeng;
		outRule =  new ArrayList<String>();
		huipaiId = huId;
		this.numLIanzhuang = lian;
	}
	/**
	 * 1 总番数  加番类型  番数
	 * 参数  1 plyaer  2. 手牌胡牌后的分组 3 3 2  
	 *     *** 碰  杠都算刻  ***
	 * @return
	 */
	public void caculateFan()
	{
		totalFan  =  0;
		fanInfos  = new LinkedHashMap<String, Integer>();
		
		if(this.anqidui)
		{
				this.deal_anqidui();
				checkLianZhuang();
			return;
		}
		//  1 遍历极品
		boolean jipin  =  check_jipin();
		if(jipin)
		{
			checkLianZhuang();
			return;
		}
		caculagteByRuleId(new int[]{36,38,44,39,37,29,43,18,10,11,12,13,14,15,16,17,9,19,20,26,22,23,24,25,21,27,28,30,31,32,33});
		
		if(fanInfos.size() == 0)
		{
			setTotalFan(1);			
			fanInfos.put("和", 1);
		}
		checkLianZhuang();
	}
	/**
	 * 检查极品
	 */
	private boolean check_jipin()
	{
		this.caculateByName(new String[]{"天和","地和","十三幺","九莲宝灯","十八学士","绿一色"});
		
		if(fanInfos.containsKey("十八学士"))
		{
			this.caculateByName(new String[]{"清一色","字一色","混一色","大四喜","小四喜","大三元","小三元","杠上开花"});
		}
		if(this.fanInfos.size() != 0)return true;
//		this.caculateByName(new String[]{"清幺九","大四喜","大三元"});
//		if(this.fanInfos.size() != 0)
//		{
//			this.caculateByName(new String[]{"三暗刻","四暗刻"});
//			return true;
//		}
		
		return false;
	}
	/**
	 * 处理暗七对
	 */
	private void deal_anqidui()
	{
		//字一色  绿一色    清一色  混一色    听 天听  场风  门风   海底捞  强杠胡  天和 地和
		this.caculateByName(new String[]{"天和","地和","绿一色"});
		 if(fanInfos.size() > 0)return;
		this.caculateByName(new String[]{"暗七对","字一色","清一色","混一色","天听","听牌","场风刻","门风刻","海底捞月","抢杠胡","四归一"});
	}
	/**
	 * 绿一色
	 */
	private boolean check_lvyise(MgsPlayer p,List<Integer> anke,List<List<Integer>> shun,List<Integer> jiang)
	{
		List<Integer> paixu  =  MyArrays.asList(12,13,14,16,18,36);
		for(List<Integer> tmpList : p.getGang())
		{
			int pai  = tmpList.get(0);
			if(!paixu.contains(pai))return false;
		}
		for(int pai : p.getPeng())
		{
			if(!paixu.contains(pai))return false;
		}
		for(int[] arr : p.getChi())
		{
			int pai  = arr[0];
			if(pai != 12)return false;
		}
		for(int pai : anke)
		{
			if(!paixu.contains(pai))return false;
		}
		for(List<Integer> tmpList :shun)
		{
			int pai  = tmpList.get(0);
			if(pai != 12)return false;
		}
		if(jiang.size() >0)
		{
			int pai  = jiang.get(0);
			if(!paixu.contains(pai))return false;
		}
		for(Integer tmp :this.tingpaiId)
		{
			int pai  = tmp.intValue();
			if(!paixu.contains(pai))return false;
		}
		for(Integer tmp :p.getShoupai())
		{
			int pai  = tmp.intValue();
			if(!paixu.contains(pai))return false;
 		}
		
		return true;
	}
	/**
	 * 清幺九
	 */
	private boolean check_qinglaotou(MgsPlayer p ,List<Integer> anke,List<Integer> jiang)
	{
		if(p.getChi().size() > 0 || this.shun.size() > 0)return false;
		List<Integer>  yaojiu  = MyArrays.asList(1,9,11,19,21,29);
		for(List<Integer>  tmpList: p.getGang())  //杠
		{
			if(!yaojiu.contains(tmpList.get(0)))return false;
		}
		for( int pai : p.getPeng())  //碰
		{
			if(!yaojiu.contains(pai))return false;
		}
		for( int pai :anke)  //暗刻
		{
			if(!yaojiu.contains(pai))return false;
		}
		if(!yaojiu.contains(jiang.get(0)))return false;  //将牌
		return true;
	}
	
	
	/**
	 * 字一色
	 */
	private boolean check_ziyise(MgsPlayer p,List<Integer> anke,List<List<Integer>> shun,List<Integer> jiang)
	{
		if(p.getChi().size() >0)return false;
		if(shun.size() > 0 )    return false;
		if(jiang.size() >0 &&jiang.get(0) < 30)return false;
		for(List<Integer> tmplist : p.getGang())
		{
			if(tmplist.get(0) < 30)return false;
		}
		for(int pai : p.getPeng())
		{
			if(pai < 30) return false;
		}
		for(int pai : anke)
		{
			if(pai < 30)return false;
		}
		for(int pai : p.getShoupai())
		{
			if(pai < 30)return false;
		}
		this.outRule.add("碰碰和");
		
		return true;
	}
	/**
	 * 十八学士
	 */
	private boolean check_shibaxueshi(MgsPlayer p)
	{
		if(p.getGang().size() == 4)return true;
		return false;
	}
	/**
	 *  九莲宝灯
	 */
	private boolean check_jiulianbaodeng(MgsPlayer  p)
	{
		if(p.getShoupai().size() != 14)return false;
		List<Integer> list  = MyArrays.asList(1,1,1,2,3,4,5,6,7,8,9,9,9);
		int[] paixu =  MyArrays.ListToArr(p.getShoupai());
		if(paixu[paixu.length-1] -  paixu[0] > 8)return false;
		int i = 0;
		for(i = 0 ; i < paixu.length;i++ )
		{
			int pai = paixu[i]%10;
			if(list.contains(pai))list.remove((Integer)pai);
		}
		if(list.size()  != 0)return false;
		
		if(this.huipaiId%10 == 1 || this.huipaiId%10 == 9)
		{
			int count = 0;
			for(i = 0 ; i < p.getShoupai().size();i++ ) 
			{
				if(p.getShoupai().get(i) == huipaiId)
					count++;
			}
			if(count != 4)return false;
		}
		return true;
	}
	
	/**
	 * 十三幺
	 */
	private boolean check_shisanyao(MgsPlayer p)
	{
		List<Integer> list = MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		List<Integer> list2 = MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		int shuang  = 0;
		for(int pai : p.getShoupai())
		{
			if(list.contains(pai))
			{
				list.remove((Integer)pai);
			}else
			{
				shuang  = pai;
			}
		}
		if(list.size() > 0)return false;
		if(!list2.contains(shuang))return false;
		return true;
	}
	/**
	 * 四暗刻
	 */
	private boolean check_sianke(MgsPlayer p,List<Integer> anke)
	{
		int count = 0;
		for(List<Integer> tmpList : p.getGang())
		{
			System.out.println("四暗刻:" + tmpList.size() +":" + tmpList.get(1));
			if(tmpList.get(1)!= 0)count++;
		}
		count  += anke.size();
		if(count == 4)return true;
		return false;
	}
	/**
	 * 大四喜
	 */
	private boolean check_dasixi(MgsPlayer p,List<Integer> anke)
	{
		List<Integer> list = MyArrays.asList(31,32,33,34);
		for(int pai : p.getPeng())
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		for(List<Integer> tmpList : p.getGang())
		{
			if(list.contains(tmpList.get(0)))list.remove((Integer)tmpList.get(0));
		}
		for(int pai : anke)
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		if(list.size() == 0)return true;
		return false;
	}
	
	/**
	 * 小四喜
	 */
	private boolean check_xiaosixi(MgsPlayer p,List<Integer> anke,List<Integer> jiang)
	{
		if(fanInfos.containsKey("大四喜"))
			return false;
		List<Integer> list = MyArrays.asList(31,32,33,34);
		if(list.contains(jiang.get(0)))
		{
			list.remove((Integer)jiang.get(0));
		}else
		{
			return false;
		}
		for(int pai : p.getPeng())
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		for(List<Integer> tmpList : p.getGang())
		{
			if(list.contains(tmpList.get(0)))list.remove((Integer)tmpList.get(0));
		}
		for(int pai : anke)
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		if(list.size() == 0)return true;
		return false;
	}
	/**
	 * 大三元
	 */
	private boolean check_dasanyuan(MgsPlayer p,List<Integer> anke)
	{
		List<Integer> list = MyArrays.asList(35,36,37);
		for(int pai : p.getPeng())
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		for(List<Integer> tmpList : p.getGang())
		{
			if(list.contains(tmpList.get(0)))list.remove((Integer)tmpList.get(0));
		}
		for(int pai : anke)
		{
			if(list.contains(pai))list.remove((Integer)pai);
		}
		if(list.size() == 0)
		{
			this.outRule.add("三元刻");
			return true;
		}
		return false;
	}
	
	/**
	 * 地和
	 */
	private boolean check_dihe()
	{
		if(this.dihe)return true;
		return false;
	}
	/**
	 * 天和
	 */
	private boolean check_tianhe()
	{
		if(this.tianhe)return true;
		return false;
	}
	/**
	 * 抢杠胡
	 */
	private boolean check_qiangganghu()
	{
		if(qianggang)return true;
		return false;
	}
	/**
	 * 海底捞月
	 */
	private boolean check_haidilao()
	{
		if(this.lastPai)return true;
		return false;
	}
	
	/**
	 * 杠上开花
	 */
	private boolean check_gangshangkaihua()
	{
		//由外部确定
		if(this.gangShangHua)return true;
		return false;
	}
	
	/**
	 * 一气贯通
	 */
	private boolean check_yiqiguantong(MgsPlayer p,List<List<Integer>> shun)
	{
		List<List<Integer>> one   = new ArrayList<List<Integer>>();
		List<List<Integer>> four  = new ArrayList<List<Integer>>();
		List<List<Integer>> seven = new ArrayList<List<Integer>>();
		List<int[]> chi  = p.getChi();
		for(int[] arr : chi)
		{
			if(arr[0]%10 == 1)one.add(MyArrays.asList(arr));
			if(arr[0]%10 == 4)four.add(MyArrays.asList(arr));
			if(arr[0]%10 == 7)seven.add(MyArrays.asList(arr));
		}
		for(List<Integer> tmpList : shun)
		{
			if(tmpList.get(0)%10 == 1)one.add(tmpList);
			if(tmpList.get(0)%10 == 4)four.add(tmpList);
			if(tmpList.get(0)%10 == 7)seven.add(tmpList);
		}
		if(one.size() ==0 || four.size() == 0 || seven.size() == 0)return false;
		if((int)(one.get(0).get(0)/10) == (int)(four.get(0).get(0)/10) &&(int)(four.get(0).get(0)/10) == (int)(seven.get(0).get(0)/10))
		{
			return true;
		}else
		{
			if(one.size()>1)one.remove(0);
			if(four.size()>1)four.remove(0);
			if(seven.size()>1)seven.remove(0);
		}
		if((int)(one.get(0).get(0)/10) == (int)(four.get(0).get(0)/10) &&(int)(four.get(0).get(0)/10) == (int)(seven.get(0).get(0)/10))
			return true;
		
		return false;
	}
	/**
	 * 小三元
	 */
	private boolean check_xiaosanyuan(MgsPlayer p,List<Integer> anke,List<Integer> jiang)
	{
		if(fanInfos.containsKey("大三元"))
			return false;
		List<Integer> list  =  MyArrays.asList(35,36,37);
		if(!list.contains(jiang.get(0)))return false;
		int kinds =0;
		for(List<Integer> list1 : p.getGang())
		{
			int pai  = list1.get(0);
			if(list.contains(pai))kinds++;
		}
		for(int pai : p.getPeng())
		{
			if(list.contains(pai))kinds++;
		}
		for(int pai : anke)
		{
			if(list.contains(pai))kinds++;
		}
		if(kinds == 2)
		{
			this.outRule.add("三元刻");
			return true;
		}
		
		return false;
	}
	/**
	 * 清一色
	 */
	private boolean check_qingyise(MgsPlayer  p)
	{
		int[]  kinds = new int[]{0,0,0,0};
		for(List<Integer> list : p.getGang() )
		{
			int xu = list.get(0)/10;kinds[xu] +=4;
		}
		for(int pai : p.getPeng() )
		{
			int xu = pai/10;kinds[xu] +=3;
		}
		for(int[] pai : p.getChi() )
		{
			int xu = pai[0]/10;kinds[xu] +=3;
		}
		for(int i = 0 ; i < p.getShoupai().size();i++)
		{
			int xu = p.getShoupai().get(i)/10;kinds[xu] +=1;
		}
		if(kinds[3] != 0)return false;
		 if(kinds[0] == 0 && kinds[1] == 0)return true;
		 if(kinds[0] == 0 && kinds[2] == 0)return true; 
		 if(kinds[1] == 0 && kinds[2] == 0)return true;
		return false;
	}
	/**
	 * 混一色
	 */
	private boolean check_hunyise(MgsPlayer p)
	{
		if(fanInfos.containsKey("混幺九"))
			return false;
		int[]  kinds = new int[]{0,0,0,0};
		for(List<Integer> list : p.getGang() )
		{
			int xu = list.get(0)/10;kinds[xu] +=4;
		}
		for(int pai : p.getPeng() )
		{
			int xu = pai/10;kinds[xu] +=3;
		}
		for(int[] pai : p.getChi() )
		{
			int xu = pai[0]/10;kinds[xu] +=3;
		}
		for(int i = 0 ; i < p.getShoupai().size();i++)
		{
			int xu = p.getShoupai().get(i)/10;kinds[xu] +=1;
		}
		if(kinds[3] == 0)return false;
		 if(kinds[0] == 0 && kinds[1] == 0 && kinds[2] != 0)return true;
		 if(kinds[0] == 0 && kinds[2] == 0 && kinds[1] != 0)return true;
		 if(kinds[1] == 0 && kinds[2] == 0 && kinds[0] != 0)return true;
		return false;
	}
	/**
	 * 混幺九
	 */
	private boolean check_hunlaotou(MgsPlayer p,List<Integer> anke,List<List<Integer>> shun,List<Integer> jiang)
	{
		boolean zhi = false;
		boolean yj  = false;
		List<Integer>  yaojiu  = MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		if(p.getChi().size()!=0 || shun.size() !=0)return false;
		for(List<Integer>  tmpList: p.getGang())  //杠
		{
			if(!yaojiu.contains(tmpList.get(0)))return false;
			if(tmpList.get(0) > 30)zhi  = true;
			if(tmpList.get(0) < 30)yj   = true;
		}
		for( int pai : p.getPeng())  //碰
		{
			if(!yaojiu.contains(pai))return false;
			if(pai > 30)zhi  = true;
			if(pai < 30)yj   = true;
		}
		for( int pai :anke)  //暗刻
		{
			if(!yaojiu.contains(pai))return false;
			if(pai > 30)zhi  = true;
			if(pai < 30)yj   = true;
		}
		if(!yaojiu.contains(jiang.get(0)))return false;  //将牌
		if(jiang.get(0) > 30)zhi  = true;
		if(jiang.get(0) < 30)yj   = true;
		
		if(zhi && yj )return true;
		return false;
	}
	
	/**
	 * 全带幺
	 */
	private boolean check_quandaiyao(MgsPlayer p,List<Integer> anke,List<List<Integer>> shun,List<Integer> jiang)
	{
		if(fanInfos.containsKey("混幺九"))
			return false;
		boolean yj = false;
		List<Integer>  yaojiu  = MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		for(List<Integer>  tmpList: p.getGang())  //杠
		{
			if(!yaojiu.contains(tmpList.get(0)))return false;
			if(tmpList.get(0) < 30)yj = true;
		}
		for( int pai : p.getPeng())  //碰
		{
			if(!yaojiu.contains(pai))return false;
			if(pai < 30)yj = true;
		}
		for( int pai :anke)  //暗刻
		{
			if(!yaojiu.contains(pai))return false;
			if(pai < 30)yj = true;
		}
		if(!yaojiu.contains(jiang.get(0)))return false;  //将牌
		if(jiang.get(0) < 30)yj = true;
		
		for(List<Integer> tmpList : shun)
		{
			if(tmpList.get(0)%10 != 1 && tmpList.get(0)%10 !=7)return false;
			if(tmpList.get(0)< 30)yj = true;
		}
		for(int[] tmpList : p.getChi())
		{
			if(tmpList[0]%10 !=1 && tmpList[0]%10 !=7)return false;
			if(tmpList[0]< 30)yj = true;
		}
		if(yj)return true;
		return false;
	}
	/**
	 * 三暗刻
	 */
	private boolean chekc_sananke(MgsPlayer p,List<Integer> anke )
	{
		int ke  = 0;
		List<List<Integer>> gang  = p.getGang();
		for( List<Integer> tmpList: gang)
		{
			System.out.println("三暗刻:" + tmpList.size() +":" + tmpList.get(1));
			if(tmpList.get(1) != 0)ke++;
		}
		ke  +=anke.size();
		if(ke ==3)return true;
		return false;
	}
	/**
	 * 三色同刻
	 */
	private boolean check_sansetongke(MgsPlayer p,List<Integer> anke )
	{
		List<Integer> peng = p.getPeng();
		int[] arr   =  new int[peng.size() + anke.size()];
		int index = 0;
		List<Integer>  chunpai;
			for(int pai : peng)
				{arr[index]  =pai;index++;}
			for(int pai : anke)
				{arr[index]  = pai;index++;}
			if(arr.length < 3)return false;
			Arrays.sort(arr);
			chunpai =  MyArrays.ArrasList(arr);
			for(int i  = 0 ; i < 2;i++)
			{
				int pai1=  chunpai.get(i);
				if(chunpai.contains(pai1+10) &&chunpai.contains(pai1+20))return true;
			}
		return false;
	}
	/**
	 * 三色同顺
	 */
	private boolean check_sansetongshun(List<int[]> chi,List<List<Integer>> shun)
	{
		int[] arr   =  new int[chi.size() + shun.size()];
		int index = 0;
		List<Integer>  chunpai;
			for(int[] tmpArr : chi)
				{arr[index]  = tmpArr[0];index++;}
			for(List<Integer> tmplist : shun)
				{arr[index]  = tmplist.get(0);index++;}
			if(arr.length < 3)return false;
			Arrays.sort(arr);
			chunpai =  MyArrays.ArrasList(arr);
			for(int i  = 0 ; i < 2;i++)
			{
				int pai1=  chunpai.get(i);
				if(chunpai.contains(pai1+10) &&chunpai.contains(pai1+20))return true;
			}
		return false;
	}
	/**
	 *  碰碰和
	 */
	private boolean check_pengpenghe(MgsPlayer p,List<List<Integer>> shun)
	{
		if(fanInfos.containsKey("四暗刻"))
			return false;
		if(fanInfos.containsKey("混幺九"))
			return false;
		if(fanInfos.containsKey("字一色"))
			return false;
		if(fanInfos.containsKey("大三元"))
			return false;
		if(fanInfos.containsKey("大四喜"))
			return false;
		if(fanInfos.containsKey("清幺九"))
			return false;
		
		if(p.getChi().isEmpty() && shun.isEmpty())return true;
		return false;
	}
	/**
	 *  暗七对
	 */
	private boolean check_anqidui()
	{
		if(this.anqidui)return true;
		return false;
	}
	/**
	 *  一般高
	 */
	private boolean check_yibangao(MgsPlayer p,List<List<Integer>> shun)
	{
//		boolean menqing  = check_menqianqing(p);
//		if(!menqing)return false;
		List<Integer> tmpshun  = new ArrayList<Integer>();
		for(List<Integer> tmp :shun)
			tmpshun.add(tmp.get(0));
		for(int[] tmp :p.getChi())
			tmpshun.add(tmp[0]);
		while(tmpshun.size() >0)
		{
			int pai =  tmpshun.get(0);tmpshun.remove(0);
			if(tmpshun.contains(pai))return true;
		}
		return false;
	}
	/**
	 *  门前清
	 */
	private boolean check_menqianqing(MgsPlayer p)
	{
		if(fanInfos.containsKey("四暗刻"))
			return false;
		if(p.getGang().size() == 0 && p.getPeng().isEmpty() &&p.getChi().isEmpty())return true;
		return false;
	}
	/**
	 * 断幺九
	 */
	private boolean check_duanyaojiu(MgsPlayer p)
	{
		List<Integer> yaojiu = MyArrays.asList(1,9,11,19,21,29);
		for( List<Integer> tmpList: p.getGang())
		{
			int pai  = tmpList.get(0);
			if(yaojiu.contains(pai))return false;
			if(pai > 30)return false;
		}
		for(int pai: p.getPeng())
		{
			if(pai > 30)return false;			
			if(yaojiu.contains(pai))return false;
		}
		for(int pai: p.getShoupai())
		{
			if(pai > 30)return false;			
			if(yaojiu.contains(pai))return false;
		}
		for(int i  =0 ;i  < p.getChi().size();i++)
		{
			int[] chi  = p.getChi().get(i);
			for(int j = 0 ; j <chi.length;j++)
			{
				if(yaojiu.contains(chi[j]))return false;
			}
		}
		return true;
	}
	/**
	 * 平和
	 */
	private boolean check_pinghe( int menfeng, int changfeng,int jiangpai,List<List<Integer>> shun)
	{
		List<Integer> jiang = MyArrays.asList(menfeng,changfeng,35,36,37);
		if(jiang.contains(jiangpai))return false;  			//将牌不能为 门风 场风 中发白
		if(shun.size() < 4) 		return false; 		    //胡牌后必须所有都是顺子
		if(this.tingpaiId.size() == 2 && (this.tingpaiId.get(0)%10>1) &&this.tingpaiId.get(0)+1 == this.tingpaiId.get(1))
									return true;			//停牌为搭张 
		return false;
	}
	/**
	 *  箭刻  31 - 34 东南西北    35 36 37 中发白
	 */
	private int check_jianke(List<Integer> peng, List<Integer> anke)
	{
		if(fanInfos.containsKey("小三元"))
			return 0;
		if(fanInfos.containsKey("大三元"))
			return 0;
		int fan =  0;
		List<Integer> list  = MyArrays.asList(35,36,37); //[46,47,48]
		int[] arr = new int[]{0,0,0};
		for(int pai:peng)
		{
			if(list.contains(pai))fan++;
			switch(pai)
			{
				case  35: 	arr[0] = 1;
					 break;
				case  36:	arr[1] = 1;
					 break;
				case  37:	arr[2] = 1;
					break;
			}
		}
		for(int pai:anke)
		{
			if(list.contains(pai))fan++;
			Log.log("check_jianke三元刻:" + pai);
			switch(pai)
			{
				case  35: 	arr[0] = 1;
				break;
				case  36:	arr[1] = 1;
					 break;
				case  37:	arr[2] = 1;
					break;
			}
		}
		for(List<Integer> tmp: this.p.getGang())
		{
			int pai = tmp.get(0);
			if(list.contains(pai))fan++;
			switch(pai)
			{
				case  35: 	arr[0] = 1;
				break;
				case  36:	arr[1] = 1;
					 break;
				case  37:	arr[2] = 1;
					break;
			}
		}
		
		addSanyuanKeFan(arr);
		return fan;
	}
	private void addSanyuanKeFan(int[] pais)
	{
		if(pais[0] != 0)AddFanInfo("三元刻_中"   ,1); 
		if(pais[1] != 0)AddFanInfo("三元刻_发"   ,1); 
		if(pais[2] != 0)AddFanInfo("三元刻_白"   ,1); 
	}
	/**\
	 * 场风刻,和门风刻
	 */
	private boolean check_fengke(int changfeng , List<Integer> peng, List<Integer> anke,List<List<Integer>> gang)
	{
		if(fanInfos.containsKey("大四喜"))
			return false;
		Log.log("check_fengke :"+ changfeng);
		changfeng  +=30;
		for(Integer pai : peng)
		{
			int tmp  = pai.intValue();
			if(changfeng == tmp)
			{
				Log.log("check_fengke :"+ changfeng);
				return  true;
			}
		}
		for(Integer pai : anke)
		{
			int tmp  = pai.intValue();
			if(changfeng == tmp)
			{
				Log.log("check_fengke :"+ changfeng);
				return  true;
			}
		}
		for(List<Integer> tmpList : gang)
		{
			int tmp  = tmpList.get(0);
			if(changfeng == tmp)
			{
				Log.log("check_fengke :"+ changfeng);
				return  true;
			}
		}
		return false;
	}
	
	/**
	 * 四归一
	 * @param peng
	 * @param shoupai
	 * @return
	 */
	private int check_siguiyi(List<Integer> peng,  List<Integer> shoupai,List<int[]> chi)
	{
		int i = 0;
		int j = 0;
		if(this.anqidui)
		{
			int fan_sjy = 0;
			List<Integer> danpai  = new ArrayList<Integer>();
			for(i = 0; i < shoupai.size();i++)
			{
				danpai.add(shoupai.get(i));
			}
			List<Integer> fanlist = new ArrayList<Integer>();
			List<Integer> hasCountList  =  new ArrayList<Integer>();
			for(i = 0 ; i < danpai.size();i++)
			{
				int pai  = danpai.get(i);
				if(hasCountList.contains(pai))continue;
				hasCountList.add(pai);
				int count = 0;
				for(j = 0; j < danpai.size();j++)
				{
					if(pai == danpai.get(j))count++;
				}
				if(count == 4)
				{
					if(!fanlist.contains(pai))
					fan_sjy++; 
				}
			}
			return fan_sjy;
		}
		List<Integer> danpai  = new ArrayList<Integer>();
		int fan_sjy = 0;
		List<Integer> fanlist = new ArrayList<Integer>();
		for(i = 0;i < chi.size();i++)
		{
			danpai.add(chi.get(i)[0]);
			danpai.add(chi.get(i)[1]);
			danpai.add(chi.get(i)[2]);
		}
		for(i = 0;i < this.shun.size();i++)
		{
			danpai.add(shun.get(i).get(0));
			danpai.add(shun.get(i).get(1));
			danpai.add(shun.get(i).get(2));
		}
		for(i = 0;i < peng.size();i++)
		{
			int pai = peng.get(i);
			danpai.add(pai);	danpai.add(pai);	danpai.add(pai);
		}
		for(i = 0;i < this.anke.size();i++)
		{
			int pai = anke.get(i);
			danpai.add(pai);	danpai.add(pai);	danpai.add(pai);
		}
		danpai.add(this.jiang.get(0));		danpai.add(this.jiang.get(0));
		
		List<Integer> hasCountList  =  new ArrayList<Integer>();
		for(i = 0 ; i < danpai.size();i++)
		{
			int pai  = danpai.get(i);
			if(hasCountList.contains(pai))continue;
			hasCountList.add(pai);
			int count = 0;
			for(j = 0; j < danpai.size();j++)
			{
				if(pai == danpai.get(j))count++;
			}
			if(count == 4)
			{
				if(!fanlist.contains(pai))
				fan_sjy++; 
			}
		}
		return fan_sjy;
//		for(i = 0;i < peng.size();i++)
//		{
//			int pai = peng.get(i);
//			if(danpai.contains(pai))
//			{
//				fanlist.add(pai);
//				fan_sjy++;
//			}
//		}
//		for(i = 0;i < this.anke.size();i++)
//		{
//			int pai = anke.get(i);
//			if(danpai.contains(pai))
//			{
//				fanlist.add(pai);
//				fan_sjy++;
//			}
//		}
		//检查手牌中是否有某个牌有4张
//		List<Integer> hasCountList  =  new ArrayList<Integer>();
//		for(i = 0 ; i < shoupai.size();i++)
//		{
//			int pai  = shoupai.get(i);
//			if(hasCountList.contains(pai))continue;
//			int count = 0;
//			for(j = 0; j < shoupai.size();j++)
//			{
//				if(pai == shoupai.get(j))count++;
//			}
//			if(count == 4)
//			{
//				hasCountList.add(pai);
//				if(!fanlist.contains(pai))
//				fan_sjy++; 
//			}
//		}
//		return fan_sjy;
	}
	
	
/**
 * @param str  得番名字
 * @param fan  番数
 */
	public void AddFanInfo(String str, int fan)
	{
		fanInfos.put(str, fan);
	}
	public int getTotalFan()
	{
		this.totalFan = 0;
		Iterator<Integer>  it =  fanInfos.values().iterator();
		while(it.hasNext())
		{
			int fan  =  it.next();
			this.totalFan += fan;
		}
		return this.totalFan;
	}
	public void setTotalFan(int totalFan) {
		this.totalFan = totalFan;
	}
	public Map<String, Integer> getFanInfos() {
		return fanInfos;
	}
	public void setFanInfos(Map<String, Integer> fanInfos) {
		this.fanInfos = fanInfos;
	}
	
	public void caculateByName(String[] arr)
	{
		for(int i = 0; i < arr.length;i++)
		{
			String key  = arr[i];
			int ruleId = this.getRuleIdByName(key);

			if(this.outRule.contains(key)) continue;
			
			if(ruleId != 0)caculateChild(ruleId);
		}
	}
	public void caculagteByRuleId(int[] arr)
	{
		List<Integer> outarr = getOutId();
		
		for(int i = 0; i < arr.length;i++)
		{
			int key  = arr[i];
			if(outarr.contains((Integer)key))continue;
			caculateChild(key);
		}
	}
	private void caculateChild(int id)
	{
		
		Map<String,MJ_DataFan> fans = Global.fans;
		
		switch(id)
		{
			case 	9	:			if(ting && !tianting)							AddFanInfo("听牌"  ,1);   		break;      //听牌
			case 	10	:			if(tianting)									AddFanInfo("天听"  ,4);  		break;  	//天听
			case 	11	:			int gang  = p.getGang().size();	if(gang > 0)   	AddFanInfo("杠"	   ,gang*2);  	break;	    // 杠
			case 	12	:			int fan_sgy = check_siguiyi(p.getPeng(),p.getShoupai(),p.getChi());
									if(fan_sgy > 0)  								AddFanInfo("四归一",fan_sgy); 	break;        //四归一
			case 	13	:			if(check_fengke(changfeng,p.getPeng(),anke,p.getGang()))	AddFanInfo("场风刻",1);      	break;	 	  //场风刻
			case 	14	:			if(check_fengke(menfeng,p.getPeng(),anke,p.getGang()))      AddFanInfo("门风刻",1);    		break;  	  //门风刻
			case 	15	:			check_jianke(p.getPeng(),anke);													break;	       //三元刻
			case 	16	:			if(check_pinghe(menfeng,changfeng,jiang.get(0),shun))	AddFanInfo("平和"  , 1);      		break;			 // 平和
			case 	17	:			if(check_duanyaojiu(p))								AddFanInfo("断幺九",1);     				break;			 // 断幺九
			case 	18	:			if(check_menqianqing(p))							AddFanInfo("门前清",1);     				break;			 // 门前清
			case 	19	:			if(check_yibangao(p,shun))							AddFanInfo("一般高",1);     				break;			 // 一般高
			case 	20	:			if(check_anqidui())									AddFanInfo("暗七对",4);     				break;			 // 暗七对
			case 	21	:			if(check_pengpenghe(p,shun))						AddFanInfo("碰碰和",2);    				break;			 // 碰碰和
			case 	22	:			if(check_sansetongshun(p.getChi(),shun))			AddFanInfo("三色同顺",2);    			break;			 // 三色同顺
			case 	23	:			if(check_sansetongke(p,anke))						AddFanInfo("三色同刻",2);    			break;			 // 三色同刻
			case 	24	:			if(chekc_sananke(p,anke))							AddFanInfo("三暗刻"   ,4);    			break;						 // 三暗刻
			case 	25	:			if(check_quandaiyao(p,anke,shun,jiang))				AddFanInfo("全带幺"   ,3);    			break;						 // 全带幺
			case 	26	:			if(check_hunlaotou(p,anke,shun,jiang))				AddFanInfo("混幺九"   ,8);    			break;						 // 混幺九
			case 	27	:			if(check_hunyise(p))								AddFanInfo("混一色"   ,2);    			break;						 // 混一色
			case 	28	:			if(check_qingyise(p))								AddFanInfo("清一色"   ,6);    			break;						 // 清一色
			case 	29	:			if(check_xiaosanyuan(p,anke,jiang))	        		AddFanInfo("小三元"   ,8);    			break;						 // 小三元
			case 	30	:			if(check_yiqiguantong(p,shun))						AddFanInfo("一气贯通" ,2);    			break;					     // 一气贯通
			case 	31	:			if(check_gangshangkaihua())							AddFanInfo("杠上开花" ,2);    			break;					     // 杠上开发	
			case 	32	:			if(check_haidilao())								AddFanInfo("海底捞月" ,1);    			break;					     // 海底捞月	
			case 	33	:			if(check_qiangganghu())								AddFanInfo("抢杠胡"   ,1);    			break;				         // 抢杠胡	
			case 	34	:			if(this.check_tianhe())								AddFanInfo("天和",24);    	 			break;				     // 天和
			case 	35	:			if(this.check_dihe())								AddFanInfo("地和",24);    	 			break;				     // 地和
			case 	36	:			if(this.check_dasanyuan(p,anke))					AddFanInfo("大三元",24);    	    		break;
			case 	37	:			if(this.check_xiaosixi(p, anke, jiang))				AddFanInfo("小四喜", 8);					break; 
			case 	38	:			if(this.check_dasixi(p,anke))						AddFanInfo("大四喜",24);    	 			break;		        // 大四喜
			case 	39	:			if(this.check_sianke(p, anke))						AddFanInfo("四暗刻",10);					break;
			case 	40	:			if(this.check_shisanyao(p))							AddFanInfo("十三幺",40);    	 			break;			     // 十三幺break;
			case 	41	:			if(this.check_jiulianbaodeng(p))					AddFanInfo("九宝莲灯",40);   			break;	 	 		 // 九宝莲灯break;
			case 	42	:			if(this.check_shibaxueshi(p))						AddFanInfo("十八学士",40);   			break;	 			 // 十八学士 break;
			case 	43	:	 		if(this.check_ziyise(p, anke, shun, jiang))			AddFanInfo("字一色", 12);					break;    
			case 	44	:			if(this.check_qinglaotou(p,anke,jiang))				AddFanInfo("清幺九",24);    	 	 		break;		     	// 清幺九   检查    三暗刻    四暗刻
			case 	45	:			if(this.check_lvyise(p, anke, shun, jiang))			AddFanInfo("绿一色",40);     			break;				 // 绿一色
//			case 	50	:		
//				
//				int lianfan = checkLianZhuang();
//				if(lianfan > 0)			AddFanInfo("连庄",lianfan);     			break;				 // 绿一色
			//case 	50	:			if(this.check_queyimen(p))							AddFanInfo("缺一门",1);     			break;
		}
	}
	public void checkLianZhuang()
	{
		int num = numLIanzhuang;
		int fan = num;
		if(fan > 0)			AddFanInfo("连庄",fan);     		
	}
	public boolean check_queyimen(MgsPlayer p)
	{
		int[] typearr = new int[]{0,0,0,0};
		//碰  吃  杠  手牌
		int i = 0;
		for(i = 0;i < p.getPeng().size();i++)
		{
			int pai  = p.getPeng().get(i);
			typearr[pai/10]++;
		}
		for(i = 0;i < p.getChi().size();i++)
		{
			int pai  = p.getChi().get(i)[0];
			typearr[pai/10]++;
		}
		for(i = 0;i < p.getGang().size();i++)
		{
			int pai  = p.getGang().get(i).get(0);
			typearr[pai/10]++;
		}
		for(i = 0;i < p.getShoupai().size();i++)
		{
			int pai  = p.getShoupai().get(i);
			typearr[pai/10]++;
		}
		int count = 0;
		if(typearr[0] > 0)count++;
		if(typearr[1] > 0)count++;
		if(typearr[2] > 0)count++;
		if(count == 2)return true;
		return false;
	}
	
	public int getRuleIdByName(String name)
	{
		int ruleId = 0;
		if(ruleMap.containsKey(name))
		{
			ruleId  = ruleMap.get(name);
		}else
		{
			try {
				throw new Exception("wrong rule name");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ruleId;
	}
	private static Map<String ,Integer> ruleMap;
	static{
		
		ruleMap  = new HashMap<String ,Integer>();
		ruleMap.put("听牌",	9	);
		ruleMap.put("天听",	10	);
		ruleMap.put("杠",	11	);
		ruleMap.put("四归一",	12	);
		ruleMap.put("场风刻",	13	);
		ruleMap.put("门风刻",	14	);
		ruleMap.put("三元刻",	15	);
		ruleMap.put("平和",	16	);
		ruleMap.put("断幺九",	17	);
		ruleMap.put("门前清",	18	);
		ruleMap.put("一般高",	19	);
		ruleMap.put("暗七对",	20	);
		ruleMap.put("碰碰和",	21	);
		ruleMap.put("三色同顺",	22	);
		ruleMap.put("三色同刻",	23	);
		ruleMap.put("三暗刻",	24	);
		ruleMap.put("全带幺",	25	);
		ruleMap.put("混幺九",	26	);
		ruleMap.put("混一色",	27	);
		ruleMap.put("清一色",	28	);
		ruleMap.put("小三元",	29	);
		ruleMap.put("一气贯通",	30	);
		ruleMap.put("杠上开花",	31	);
		ruleMap.put("海底捞月",	32	);
		ruleMap.put("抢杠胡",	33	);
		ruleMap.put("天和",	34	);
		ruleMap.put("地和",	35	);
		ruleMap.put("大三元",	36	);
		ruleMap.put("小四喜",	37	);
		ruleMap.put("大四喜",	38	);
		ruleMap.put("四暗刻",	39	);
		ruleMap.put("十三幺",	40	);
		ruleMap.put("九莲宝灯",	41	);
		ruleMap.put("十八学士",	42	);
		ruleMap.put("字一色",	43	);
		ruleMap.put("清幺九",	44	);
		ruleMap.put("绿一色",	45	);
		ruleMap.put("连庄",	50	);
		

	}
	private List<Integer> getOutId()
	{
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.outRule.size();i++)
		{
			list.add(Integer.parseInt(this.outRule.get(i)));
		}
		return list;
	}
	@Override
	public String toString() {
		return "MaJiang_Fan [fanInfos=" + fanInfos + "]";
	}
	
}
