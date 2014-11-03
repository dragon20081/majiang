package business.entity;
/**
 * 道具类: 
 * @author xue
 */
public class M_Prop {
	
	private int id;
	private int pro1Num = 0;  //喇叭
	private int pro2Num = 0;  //邀请书
	private int pro3Num = 0;  //幸运药水
	private int pro4Num = 0;  //强运药水
	private MJ_User user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPro1Num() {
		return pro1Num;
	}
	public void setPro1Num(int pro1Num) {
		this.pro1Num = pro1Num;
	}
	public MJ_User getUser() {
		return user;
	}
	public void setUser(MJ_User user) {
		this.user = user;
	}
	public int getPro2Num() {
		return pro2Num;
	}
	public void setPro2Num(int pro2Num) {
		this.pro2Num = pro2Num;
	}
	public int getPro3Num() {
		return pro3Num;
	}
	public void setPro3Num(int pro3Num) {
		this.pro3Num = pro3Num;
	}
	public int getPro4Num() {
		return pro4Num;
	}
	public void setPro4Num(int pro4Num) {
		this.pro4Num = pro4Num;
	}
	
	public int getPropNumById(int propNum)
	{
		switch(propNum)
		{
			case 1: return this.pro1Num;
			case 2: return this.pro2Num;
			case 3: return this.pro3Num;
			case 4: return this.pro4Num;
		}
		return 0;
	}
}
