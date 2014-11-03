package business.conut;

public class Sts_MJDevice {

	private int id;
	private int absDay;
	private String time1;  //日
	private String time2;  //月
	private int newAdd;
	private int duliDevice;
	private int openDevice;
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
	public int getNewAdd() {
		return newAdd;
	}
	public void setNewAdd(int newAdd) {
		this.newAdd = newAdd;
	}
	public int getDuliDevice() {
		return duliDevice;
	}
	public void setDuliDevice(int duliDevice) {
		this.duliDevice = duliDevice;
	}
	public int getOpenDevice() {
		return openDevice;
	}
	public void setOpenDevice(int openDevice) {
		this.openDevice = openDevice;
	}
	
	
	//新增设备
	//独立打开设备数
	//注册时间， 最后登录时间
	//设备打开的次数
}
