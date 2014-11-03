package business.entity;

/**
 * 技能数据
 * @author xue
 */
public class MJ_DanJil {
	
	private int id;
	private int bei;
	private int cost;
	private int quan;
	private int startPoint;
	private int pan;
	private boolean skill;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBei() {
		return bei;
	}
	public void setBei(int bei) {
		this.bei = bei;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getQuan() {
		return quan;
	}
	public void setQuan(int quan) {
		this.quan = quan;
	}
	public int getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
	public int getPan() {
		return pan;
	}
	public void setPan(int pan) {
		this.pan = pan;
	}
	public boolean isSkill() {
		return skill;
	}
	public void setSkill(boolean skill) {
		this.skill = skill;
	}
}
