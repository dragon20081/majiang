package business.conut;

public class Sts_GoldDia {

	private int id;
	private int absDay;
	private String day;
	private long totalGold;
	private long totalDia;
	private long usedGold;
	private long useedDia;
	private long GoldFromDia; //从钻石兑换来的金币数量  购买金币时统计
	private long DiaToGold;   //兑换成金币的钻石数量
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
	public long getTotalGold() {
		return totalGold;
	}
	public void setTotalGold(long totalGold) {
		this.totalGold = totalGold;
	}
	public long getTotalDia() {
		return totalDia;
	}
	public void setTotalDia(long totalDia) {
		this.totalDia = totalDia;
	}
	public long getUsedGold() {
		return usedGold;
	}
	public void setUsedGold(long usedGold) {
		this.usedGold = usedGold;
	}
	public long getUseedDia() {
		return useedDia;
	}
	public void setUseedDia(long useedDia) {
		this.useedDia = useedDia;
	}
	public long getGoldFromDia() {
		return GoldFromDia;
	}
	public void setGoldFromDia(long goldFromDia) {
		GoldFromDia = goldFromDia;
	}
	public long getDiaToGold() {
		return DiaToGold;
	}
	public void setDiaToGold(long diaToGold) {
		DiaToGold = diaToGold;
	}
}
