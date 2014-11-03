package business.junitTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Before;
import org.junit.Test;

import server.command.Algorithm_Hu;
import server.command.GroupShoupai;
import server.command.MaJiang_Fan;
import server.command.MajiangPai;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.SortByFan;
import server.command.SortMaJiang;
import server.command.cmd.CCMD11005;
import server.command.cmd.CCMD11102;
import server.http.Html_360Notice;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.Business;
import business.CountDao;
import business.conut.Sts_JuqingDay;

import common.MyArrays;

public class UnitTest{
	
	private Business b1;
	
	private void trace(int[] arr)
	{
		System.out.print("[");
		for(int i  =0 ; i < arr.length;i++)
		{
			System.out.print(arr[i]);
			if(i < arr.length-1)
				System.out.print(",");
		}
		System.out.print("];");
	}
	private void trace(Object[] arr)
	{
		System.out.print("[");
		for(int i  =0 ; i < arr.length;i++)
		{
			System.out.print(arr[i]);
			if(i < arr.length-1)
				System.out.print(",");
		}
		System.out.print("];");
	}
	@Before
	public void TestRegister()
	{
		b1 = new Business();
	}
	
	@Test
	public void testZhanJi()
	{
		List<Integer> list = b1.queryZhanJi("豆腐干");
		trace(list.toString());
	}
	
	@Test
	public void TestLogin_auto()
	{
	}
	public void trace(Object str)
	{
		System.out.println(str);
	}
	@Test
	public void testCollection()
	{
		List<Integer> list  = new ArrayList<Integer>();
		list.add(3);list.add(5);list.add(7);list.add(9);
		trace(list.toString());
		list.remove((Integer)5);
		trace(list.toString());
		trace("----------------------------------");
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		map.put(6, 4);map.put(8, 4);map.put(10, 4);map.put(12, 4);
		trace(map.toString());
		map.put(6, 3);
		trace(map.toString());
	}
	@Test
	public void testMapValue()
	{
		 int[] a =  new int[4];
		 trace(a[0] +":"+ a[1] +":"+ a[2] +":"+ a[3]);
	}
	@Test
	public void testComm()
	{
		List<Integer> list  = new ArrayList<Integer>();
		list.add(11);	list.add(7);	list.add(71);	list.add(11);	list.add(10);
		Integer[] arr = list.toArray(new Integer[]{});
		Arrays.sort(arr);
		trace(list.toArray());
		boolean b = list.remove((Integer)11);
		trace(b+"");
		trace(list.toArray());
	}
	@Test
	public void testList()
	{
		List<Integer> list  = new ArrayList<Integer>();
		list.add(11);	list.add(7);	list.add(71);	list.add(10);
		List<Integer> list2  = new ArrayList<Integer>();list2.add(11);list2.add(11);list2.add(11);
		boolean  b =  list.containsAll(list2);
		trace(b+"");
	}
	@Test 
	public void TestHUpai()
	{

		
//		List<Integer> list =  MyArrays.asList(35, 35, 9, 5, 5, 4, 5,  3, 35, 4, 6, 9, 7);
//		List<Integer> list =  MyArrays.asList(3,4,4,5,5,5,6,7,9,9,35,35,35);
		List<Integer> list =  MyArrays.asList(2, 4, 3, 6, 3, 3, 5, 2, 36, 1, 36, 1, 6);
		//  3  4  4 5  5        5  6  7        9  9          35 35   35
		Algorithm_Hu  hupai = new Algorithm_Hu();
		hupai.hupaiId = 6;
		boolean b = hupai.testHu(list);
		trace(b+"");
	}
	
	public static void main(String[] args)
	{
		MD5Util md5 = new MD5Util();
		String tmp = md5.string2MD5("1234567890abcdefghijklmnopqrstuv#101");
		System.out.println(tmp);
	}

   public static String getObject() throws Exception {
	   
	   int sec = ServerTimer.distOfSecond(Calendar.getInstance());
	   int rand = (int) (Math.random()* 1000000);
        return ""+sec + rand;
    }
	
	public void getCupReward(int cupId)
	{
		
		
		String str = "获得奖励:";
		int[] rewardArr = new int[]{0,0,0,0,0,0};// 邀请函，道具4，道具3，道具1，钻石，金币
		boolean flag = false;
		// 1 一定概率获得邀请函
		int gv_p2 = (int) (Math.random() * 100);
		if(gv_p2 < 30) //获得道具邀请函
		{
			rewardArr[0] = 1;
			str += "  邀请函x1";
		}
		int value= cupId * 10;
		if(value > (100))
		{
			flag = getRand(30);
			if(flag)
			{
				value-= 100;	rewardArr[1] = 1;
				str += "  强运药水x1";
			}
		}
		if(value > (30))
		{
			flag = getRand(30);
			if(flag)
			{
				value-=30;	rewardArr[2] = 1;
				str += "  幸运药水x1";
			}
		}
		if(value > 1) //喇叭
		{
			flag = getRand(30);
			if(flag)
			{
				int num = (int) (Math.random() * value+1);
				value-= num;	rewardArr[3] = num;
				str += "  喇叭x" + num;
			}
		}
		if(value > 0)//钻石
		{
			flag = getRand(50);
			if(flag)
			{
				int num = (int) (Math.random() * value+1);
				value-= num;	rewardArr[4] = num;
				str += "  钻石x" + num;
			}
		}
		if(value > 0)  //金币
		{
				rewardArr[5] = value * 10;
				str += "  金币x" + value * 10;
		}
		trace(str);
	}
	public boolean getRand(int rate)
	{
		int gv = (int) (Math.random() * 100);
		if(rate < gv)
			return true;
		return false;
	}
	
	@Test 
	public void TestList()
	{
		List<Integer> lis1 =  Arrays.asList(1,2,3,4,5,6,7,8);
		List<List<Integer>> lis2 = new ArrayList<List<Integer>>();
		lis2.add(lis1);
		trace(lis2.toString());
	}
	@Test 
	public void TestList2()
	{
		List<Integer> lis1 =  MyArrays.asList(1,2,3,4,5,6,7);
		List<Integer> lis2 = MyArrays.subList(2, lis1.size(), lis1);
//		lis2.remove(0);
		trace(lis2.toString());
	}
	@Test 
	public void Testxunhuan()
	{
			int[] tmp  = new int[]{1,2,3,4,5};
			for(int i : tmp)
			{
				trace(i+"");
			}
	}
	
//	public static void main(String[] args)
//	{
//		UnitTest test = new UnitTest();
//		test.testHuPai();
//	}
	
	@Test 
	public void testHuPai()
	{
		int hupai = 35;
		List<Integer>  arr_sp =  MyArrays.asList(2,2,7,7,8,8,37,37,36,36,35,35,35);  
		Algorithm_Hu hu = new Algorithm_Hu();
		hu.hupaiId  = hupai;
		hu.isZimo  = false ;
		boolean b  =  hu.testHu(arr_sp);
		Iterator<Integer> it  = hu.shoupai.keySet().iterator();
		while(it.hasNext())
		{
			int pai = it.next();
			GroupShoupai group =  hu.shoupai.get(pai);
			trace("胡牌:"+pai + ","+ group.toString());
		}
		MgsPlayer player = new MgsPlayer();
		
		ArrayList<Integer> peng = new ArrayList<Integer>();
		player.setPeng(peng);
		player.setGang(new ArrayList<List<Integer>>());
		ArrayList<int[]> chi = new ArrayList<int[]>();
		player.setChi(chi);
		
		arr_sp.add(hupai);
		 player.setShoupai(arr_sp);
		//算番
			Map<Integer, GroupShoupai> map = hu.shoupai;
			caculateFan(player, map, hu.isAnqidui);
		
	}
	
	public synchronized void caculateFan(MgsPlayer p,
			Map<Integer, GroupShoupai> map, boolean isAnqidui) {
		List<MaJiang_Fan> fans = new ArrayList<MaJiang_Fan>();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			GroupShoupai shoupai = map.get(key);
			shoupai.p = p;
			shoupai.patternShoupai();
			int menfeng = 2;

			MaJiang_Fan fan = new MaJiang_Fan();
			int lianzhuang = 0;
			fan.init(p, shoupai.getAnke(), shoupai.getShun(),
					shoupai.getJiang(), shoupai.getTing(), isAnqidui,
					p.isGangshanghua(), false, p.isQianggang(), p.isTianhe(),
					p.isDihe(), true, p.isTianting(), -1, menfeng,shoupai.hupaiId,lianzhuang);
			fan.caculateFan();
			fans.add(fan);
			
			
			System.out.println(fan);
		}
		Collections.sort(fans, new SortByFan());
		for (MaJiang_Fan f : fans) {
			System.out.println(f.getTotalFan());
		}
		MaJiang_Fan result = fans.get(0);
		p.setFan(result);
		System.out.println("结果:"+result);
	}
	
	@Test
	public void testListSize()
	{
		List<String> fanNameList  =  MyArrays.asList("暗七对","清一色");
			trace(fanNameList.indexOf("清一色")+"");
	} 
	@Test
	public void testString()
	{
		String str =  "sdf.sd\\fefsfsdf";
	}
	@Test
	public void testSort()
	{
		List<Integer> 	  list  = MyArrays.asList(1,1,1,2,3,4,5,5,6,7,7,7,8,9,9,9);
		List<MajiangPai>  tmp   = SortMaJiang.getInstance().sortByNum(list);
		
		trace(tmp.toString());
	}
	@Test
	public void TestListContain()
	{
		List<Integer> list =  new ArrayList<Integer>(); 
		list.add(1);list.add(11);list.add(12);
		int pai  =  14;
		trace(list.contains(pai)+"");
	}
	@Test 
	public void testDecode()
	{		
		List<Integer> list  = MyArrays.asList(1,2,3,4,5,6,7,8,9);
		StringBuffer strBuf = new StringBuffer();
		for(int i = 0 ; i < list.size();i++)
			strBuf.append(list.get(i));
		int cmdPattern =  Integer.parseInt(strBuf.toString());
		trace(cmdPattern+"");
	}
	@Test
	public void testStrBuf()
	{
		List<Integer> list =  MyArrays.asList(2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,4,4,4,4);
		
		 List<Integer> cmdPatterns  = new ArrayList<Integer>();
		StringBuffer strBuf = new StringBuffer();
		for(int i = 0 ; i < list.size();i++)
		{
			strBuf.append(list.get(i));
			if(strBuf.length() == 9 &&  i < list.size() -1)
			{
				strBuf.insert(0, 1);
				int tmpPat = Integer.parseInt(strBuf.toString());
				cmdPatterns.add(tmpPat);
				strBuf = new StringBuffer();
			}
		}
		int tmp =  Integer.parseInt(strBuf.toString());
		cmdPatterns.add(tmp);
		
		trace(cmdPatterns.toString());
	}
	
	@Test
	public void testInstance()
	{
		List<Object> list  = new ArrayList<Object>();
		list.add(1);
		list.add(1000);
		list.add(100000);
		
		for(int i = 0 ; i < list.size();i++)
		{ 
			System.out.println(list.get(i).getClass());
		}
	}
	@Test 
	public void testMapSize()
	{
		int[] a =  new int[]{35, 35, 9, 5, 5, 4, 5,  3, 35, 4, 6, 9, 7};
		Arrays.sort(a);
		for(int i = 0 ; i < a.length;i++)
		{
			System.out.println(a[i]);
		}
	}
	
	@Test 
	public void testMap()
	{
		List<Integer> list = null  ;
		try {
			list  = new ArrayList<Integer>();
			list.add(1);
			System.out.println("1");
			list.remove((Integer)1);
			list.remove((Integer)1);
			list.remove((Integer)1);
			System.out.println("2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size());
		
	}
	@Test
	public void testJiPin()
	{
		MyArray arr  = new MyArray();
		arr.push(1);
		arr.push("中国");
		arr.push(110);
		MyByteArray byteArr  = new MyByteArray();
		byteArr.write(arr);
		ChannelBuffer buf  =  byteArr.getBuf();
		CCMD11005 ccmd  = new CCMD11005();
		ccmd.setBytes(buf);
		
	}

	@Test
	public void testCompare()
	{
		List<MgsPlayer> list  =  new ArrayList<MgsPlayer>();
		MgsPlayer p1 =  new MgsPlayer();
		p1.setName("p1"); p1.setTotalScore(98);
		list.add(p1);
		MgsPlayer p2 =  new MgsPlayer();
		p2.setName("p2"); p2.setTotalScore(95);
		list.add(p2);
		MgsPlayer p3 =  new MgsPlayer();
		p3.setName("p3"); p3.setTotalScore(96);
		list.add(p3);
		MgsPlayer p4 =  new MgsPlayer();
		p4.setName("p4"); p4.setTotalScore(91);
		list.add(p4);
		Collections.sort(list);
		for(int i  = 0 ;  i < list.size();i++)
		{
			System.out.println(list.get(i).getName());
		}
	}
	@Test 
	public void testClass()
	{
		CCMD11011 ccmd =  new CCMD11011();
		MgsPlayer p   =  new MgsPlayer();
		p.setName("aaa");
		ccmd.setPlayer(p);
		CMD cmd  = ccmd;
		System.out.print("p:"+cmd.getPlayer());
	}
	@Test 
	public void testTime(){
	    log(ServerTimer.getDay());
	}
	public void log(String str)
	{
		System.out.println(str);
	}
	@Test
	public void testArr()
	{
		int[] arr = new int[]{0,0,0,0};
		arr[9/10]++;
		trace(arr);
		arr[19/10]++;
		trace(arr);
		arr[19/10]++;
		trace(arr);
	}
	@Test
	public void testyqgt()
	{
		List<int[]> chi = new ArrayList<int[]>();
		chi.add(new int[]{1,2,3});
		chi.add(new int[]{17,18,19});
		List<List<Integer>> shun = new ArrayList<List<Integer>>();
		shun.add(MyArrays.asList(4,5,6));
		shun.add(MyArrays.asList(7,8,9));
	}
	@Test
	public void testBoolean()
	{
		boolean  b = true;
		List<Object> list = new ArrayList<Object>();
		list.add(b);
		Object bb  = list.get(0);
		trace(bb.toString());
	}
	@Test 
	public void testArr11()
	{
		int[] a = new int[10];
		trace(a);
	}
	@Test
	public void testList11()
	{
		List<Integer> list = MyArrays.asList(-1,-1,-1,-1);
		list.set(3, 1);
		list.set(1, 4);
		trace(list.size()+"");
		trace(list.toString());
		
		for(int i= 0; i < list.size();i++)
		{
			if(list.get(i) == -1)
				list.remove(i);
		}
		trace(list.toString());
	}
	@Test
	public void testStr()
	{
		String str = "2014-03-25 11:17:26";
		trace(str.substring(0, 10));
		String str1 = "";
		trace(str1.length()+"");
		String[] taskStrs = str1.split(";");
		trace(taskStrs.length+"");
		if(taskStrs[0].equals(""))trace("true");
	}
	@Test
	public void testMap11()
	{
		Map<Integer,String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "1");
		String str = map.get(2);
		trace(str + "");
	}
	@Test
	public void testStr11()
	{
		List<Object> list = new ArrayList<Object>();
		list.add(1);list.add(2);list.add("abc");
		trace(list.toString());
		
		list.set(1, 10);
		list.set(2, 77);
		trace(list.toString());
		
	}
	@Test
	public void testMapremove()
	{
		trace("aaa");
	}
	@Test
	public void testList0()
	{
		List<Integer> list1 = MyArrays.asList(1,2,3,4);
		List<Integer> list2 = MyArrays.asList(1,2,3,4);
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(list1);
		list.addAll(list2);
		
		while(list.size() > 0)
		{
			list.remove(0);
		}
		trace(list1.toString());
		trace(list2.toString());
	}
	
	@Test 
	public void testPay()
	{
		int a = 10;
		int b = (int) (a * 0.99);
		trace(b + "");
		
	}
	@Test 
	public void testJuqing()
	{
		CCMD11102 ccmd = new CCMD11102();
		ccmd.cdao = new CountDao();
		Sts_JuqingDay day = ccmd.getJuqingCountData();
		ccmd.modeJuqingDayOtherData(day);
		ccmd.cdao.saveSts_Object(day);
	}
	@Test
	public void testrr()
	{

	}
	
	
}
