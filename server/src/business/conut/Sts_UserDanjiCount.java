package business.conut;



public class Sts_UserDanjiCount {

	private int id;
	private int absDay;
	private String timeDay = "";
	private String timeMonth ="";

	private int dan1_open;
	private int dan1_close;
	
	private int dan2_open;
	private int dan2_close;
	
	private int dan3_open;
	private int dan3_close;
	
	private int dan4_open;
	private int dan4_close;
	
	private int dan5_open;
	private int dan5_close;
	
	private int dan6_open;
	private int dan6_close;
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
	public int getDan1_open() {
		return dan1_open;
	}
	public void setDan1_open(int dan1_open) {
		this.dan1_open = dan1_open;
	}
	public int getDan1_close() {
		return dan1_close;
	}
	public void setDan1_close(int dan1_close) {
		this.dan1_close = dan1_close;
	}
	public int getDan2_open() {
		return dan2_open;
	}
	public void setDan2_open(int dan2_open) {
		this.dan2_open = dan2_open;
	}
	public int getDan2_close() {
		return dan2_close;
	}
	public void setDan2_close(int dan2_close) {
		this.dan2_close = dan2_close;
	}
	public int getDan3_open() {
		return dan3_open;
	}
	public void setDan3_open(int dan3_open) {
		this.dan3_open = dan3_open;
	}
	public int getDan3_close() {
		return dan3_close;
	}
	public void setDan3_close(int dan3_close) {
		this.dan3_close = dan3_close;
	}
	public int getDan4_open() {
		return dan4_open;
	}
	public void setDan4_open(int dan4_open) {
		this.dan4_open = dan4_open;
	}
	public int getDan4_close() {
		return dan4_close;
	}
	public void setDan4_close(int dan4_close) {
		this.dan4_close = dan4_close;
	}
	public int getDan5_open() {
		return dan5_open;
	}
	public void setDan5_open(int dan5_open) {
		this.dan5_open = dan5_open;
	}
	public int getDan5_close() {
		return dan5_close;
	}
	public void setDan5_close(int dan5_close) {
		this.dan5_close = dan5_close;
	}
	public int getDan6_open() {
		return dan6_open;
	}
	public void setDan6_open(int dan6_open) {
		this.dan6_open = dan6_open;
	}
	public int getDan6_close() {
		return dan6_close;
	}
	public void setDan6_close(int dan6_close) {
		this.dan6_close = dan6_close;
	}
	
	public void openDanji(int type)
	{
		switch(type)
		{
			case 1: this.dan1_open +=1;break;
			case 2: this.dan2_open +=1;break;
			case 3: this.dan3_open +=1;break;
			case 4: this.dan4_open +=1;break;
			case 5: this.dan5_open +=1;break;
			case 6: this.dan6_open +=1;break;
		}
	}
	public void closeDanji(int type)
	{
		switch(type)
		{
			case 1: this.dan1_close +=1;break;
			case 2: this.dan2_close +=1;break;
			case 3: this.dan3_close +=1;break;
			case 4: this.dan4_close +=1;break;
			case 5: this.dan5_close +=1;break;
			case 6: this.dan6_close +=1;break;
		}
	}
	
}
