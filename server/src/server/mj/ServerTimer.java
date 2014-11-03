package server.mj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import business.CountDao;
import business.conut.Sts_Urr;
import business.entity.M_State;

public class ServerTimer implements TimerTask{
	
	private static ServerTimer _instance;
	public int now_seconds = 0;
	private Timeout timeout = null;
		
	public static ServerTimer getInstance()
	{
		if(_instance == null)
		{
			_instance = new ServerTimer();
			
		}
		return _instance;
	}
	public void start()
	{
		//this.timeout  = Global.timer.newTimeout(this, 1, TimeUnit.HOURS);
	}
	public static String getDateString(Calendar c)
	{
		String time="";
		boolean mark=true;
		try{
			SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time=s.format(c.getTime());
		}catch (Exception e) {
			mark=false;
		}
       if(mark==false){
			
	        SimpleDateFormat s1=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			try {
				time=s1.format(c.getTime());
			} catch (Exception e1) {
				
				mark=false;
			}
		}
		if(mark==false){
			SimpleDateFormat s2=new SimpleDateFormat("yyyy:MM:dd");
			try {
				time=s2.format(c.getTime());
			} catch (Exception e2) {
				
				mark=false;
			}
		}
		if(mark==false){
			SimpleDateFormat s2=new SimpleDateFormat("yyyy-MM-dd");
			try {
				time=s2.format(c.getTime());
			} catch (Exception e3) {
				
				mark=false;
			}
		}
		return time;
	}
	public static String getNowString()
	{
		Calendar now = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(now.getTime());
	}
	public static Calendar getCalendarFromString(String str)
	{
		Calendar c = Calendar.getInstance();
		boolean mark=true;
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c.setTime(s.parse(str));
		} catch (ParseException e) {
//			e.printStackTrace();
			mark=false;
		}
		if(mark==false){
			
	        SimpleDateFormat s1=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			try {
				c.setTime(s1.parse(str));
			} catch (ParseException e) {
//				e.printStackTrace();
				mark=false;
			}
		}
		
		if(mark==false){
			SimpleDateFormat s2=new SimpleDateFormat("yyyy:MM:dd");
			try {
				c.setTime(s2.parse(str));
			} catch (ParseException e) {
//				e.printStackTrace();
				mark=false;
			}
		}
		
		if(mark==false){
			SimpleDateFormat s2=new SimpleDateFormat("yyyy-MM-dd");
			try {
				c.setTime(s2.parse(str));
			} catch (ParseException e) {
//				e.printStackTrace();
				mark=false;
			}
		}
		return c;
	}
	public static int distOfDay(Calendar c)
	{
		Calendar c2000 = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			c2000.setTime(s.parse("2000-1-1 0:0:0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) ((c.getTimeInMillis() - c2000.getTimeInMillis())/(24*60*60*1000));
	}
	public static int distFromNowDay(Calendar c)
	{
		Calendar now = Calendar.getInstance();
		return (int) ((now.getTimeInMillis() - c.getTimeInMillis())/(24*60*60*1000));
	}
	public static int distOfHour(Calendar c)
	{
		Calendar c2000 = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c2000.setTime(s.parse("2000-1-1 0:0:0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) ((c.getTimeInMillis() - c2000.getTimeInMillis())/(60*60*1000));
	}
	public static int distOfMinute(Calendar c)
	{
		Calendar c2000 = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			c2000.setTime(s.parse("2000-1-1 0:0:0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) ((c.getTimeInMillis() - c2000.getTimeInMillis())/(60*1000));
	}
	public static int distOfSecond(Calendar c)
	{
		Calendar c2000 = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			c2000.setTime(s.parse("2000-1-1 0:0:0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) ((c.getTimeInMillis() - c2000.getTimeInMillis())/(1000));
	}
	public static double distOfDaydouble(Calendar c)
	{
		Calendar c2000 = Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c2000.setTime(s.parse("2000-1-1 0:0:0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  (c.getTimeInMillis() - c2000.getTimeInMillis())/(24*60*60*1000);
	}
	public static String getHour(){
		
		return getNowString().substring(11,13);
		
	}
    public static String getHouradd1(int hour){
		int m=0;
	    m=hour+1;
	    String time="";
	    if(m!=23){
	    	if(m<10){
	    		time+="0"+String.valueOf(m);
	    	}else{
	    		time=String.valueOf(m);
	    	}
	    }else{
	    	time="00";
	    }
		return time;
	}
    public static String getDay(){
		
		return getNowString().split(" ")[0];
		
	}
    public static String getMonth(){
		
    	String day=getDay();
    	String[] r=day.split("-");
		return r[0]+"-"+r[1];
	}
    public static String getTimehdm(){
	   
    	String str=getNowString().substring(11,19);
    	String[] m=str.split(":");
	    return   m[0]+m[1];
     }
	public void run(Timeout arg0) throws Exception {
		
		//if(this.timeout != null) this.timeout.cancel();
		//modAverageOnLine();
		
		
	}
//	public void modAverageOnLine()
//	{
//		//算出平均数据
//		
//		int onLineNum  = Global.players.size();
//		CountDao dao = new CountDao();
//		M_State state  = dao.getState();
//		String aveStr  = state.getAveOnline();
//		String now  =ServerTimer.getDay();
//		String[] rec  = state.getAveOnline().split(",");
//		if(rec.length != 0)
//		{
//			String date  =  rec[0];
//			if(!now.equals(date))
//			{
//				aveStr  =  now +","+onLineNum+",";
//			}else
//			{
//				aveStr += onLineNum+",";
//			}
//		}else
//		{
//			aveStr  =  now +","+onLineNum+",";
//		}
//		
//		
//		
//		rec  = aveStr.split(",");
//		int maxCount = 0;
//		for(int i  = 1 ; i < rec.length;i++)
//		{
//			if(rec[i].equals(""))continue;
//			maxCount += Integer.parseInt(rec[i]);
//		}
//		int aver =  (int)maxCount /(rec.length -1);
//		state.setAveOnline(aveStr);
//		dao.saveState(state);
//		Sts_Urr urr  = dao.findTodayRR();
//		urr.setAveOnline(aver);
//		dao.saveUserRR(urr);
//		this.timeout  = Global.timer.newTimeout(this, 1, TimeUnit.HOURS);
//	}
}
