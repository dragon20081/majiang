package business.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MJ_User {
	
	
	public static final int MAXLEVEL = 1000;
	public static final int SCORE_EVERYLEVEL = 100;
	/**
 	1	名字
	2	金币
	3	剧情1进度
	4	剧情2进度
	5	剧情3进度
	6 	点券
	7	性别
		
	9 	 新手指引
		
	11	解锁形象
	12	拥有技能
	
	14     是否成年
		
		
	21	网络等级
	22	网络积分
	23 	 当前形象
	24      当前技能
	
	25: 玩家UID
	
	31     任务进度
	33 刷新消耗金币
*/
	//刷新单机进度需要消耗的金币
	private int refreshDia = 100;
	
	
	private int    uid;
	private String name  ="";
	private String pwd   = ""; 
	private String email = "";
	private boolean robot  = false;
	//////////////////////////////////////  
	
	private String 	nick  = "";      //1   
	private int    	gold;            //2
	private int 	juqing_1;        //3
	private int 	juqing_2;        //4
	private int 	juqing_3;        //5
	private int     dianQuan;        //6   点券
	private int 	sex =  -1;    //" 性别   0 女  1 男"
	
	private int 	newTeach; 	//新手指引
	
	//玩家战绩统计
	private int  panTotal;
	private int  panHu;
	private int  panHu10;
	private int  panHu20;
	private int  panHu30;
	////////////////////////////////////////
//	private String     unLockImg = "";		//11
	private String    ownSkills  = "";		//12
	////////////////////////////////////////
	//网络
	private int    level = 1;			//21
	private int    score;			//22
	private int    image   = -1;			//23	   当前形象
	private String equipSkill  =""; 	    //24              当前技能
	///////////////////////////////////////
	private String changDanji = "";
	private int openedCup;
	private int openedDanji;
	public int befroreOpenedDanji = -2;
	private boolean adults = true;
	//统计
	private String  regTime = "";
	private String  lastLogin = "";
	private int 	loginCount;
	private int     taopaoRate;
	
	//杯赛战绩
	private String score_cup = "-1,0,-1,-1,-1,-1,-1,-1,-1";
	//单机次数
	private int danjiCount = 0;
	
	//任务进度
	private String taskJinDu = "";

	private String pingtai = ""; 
	
	//当日累积在线时间
	private int onLineTime;
	
	private boolean hadRole = false;
	private boolean joinedNetBattle = false;

	////////////////////////////////////////
	private List<M_Shop> shopItems;  //商店商品
	/////////////////////////////////////
	//一对多
	private Set<MJ_Role> roles;
	//一对一
	private M_Prop  props;

	private MJ_Role equipRole;
	
	public boolean firstLogin_today = false;
	
	
	
	/**
	 * 获得任务状态map
	 */
	public Map<Integer,List<Object>> getTaskJinduMap()
	{
		Map<Integer,List<Object>> map = new LinkedHashMap<Integer, List<Object>>();
		String[] taskStrs = taskJinDu.split(";");
		for(int i = 0; i < taskStrs.length; i++)
		{
			String[] arr = taskStrs[i].split(",");
			if(arr.length < 2)continue;
			int taskId = Integer.parseInt(arr[0]);
			int taskStatus = Integer.parseInt(arr[1]);
			String taskPro =  "";
			if(arr.length > 2) taskPro = arr[2];
			List<Object> list  = new ArrayList<Object>();
			list.add(taskId);
			list.add(taskStatus);
			list.add(taskPro);
			map.put(taskId, list);
		}
		return map;
	}
	/**
	 * 保存任务状态Map
	 */
	public void setTaskJinduMap(Map<Integer,List<Object>> map)
	{
		Iterator<Integer> it  = map.keySet().iterator();
		String newStr = "";
		while(it.hasNext())
		{
			int key = it.next();
			List<Object> list = map.get(key);
			newStr += list.get(0)+"," + list.get(1)+"," + list.get(2) +";";
		}
		this.setTaskJinDu(newStr);
	}
	
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTaopaoRate() {
		return taopaoRate;
	}
	public void setTaopaoRate(int taopaoRate) {
		this.taopaoRate = taopaoRate;
	}
	public String getOwnSkills() {
		return ownSkills;
	}
	public void setOwnSkills(String ownSkills) {
		this.ownSkills = ownSkills;
	}
	public int getJuqing_1() {
		return juqing_1;
	}
	public void setJuqing_1(int juqing_1) {
		this.juqing_1 = juqing_1;
	}
	public int getJuqing_2() {
		return juqing_2;
	}
	public void setJuqing_2(int juqing_2) {
		this.juqing_2 = juqing_2;
	}
	public int getJuqing_3() {
		return juqing_3;
	}
	public void setJuqing_3(int juqing_3) {
		this.juqing_3 = juqing_3;
	}
	public int getDianQuan() {
		return dianQuan;
	}
	public void setDianQuan(int dianQuan) {
		this.dianQuan = dianQuan;
	}
	public List<M_Shop> getShopItems() {
		return shopItems;
	}
	public void setShopItems(List<M_Shop> shopItems) {
		this.shopItems = shopItems;
	}
	public String getEquipSkill() {
		return equipSkill;
	}
	public void setEquipSkill(String equipSkill) {
		this.equipSkill = equipSkill;
	}
	
	public boolean isRobot() {
		return robot;
	}
	public void setRobot(boolean robot) {
		this.robot = robot;
	}
	public Set<MJ_Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<MJ_Role> roles) {
		this.roles = roles;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public int getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	public M_Prop getProps() {
		return props;
	}
	public void setProps(M_Prop props) {
		this.props = props;
	}
	public MJ_Role getEquipRole() {
		return equipRole;
	}
	public void setEquipRole(MJ_Role equipRole) {
		this.equipRole = equipRole;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public int getOpenedCup() {
		return openedCup;
	}
	public void setOpenedCup(int openedCup) {
		this.openedCup = openedCup;
	}
	public boolean isHadRole() {
		return hadRole;
	}
	public void setHadRole(boolean hadRole) {
		this.hadRole = hadRole;
	}
	public boolean isJoinedNetBattle() {
		return joinedNetBattle;
	}
	public void setJoinedNetBattle(boolean joinedNetBattle) {
		this.joinedNetBattle = joinedNetBattle;
	}
	public int getNewTeach() {
		return newTeach;
	}
	public void setNewTeach(int newTeach) {
		this.newTeach = newTeach;
	}
	
	
	public int getPanTotal() {
		return panTotal;
	}
	public void setPanTotal(int panTotal) {
		this.panTotal = panTotal;
	}
	public int getPanHu() {
		return panHu;
	}
	public void setPanHu(int panHu) {
		this.panHu = panHu;
	}
	public int getPanHu10() {
		return panHu10;
	}
	public void setPanHu10(int panHu10) {
		this.panHu10 = panHu10;
	}
	public int getPanHu20() {
		return panHu20;
	}
	public void setPanHu20(int panHu20) {
		this.panHu20 = panHu20;
	}
	public int getPanHu30() {
		return panHu30;
	}
	public void setPanHu30(int panHu30) {
		this.panHu30 = panHu30;
	}
	public String getTaskJinDu() {
		return taskJinDu;
	}
	public void setTaskJinDu(String taskJinDu) {
		this.taskJinDu = taskJinDu;
	}
	public int getOnLineTime() {
		return onLineTime;
	}
	public void setOnLineTime(int onLineTime) {
		this.onLineTime = onLineTime;
	}
	public String getPingtai() {
		return pingtai;
	}
	public void setPingtai(String pingtai) {
		this.pingtai = pingtai;
	}

	public String getChangDanji() {
		return changDanji;
	}
	public void setChangDanji(String changDanji) {
		this.changDanji = changDanji;
	}
	public int getRefreshDia() {
		return refreshDia;
	}
	public void setRefreshDia(int refreshDia) {
		this.refreshDia = refreshDia;
	}
	
	public int getDanjiCount() {
		return danjiCount;
	}
	public void setDanjiCount(int danjiCount) {
		this.danjiCount = danjiCount;
	}
	public int getOpenedDanji() {
		return openedDanji;
	}
	public void setOpenedDanji(int openedDanji) {
		this.openedDanji = openedDanji;
	}
	public boolean isAdults() {
		return adults;
	}
	public void setAdults(boolean adults) {
		this.adults = adults;
	}
	public int getSkillStatus(int skillId)
	{
		int status =  0;
		switch(skillId)
		{
		case 1:   
		case 2:
		case 3:
		case 8:
		case 9:
		case 12:
		case 13:
		case 14:
			status=1;   break;
		case 7: 
			status = 0;
			break;
		case 4: 
		case 5: 
		case 6:
		case 10:
		case 11:
			break;
		}
		return status;
	}
	public String getScore_cup() {
		return score_cup;
	}
	public void setScore_cup(String score_cup) {
		this.score_cup = score_cup;
	}
	
	public List<Integer> getCupScore()
	{
		List<Integer> list = new ArrayList<Integer>();
		String[] arr = score_cup.split(",");
		for(int i = 0 ; i < arr.length;i++)
		{
			if(arr[i].equals("") || arr[i].equals(" "))continue;
			int score = Integer.parseInt(arr[i]);
			list.add(score);
		}
		return list;
	}
	public void setCupScore(List<Integer> list)
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0 ;i < list.size(); i++)
		{
			buf.append(list.get(i));
			if(i < list.size() -1)
				buf.append(",");
		}
		this.score_cup = buf.toString();
	}
	
	public List<Integer> getDanJiChangs()
	{
		String[] strs = this.changDanji.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < strs.length; i++ )
		{
			if(strs[i].equals("") || strs[i].equals(" "))
				continue;
			int num = Integer.parseInt(strs[i]);
			list.add(num);
		}
		return list;
	}
	public void setDanJiChangs(List<Integer> list)
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0 ;i < list.size(); i++)
		{
			buf.append(list.get(i));
			if(i < list.size() -1)
				buf.append(",");
		}
		this.changDanji = buf.toString();
		
	}
	
}
