package business.conut;

import java.util.Calendar;

import business.entity.MJ_DanJil;
import business.entity.MJ_User;

import server.mj.MgsPlayer;
import server.mj.ServerTimer;

/**
 * 单机次数统计
 * @author xue
 *
 */
public class Sts_DJ_Count {

	private int id;
	private int absDay;
	private String timeDay = "";
	private String timeMonth ="";
	
	private int Time_1 = 0;
	private int Time_2 = 0;
	private int Time_3 = 0;
	private int Time_4 = 0;
	private int Time_5 = 0;
	private int Time_other = 0;
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
	public int getTime_1() {
		return Time_1;
	}
	public void setTime_1(int time_1) {
		Time_1 = time_1;
	}
	public int getTime_2() {
		return Time_2;
	}
	public void setTime_2(int time_2) {
		Time_2 = time_2;
	}
	public int getTime_3() {
		return Time_3;
	}
	public void setTime_3(int time_3) {
		Time_3 = time_3;
	}
	public int getTime_4() {
		return Time_4;
	}
	public void setTime_4(int time_4) {
		Time_4 = time_4;
	}
	public int getTime_5() {
		return Time_5;
	}
	public void setTime_5(int time_5) {
		Time_5 = time_5;
	}
	public int getTime_other() {
		return Time_other;
	}
	public void setTime_other(int time_other) {
		Time_other = time_other;
	}
	
	
	
}
