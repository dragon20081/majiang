package business.junitTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.command.Algorithm_Hu;
import server.command.GroupShoupai;
import server.command.MajiangPai;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.SortMaJiang;
import server.command.cmd.CCMD11005;
import server.command.cmd.CCMD11008;
import server.mj.MgsPlayer;
import server.mj.Room;
import server.mj.ServerTimer;
import business.Business;
import business.CountDao;
import business.conut.Sts_BuyProp;
import business.conut.Sts_Chat;
import business.conut.Sts_JuqingDay;
import business.conut.Sts_UseProp;

import common.Log;
import common.MyArrays;

public class UnitCountTest{
	
	private CountDao dao;
	
	@Before
	public void before()
	{
		dao = new CountDao();
	}
	@Test
	public void test1()
	{
		int num1 = dao.CountPlayerByGold(30);
		Log.log(num1);
		int num2 = dao.CountPlayerByDia(100);
		Log.log(num2);
	}
	@Test 
	public void testAddChat()
	{
		
		Calendar c = ServerTimer.getCalendarFromString("2014-03-19");
		Sts_Chat chat = new Sts_Chat();
		chat.setAbsDay(ServerTimer.distOfSecond(c));
		chat.setDay("2014-03-19");
		chat.setUid(1);
		chat.setAccount("ffx");
		chat.setNick("ffx");
		chat.setChatMsg("喇叭: 这是一条喇叭");
		dao.saveSts_Object(chat);
	}
	
	@Test
	public void testCountProp()
	{
		
		Sts_BuyProp buy = new Sts_BuyProp();
		buy.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		buy.setAccount("ffx");
		buy.setDay(ServerTimer.getNowString());
		buy.setTime1(ServerTimer.getDay());
		buy.setTime2(ServerTimer.getMonth());
		buy.setProp1(1);
		dao.saveSts_Object(buy);
	}
	@Test
	public void testFindProp()
	{
		List list = dao.countPropUse_day();
		for(int i = 0 ; i < list.size();i++)
		{
			Object[] arr = (Object[]) list.get(i);
			trace(arr);
		}
	}
	@Test
	public void addRecUseProp()
	{
		Sts_UseProp use = new Sts_UseProp();
		use.setAccount("ffx");
		use.setProp1(1);
		use.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		use.setAccount("ffx");
		use.setDay(ServerTimer.getNowString());
		use.setTime1(ServerTimer.getDay());
		use.setTime2(ServerTimer.getMonth());
		dao.saveSts_Object(use);
		
	}
	@Test
	public void testFindPropUse()
	{
		List list = dao.countPropUse_month();
		for(int i = 0 ; i < list.size();i++)
		{
			Object[] arr = (Object[]) list.get(i);
			for(int j = 0; j < arr.length;j++)
			{
				String str  = "";
				BigDecimal num = null;
				if(j == 0)
				{
					str = (String)arr[j];
					trace(str);
				}
				else
				{
					num = (BigDecimal) arr[j];
					trace(num.intValue());
				}
			
			
			}
		}
	}
	
	@Test
	public void testListInsert()
	{
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.set(0, 10);
		trace(list);
	}
	
	
	@Test
	public void addJuqingRec()
	{
		Sts_JuqingDay day = new Sts_JuqingDay();
		day.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
		day.setDay(ServerTimer.getDay());
		day.setHistoryHadImg(100);
		day.setHistoryNetBattle(99);
		day.setTotalPlayer(10000);
		day.setJuqing1(43);
		day.setJuqing2(55);
		day.setJuqing3(56);
		day.setTime1(ServerTimer.getDay());
		day.setTime2(ServerTimer.getMonth());
		dao.saveSts_Object(day);
	}
	@Test
	public void testChufa()
	{
		float a =  1.0f;
		int b = 21;
		float c =  a/b;
		trace(c);
	}
	@Test
	public void contgold()
	{
		int a = dao.countGold100();
		trace(a);
	}

	
	
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
		System.out.println();
	}
	private void trace(Object o)
	{
		System.out.println(o.toString());
	}
}
