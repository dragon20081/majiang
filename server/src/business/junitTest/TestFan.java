package business.junitTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.MyArrays;

import server.command.Algorithm_Hu;
import server.command.GroupShoupai;
import server.command.MaJiang_Fan;
import server.command.SortByFan;
import server.mj.MgsPlayer;

public class TestFan {

	private  List<Integer> tingpaiId;
	private boolean justGang;
	private boolean lastPai;
	private boolean qianggang;
	private  boolean tianhe;
	private  boolean dihe;
	private boolean ting;
	private boolean tianting;
	private int changfeng;
	private int menfeng;
	private MgsPlayer p;
	
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {

		TestFan test  = new TestFan();
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("输入杠牌,直接回车跳过");
		String  gangStr = b.readLine();
		System.out.println("输入碰牌,直接回车跳过");
		String  pengStr = b.readLine();
		System.out.println("输入吃牌,直接回车跳过");
		String  chiStr = b.readLine();
		System.out.println("输入手牌:");
		String  shoupaiStr = b.readLine();
		System.out.println("输入胡牌ID:");
		String  hupaiStr = b.readLine();
		
		
		String[] strs = shoupaiStr.split(",");
		int[] pais  = new int[strs.length];
		int i = 0;
		for(i = 0 ; i  < strs.length;i++)
			pais[i]  = Integer.parseInt(strs[i]);
		Algorithm_Hu hu  = new Algorithm_Hu();
		hu.hupaiId =  Integer.parseInt(hupaiStr);
		hu.testHu(MyArrays.ArrasList(pais));
		test.caculateFan(test.p,hu.shoupai,false);
		
	}
	public void caculateFan(MgsPlayer p,Map<Integer,GroupShoupai> map,boolean isAnqidui)
	{
		List<MaJiang_Fan>  fans = new ArrayList<MaJiang_Fan>();
		Iterator<Integer> it  = map.keySet().iterator();
		while(it.hasNext())
		{
			int key  = it.next();
			GroupShoupai shoupai  = map.get(key);
			shoupai.patternShoupai();
			int lianzhuang = 10;
			MaJiang_Fan fan = new MaJiang_Fan();
			fan.init(p, shoupai.getAnke(), shoupai.getShun(), shoupai.getJiang(), tingpaiId, isAnqidui, justGang, lastPai, qianggang, tianhe, dihe, ting, tianting, changfeng, menfeng,shoupai.hupaiId,lianzhuang);
			fans.add(fan);
		}
		Collections.sort(fans, new SortByFan());
		for(MaJiang_Fan f : fans)
		{
			System.out.println(f.getTotalFan());
			Iterator<String> it2   = f.getFanInfos().keySet().iterator();
			while(it2.hasNext())
			{
				String name  = it2.next();
				System.out.println("--->name:" + f.getFanInfos().get(name));
			}
		}
	}
	public void initPalyer()
	{
		p  = new MgsPlayer();
		p.setPeng(new ArrayList<Integer>());
		p.setGang(new ArrayList<List<Integer>>());
		p.setChi(new ArrayList<int[]>());
	}

}
