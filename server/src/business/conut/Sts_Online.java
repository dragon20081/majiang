package business.conut;

public class Sts_Online {
	private int id;
	private int absDay;
	private String day =  "";
	private String olplayer = "";   //玩家
	private String olChang = "";    //场次
	private String olDating = "";   //大厅
	private int averOl = -1;
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
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getOlplayer() {
		return olplayer;
	}
	public void setOlplayer(String olplayer) {
		this.olplayer = olplayer;
	}
	public String getOlChang() {
		return olChang;
	}
	public void setOlChang(String olChang) {
		this.olChang = olChang;
	}
	public int getAverOl() {
		return averOl;
	}
	public void setAverOl(int averOl) {
		this.averOl = averOl;
	}
	public String getOlDating() {
		return olDating;
	}
	public void setOlDating(String olDating) {
		this.olDating = olDating;
	}
	
	
}
