package business.conut;

import java.util.Calendar;

import business.entity.MJ_DanJil;
import business.entity.MJ_User;

import server.mj.MgsPlayer;
import server.mj.ServerTimer;

public class Sts_UserDanji {

	private int id;
	private int absDay;
	private String day = "";
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
	public int getDanjiId() {
		return danjiId;
	}
	public void setDanjiId(int danjiId) {
		this.danjiId = danjiId;
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
	public void startDanji(MgsPlayer p,MJ_DanJil danji)
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
		this.danjiId = danji.getId();
	}
	public void endDanji(MgsPlayer p,MJ_DanJil danji)
	{
		this.endFlag = 1;
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
