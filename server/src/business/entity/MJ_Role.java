package business.entity;


public class MJ_Role {
	
	private int id;
	private int roleId;
	private int level = 1;
	private int exp;            //赢一把加一点， 满级10级，没升一级固定点数
	private MJ_User user;
	
	public void init(int _roleId)
	{
		this.roleId  = _roleId;
		this.level  = 1;
		this.exp  = 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public MJ_User getUser() {
		return user;
	}
	public void setUser(MJ_User user) {
		this.user = user;
	}
	
	
	
}
