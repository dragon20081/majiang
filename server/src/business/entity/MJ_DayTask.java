package business.entity;

/**
 * 每日任务
 * @author xue
 */
public class MJ_DayTask {
	
	private int id;
	
	private int taskId;
	//任务奖励
	private int gold;
	private int dia;
	private String props = "";
	//任务描述
	private String taskDesc = "";
	//任务完成条件
	//当天第一次登陆
	private int firstLogin;
	private int onLineTime;
	private int winPan;
	private int playPan;
	private int manGuanPan;
	private int manGuanFan;
	private int zimoPan;
	private int dianpaoPan;
	private int finishJuqing;
	
	private int taskParam1 ;
	private int taskParam2;
	
	//任务类型: 日常， 持续任务   1 每日任务   2 持续性任务
	private int taskType = 0;
	//初始条件
	private String initProgress = "";
	
	
	public static final int  TASK_RICHANG = 1;
	public static final int  TASK_CHIXU = 2;
	
	//任务状态 :1 未完成  2已完成 3已领取 
	public static final int TASK_NOCOMPLETE = 1;
	public static final int TASK_COMPLETE 	= 2;
	public static final int TASK_YILINGQU 	= 3;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public int getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(int firstLogin) {
		this.firstLogin = firstLogin;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public int getOnLineTime() {
		return onLineTime;
	}
	public void setOnLineTime(int onLineTime) {
		this.onLineTime = onLineTime;
	}
	public String getInitProgress() {
		return initProgress;
	}
	public void setInitProgress(String initProgress) {
		this.initProgress = initProgress;
	}
	public int getWinPan() {
		return winPan;
	}
	public void setWinPan(int winPan) {
		this.winPan = winPan;
	}
	public int getPlayPan() {
		return playPan;
	}
	public void setPlayPan(int playPan) {
		this.playPan = playPan;
	}
	public int getManGuanPan() {
		return manGuanPan;
	}
	public void setManGuanPan(int manGuanPan) {
		this.manGuanPan = manGuanPan;
	}
	public int getZimoPan() {
		return zimoPan;
	}
	public void setZimoPan(int zimoPan) {
		this.zimoPan = zimoPan;
	}
	public int getDianpaoPan() {
		return dianpaoPan;
	}
	public void setDianpaoPan(int dianpaoPan) {
		this.dianpaoPan = dianpaoPan;
	}
	public int getManGuanFan() {
		return manGuanFan;
	}
	public void setManGuanFan(int manGuanFan) {
		this.manGuanFan = manGuanFan;
	}
	public int getFinishJuqing() {
		return finishJuqing;
	}
	public void setFinishJuqing(int finishJuqing) {
		this.finishJuqing = finishJuqing;
	}
	public int getTaskParam1() {
		return taskParam1;
	}
	public void setTaskParam1(int taskParam1) {
		this.taskParam1 = taskParam1;
	}
	public int getTaskParam2() {
		return taskParam2;
	}
	public void setTaskParam2(int taskParam2) {
		this.taskParam2 = taskParam2;
	}
	
	
}
