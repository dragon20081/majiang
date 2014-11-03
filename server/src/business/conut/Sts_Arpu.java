package business.conut;

public class Sts_Arpu {

	 private int id;
	 private int absDay;
	 private String day;
	 private String timeMoth;
	 private int chargeNum;
	 private int addCharge;
	 /**
	  * 金额
	  */
	 private int amount;
	 private Double arpuv;
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
	public int getChargeNum() {
		return chargeNum;
	}
	public void setChargeNum(int chargeNum) {
		this.chargeNum = chargeNum;
	}
	public int getAddCharge() {
		return addCharge;
	}
	public void setAddCharge(int addCharge) {
		this.addCharge = addCharge;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Double getArpuv() {
		return arpuv;
	}
	public void setArpuv(Double arpuv) {
		this.arpuv = arpuv;
	}
	public String getTimeMoth() {
		return timeMoth;
	}
	public void setTimeMoth(String timeMoth) {
		this.timeMoth = timeMoth;
	}

}