package business.conut;

import java.util.Calendar;
import java.util.List;

import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.entity.MJ_Cup;
import business.entity.MJ_User;

public class Sts_UserCup {

	
	public static int END_WIN = 1;
	public static int END_FAIL = 2;
	
	private int id;
	private int absDay;
	private String day = "";
	private String timeDay = "";
	private String timeMonth ="";
	private int uid;
	private String account = "";
	private String nick ="";
	private int cupId = 0;
	private int startFlag = -1;
	private String startTime = "";
	private int endFlag = -1;
	private String endTime = "";
	private int consumeProp1;  //消耗道具1
	private int consumeProp2;  //消耗道具2
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAbsDay() {
		return absDay;
	}
	public void setAbsDay(int absDay) {
		this.absDay = absDay;
	}
	public String getTimeDay() {
		return timeDay;
	}
	public void setTimeDay(String timeDay) {
		this.timeDay = timeDay;
	}
	public String getTimeMonth() {
		return timeMonth;
	}
	public void setTimeMonth(String timeMonth) {
		this.timeMonth = timeMonth;
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
	public int getCupId() {
		return cupId;
	}
	public void setCupId(int cupId) {
		this.cupId = cupId;
	}
	public int getStartFlag() {
		return startFlag;
	}
	public void setStartFlag(int startFlag) {
		this.startFlag = startFlag;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int getEndFlag() {
		return endFlag;
	}
	public void setEndFlag(int endFlag) {
		this.endFlag = endFlag;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public int getConsumeProp1() {
		return consumeProp1;
	}
	public void setConsumeProp1(int consumeProp1) {
		this.consumeProp1 = consumeProp1;
	}
	public int getConsumeProp2() {
		return consumeProp2;
	}
	public void setConsumeProp2(int consumeProp2) {
		this.consumeProp2 = consumeProp2;
	}
	public void startCup(MgsPlayer p,MJ_Cup cup,List<Object> props)
	{
		this.absDay = ServerTimer.distOfSecond(Calendar.getInstance());
		this.day = ServerTimer.getNowString();
		this.timeDay = ServerTimer.getDay();
		this.timeMonth = ServerTimer.getMonth();
		
		MJ_User user = p.getBusiness().getPlayer();
		this.uid = user.getUid();
		this.account = user.getName();
		this.nick = user.getNick();
		this.startFlag = 1;
		this.startTime = ServerTimer.getNowString();
		this.cupId = cup.getId();
		
		if(props != null)
		{
			for(int i = 0; i < props.size();i++)
			{
				int propId = (Integer) props.get(i);
				modConsumeProp(propId);
			}
		}
	}
	
	public void  modConsumeProp(int propId)
	{
		propId = propId%100;
		switch(propId)
		{
			case 3: this.consumeProp1 += 1;break;
			case 4: this.consumeProp2 += 1; break;
		}
	}
	public void endCup(MgsPlayer p,MJ_Cup cup)
	{
		this.endFlag = END_WIN;
		this.endTime = ServerTimer.getNowString();
	}
	public void failCup(MgsPlayer p,MJ_Cup cup)
	{
		this.endFlag = END_FAIL;
		this.endTime = ServerTimer.getNowString();
	}
	
	/**
	 * 	private int id;
	private int absDay;
	private String timeDay = "";
	private String timeMonth ="";
	private int uid;
	private String account = "";
	private String nick ="";
	private int danjiId = 0;
	private int startFlag = -1;
	private String startTime = "";
	private int endFlag = -1;
	private String endTime = "";
	 */
	
}
