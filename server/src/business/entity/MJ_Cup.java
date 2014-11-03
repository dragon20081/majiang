package business.entity;

/**
 * 杯赛
 * @author xue
 */
public class MJ_Cup {
	
	private int id;
	private String name;
	private String info;
	private int rule;
	private int cost;
	private int difficulty;
	private String condition;
	private String scopePlayer;
	private int playerNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getRule() {
		return rule;
	}
	public void setRule(int rule) {
		this.rule = rule;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getScopePlayer() {
		return scopePlayer;
	}
	public void setScopePlayer(String scopePlayer) {
		this.scopePlayer = scopePlayer;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
}
