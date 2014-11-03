package business.entity;

/**
 * 设备统计
 * @author xue
 */
public class MJ_Device {
	
	private int id;
	private int deviceId;
	private String regTime ="";
	private String lastLoginTime = "";
	private int openTimes;           //打开次数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getOpenTimes() {
		return openTimes;
	}
	public void setOpenTimes(int openTimes) {
		this.openTimes = openTimes;
	}
}
