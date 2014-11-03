package business.entity;

public class M_State {
	private int id;
	private int time = 0;
	private int maxOnline = 0;
	private int login_count = 0;
	private int reg_count = 0;
	private int money = 0;
	private int activeUser = 0;
	private String  aveOnline ="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLogin_count() {
		return login_count;
	}
	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}
	public int getReg_count() {
		return reg_count;
	}
	public void setReg_count(int reg_count) {
		this.reg_count = reg_count;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(int activeUser) {
		this.activeUser = activeUser;
	}
	public int getMaxOnline() {
		return maxOnline;
	}
	public void setMaxOnline(int maxOnline) {
		this.maxOnline = maxOnline;
	}
	public String getAveOnline() {
		return aveOnline;
	}
	public void setAveOnline(String aveOnline) {
		this.aveOnline = aveOnline;
	}
}
