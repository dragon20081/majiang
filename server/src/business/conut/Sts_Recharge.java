package business.conut;

import java.util.Iterator;
import java.util.Map;

import business.entity.MJ_PayInfo;
import server.command.GlobalData;
import server.mj.Global;

public class Sts_Recharge {

	private int id;
	private int absDay;
	private String day;
	private String  time1 = ""; //2012-12-3
	private String  time2 = ""; //2012-12
	private int uid;
	private String account;
	private String nick;
	private int dia1;
	private int dia2;
	private int dia3;
	private int dia4;
	private int dia5;
	private int dia6;
	private int money;
	private String payWay;
	private int diaByfore;
	private int diaAfter;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getDia1() {
		return dia1;
	}
	public void setDia1(int dia1) {
		this.dia1 = dia1;
	}
	public int getDia2() {
		return dia2;
	}
	public void setDia2(int dia2) {
		this.dia2 = dia2;
	}
	public int getDia3() {
		return dia3;
	}
	public void setDia3(int dia3) {
		this.dia3 = dia3;
	}
	public int getDia4() {
		return dia4;
	}
	public void setDia4(int dia4) {
		this.dia4 = dia4;
	}
	public int getDia5() {
		return dia5;
	}
	public void setDia5(int dia5) {
		this.dia5 = dia5;
	}
	public int getDia6() {
		return dia6;
	}
	public void setDia6(int dia6) {
		this.dia6 = dia6;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public int getDiaByfore() {
		return diaByfore;
	}
	public void setDiaByfore(int diaByfore) {
		this.diaByfore = diaByfore;
	}
	public int getDiaAfter() {
		return diaAfter;
	}
	public void setDiaAfter(int diaAfter) {
		this.diaAfter = diaAfter;
	}
	public int getAbsDay() {
		return absDay;
	}
	public void setAbsDay(int absDay) {
		this.absDay = absDay;
	}

	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public static String getDiaNumStr()
	{
		int[] arr  = getRechargeNum();
		return arr[0]+","+arr[1]+","+arr[2]+","+arr[3]+","+arr[4]+","+arr[5];
	}
	public synchronized static int[] getRechargeNum()
	{
		int[] arr = new int[6];
		int i = 0;
		 Map<Integer,MJ_PayInfo> payItems = Global.payItems;
		 
		 Iterator<MJ_PayInfo> it = payItems.values().iterator();
		 while(it.hasNext())
		 {
			 MJ_PayInfo pay = it.next();

			 if(GlobalData.PINGTAI_SERVER.equals("IOS") && pay.getPayStr_apple().equals(""))continue;
			 if(!GlobalData.PINGTAI_SERVER.equals("IOS") && !pay.getPayStr_apple().equals(""))continue;
			 arr[i] = pay.getDia();
			 i++;
		 }
		return arr;
	}
	
};

