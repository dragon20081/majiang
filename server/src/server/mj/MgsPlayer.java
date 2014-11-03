package server.mj;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import server.command.CommandMap;
import server.command.GlobalData;
import server.command.MaJiang_Fan;
import server.command.cmd.CCMD11056;
import server.command.cmd.CCMD11071;
import server.command.cmd.CCMD11072;
import server.command.cmd.CCMD11101;
import server.command.cmd.CCMD11102;
import business.Business;
import business.CmdRand;
import business.CountDao;
import business.conut.MJUserCharge;
import business.conut.Sts_UserCup;
import business.conut.Sts_UserDanji;
import business.entity.MJ_DayTask;
import business.entity.MJ_Role;
import business.entity.MJ_User;

import common.CommonBuffer;
import common.Log;

public class MgsPlayer extends SimpleChannelUpstreamHandler implements Comparable<MgsPlayer>,TimerTask{
	
	private static final Logger logger = Logger.getLogger(MgsPlayer.class.getName());
		
	private boolean closing = false;
	private String _name;
	private Channel _channel;
	public Map<Integer,List<ChannelBuffer>> CacheBufMap;
	public List<Integer> cacheMapKeys;
	private CmdRand cmdRand;
	private Business business;
	private List<Integer> shoupai;
	private List<Integer> peng;
	private List<List<Integer>> gang;
	private List<int[]>   chi;
	/**
	 *  玩家所有已打过的牌  [paiId, flag ]  flag  0 :无状态   1.被其他玩家 吃 |碰 | 杠
	 */
	private List<int[]> dapai;
	private List<Integer> dapaiList;
	private boolean ting  = false;
	private boolean tianting = false;
	private boolean tianhe   = false;
	private boolean dihe     = false;
	private boolean gangshanghua  = false;
	private boolean qianggang     = false;
	private CommandMap commandMap;
	/**
	 * 玩家下一张想要摸到的牌
	 */
	private List<Integer> wishNextPais;
	/**
	 * 玩家技能需要的牌
	 */
	private List<Integer> skillWishPais;
	/**
	 *  玩家已打牌数量
	 */
	private int     countDapai;
	/**
	 * 这局积分
	 */
	private int     score;
	/**
	 * 总积分
	 */
	private int  totalScore;

	/**
	 * 连杠
	 */
	private int liangang = 0;
	private MaJiang_Fan fan;
	/**
	 * 胡牌  自摸 点炮  点炮玩家
	 */
	public boolean zhunbei= false;
	private Map<Integer, MgsPlayer> huMap;
	
	private Map<Integer,int[]> fixedPai;
	private int   touchedPai;
	private int roomId = -1;
	/**
	 * 目前玩家不能自己修改位置信息
	 */
	private int locId = -1;
	private int feng = -1;
	private Room room;
	private int offLineTime  = -1;
	private List<Integer> jiao;
	
	public int disconnetct_flag = 0;//表示玩家是否被挤掉线了，如果挤掉线了就为1
	private CommonBuffer pattern;
	private int  preOperate  =  0; 
	
	//技能
	private int yun =  0;
	private int qi  =  0;
	private int tmpYun = 0;    // 临时运， 本回合有效，使用完自动清零
	private int maxQi 	= 100; //满  5点
	private int maxYun  = 5;//满  5点
	private  LinkedHashMap<Integer,Integer> skillMap;
	private PlayerSkill skillutil;
	//皇权 技能倒计圈数  开启技能是初始化为3;
	private int quan_hq =  0;
	
	private boolean onLine  = true;
	
	private boolean robot;
	
	public boolean offline = false;
	
	/**
	 * 玩家是否被禁言
	 */
	public boolean mute = false;
	public int time_jiejin = 0;
	public String platform = "";
	
	public boolean danjiOpen = false;
	public int danji_type = -1;
	
	public Sts_UserCup userCup = null;
	public Sts_UserDanji userDanji = null;
	public boolean cupOpen = false;
	public int cup_type = -1;
	//360登陆 access_tokenMapng
	private Map<String,String> tokenMap;
	//360登陆 userinfo
	private Map<String,String> userinfo_360;
	
	public MgsPlayer()
	{
		commandMap = new CommandMap();
		cmdRand  	=  	new CmdRand();
//		cmdRand.setSeed();
		business 	= 	new Business();
		pattern  	= 	new CommonBuffer();
		CacheBufMap = 	new HashMap<Integer, List<ChannelBuffer>>();
		cacheMapKeys=   new ArrayList<Integer>();
	}
	public MgsPlayer(boolean isRobot)
	{
		commandMap = new CommandMap();
		cmdRand  	=  	new CmdRand();
//		cmdRand.setSeed();
		business 	= 	new Business();
		pattern  	= 	new CommonBuffer();
		CacheBufMap = 	new HashMap<Integer, List<ChannelBuffer>>();
		cacheMapKeys=   new ArrayList<Integer>();
		robot  = isRobot;
	}
	public  MgsPlayer(int haveNOArg)
	{
		
	}
	public void initSkill()
	{
		skillMap =  new LinkedHashMap<Integer, Integer>();// 0 关闭  1开始//被动一直开启，主动条件开启
		String skills  = this.business.getPlayer().getEquipSkill();
		String[] strs = skills.split(",");
		for(int i = 0; i < strs.length;i++)
		{
			if("".equals(strs[i]) || " ".equals(strs[i]))continue;
			int skillId  = Integer.parseInt(strs[i]);
			int status   =  this.business.getPlayer().getSkillStatus(skillId);
			skillMap.put(skillId, status);
			Log.log("skill  "+this.getName() +":"+skillId +":" +status);
		}
		
//		skillMap.put(1, 1);
//		skillMap.put(2, 1);
//		skillMap.put(5, 1);
	}
	public List<Integer> getEquipSkill()
	{
		List<Integer> list = new ArrayList<Integer>();
		Iterator<Integer> it = this.skillMap.keySet().iterator();
		while(it.hasNext())
		{
			list.add(it.next());
		}
		return list;
	}
	public MgsPlayer(String name)
	{
		this._name = name;
	}
	
	public void setName(String name)
	{
		this._name = name;
	}
	
	public String getName()
	{
		return this._name;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public void setChannel(Channel channel)
	{
		this._channel = channel;
	}
	
	public Channel getChannel()
	{
		return this._channel;
	}
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		this.setChannel(e.getChannel());
//		logger.log(Level.INFO,"<<channelConnected!");
	}
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
//		logger.log(Level.INFO,"<<channelDisconnected!");
		//断开连接
		logger.info(this.getName()+"  socket channelDisconnected");
		stopHeartBeat();
		if(this.getRoom() != null)
			this.getRoom().offLine(this);  //掉线处理， 而不是离开房间
		Calendar now = Calendar.getInstance();
		long timeNum = now.getTimeInMillis()+28800000;
		this.offLineTime = (int) (timeNum / 1000);
//		MgsCache.getInstance().removeCache(this);
	
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.messageReceived(ctx, e);
		MgsEvent event = (MgsEvent) e.getMessage();
		if(this.closing)return;
			//直接执行
		if(event.cmd == 999)//测试360账号
		{
			commandMap.doCMD(this, event.cmd, event.message);
			return;
		}
		
		this.setBeatDown(10);
		if(event.cmd != 11112)
		{
			logger.info("reveive cmd:"+  event.cmd);
			this.business.saveUserOperate("接收指令:" + event.cmd);
		}else
		{
//			logger.info("heart beat:"+  this.getBeatDown());
		}
		
			if(event.cmdCode == 0){commandMap.doUnCheckCMD(this, event.cmd, event.message);  if(event.cmd != 11112)logger.info("uncheck cmd" + event.cmd); return;}            //直接执行
 			if(CacheBufMap.containsKey((Integer)event.cmdCode)){	this.cacheSend(event.cmdCode); logger.info("cacheSend:"+event.cmdCode);return;}                                 //cmdCode判断
 			if(event.cmdCode != 0)this.cmdRand.next();
			if(cmdRand.getSeed() != event.cmdCode)
			{ // cmdRand对不上， 掉线
				logger.info("cmdRand 错误， 掉线" +cmdRand.getSeed() + " client:"+ event.cmdCode);
				this.business.saveUserOperate("cmdRand 错误， 掉线   server" +cmdRand.getSeed() + " client:"+ event.cmdCode);
				CCMD11056 c56 = new CCMD11056();
				c56.audo_deal(this);
				return;
			}
			commandMap.doCMD(this, event.cmd, event.message);
	}
	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
		super.writeComplete(ctx, e);
//		logger.info("!!writeComplete");
	}
	public void send(Integer cmd, ChannelBuffer buf)
	{
		    if(buf == null)buf = ChannelBuffers.dynamicBuffer();
			ChannelBuffer data = ChannelBuffers.dynamicBuffer();
			data.writeInt(buf.readableBytes()+2);
			data.writeShort(cmd);
			data.writeBytes(buf);
			cacheBufMesg(data);
			Channel channel = this.getChannel();
			if(channel != null && channel.isConnected())
				channel.write(data);
//			this.cmdRand.next();
//			System.out.println(cmd+ " ---->" + data.readableBytes());
			if(cmd != 11112 && cmd != 11072)logger.info("messageSended!:"+ this.getName()+"   " + cmd);
	}
	/**
	 * @param cmd
	 * @param buf
	 * @param cache true 缓存消息
	 */
//	public void send(Integer cmd, ChannelBuffer buf,boolean cache)
//	{
//	    if(buf == null)buf = ChannelBuffers.dynamicBuffer();
//		ChannelBuffer data = ChannelBuffers.dynamicBuffer();
//		data.writeInt(buf.readableBytes()+2);
//		data.writeShort(cmd);
//		data.writeBytes(buf);
//		if(cache)cacheBufMesg(data);
//		Channel channel = this.getChannel();
//		if(channel != null && channel.isConnected())
//			channel.write(data);
//	}
	public void cacheSend(int seed)
	{
		List<ChannelBuffer> bufList =  this.CacheBufMap.get(seed);
		if(bufList == null || bufList.size() == 0)
		{
			Log.log("cacheSend:" +bufList);
			return;
		}
		Channel channel = this.getChannel();
		if(channel != null && channel.isConnected())
		{
			for(int i = 0; i < bufList.size();i++)
			{
				ChannelBuffer cacheBuffer = bufList.get(i);
				channel.write(cacheBuffer);
			}
		}
	}
	public void cacheBufMesg(ChannelBuffer buf)
	{
		int cmdkey = this.cmdRand.getSeed();
		if(cacheMapKeys.size() > 1)
		{
			int removeKey = cacheMapKeys.remove(0);
			if(this.CacheBufMap.containsKey(removeKey))
				this.CacheBufMap.remove(removeKey);
		}
		ChannelBuffer tmpbuf  = buf.copy();
		List<ChannelBuffer> list  = this.CacheBufMap.get(cmdkey);
		if(list == null)
		{
			list = new ArrayList<ChannelBuffer>();
			this.CacheBufMap.put(cmdkey, list);
			cacheMapKeys.add(cmdkey);
		}
		list.add(tmpbuf);
	}
	/**
	 * 获得包装消息
	 * @param cmd
	 * @param buf
	 * @return
	 */
	public ChannelBuffer coderCMD(Integer cmd, ChannelBuffer buf){
		ChannelBuffer data = ChannelBuffers.dynamicBuffer();
		data.writeInt(buf.readableBytes()+2);
//		logger.info(buf.readableBytes()+2);
		data.writeShort(cmd);
		data.writeBytes(buf);
		
		return data;
	}
	/**
	 *  获得手牌的buffer 
	 */
	public ChannelBuffer getShouPaiBuf()
	{
		//明牌  暗牌   摸牌    打牌 
		ChannelBuffer buf  = ChannelBuffers.dynamicBuffer();
		this.pattern.writeByteList(buf, this.getShoupai());
		this.pattern.writeByteList(buf, this.getPeng());
		this.pattern.writeByteArrList2(buf, this.getGang(),2);
		this.pattern.writeByteArrList(buf, this.getChi(), 3);
		
		this.getDapai();
		List<Integer> dapaiFlag = new ArrayList<Integer>();
		List<Integer> dapaiId = new ArrayList<Integer>();

		for(int i = 0 ; i < this.dapai.size();i++)
		{
			int[] tmp  = this.dapai.get(i);
			dapaiId.add(tmp[0]);
			if(tmp[1] != 0)dapaiFlag.add(i);
		}
		this.pattern.writeByteList(buf, dapaiId);
		this.pattern.writeByteList(buf, dapaiFlag);
		return buf;
	}
	/**
	 * 主动断开玩家与前台的链接
	 */
	public void closing()
	{
		this.closing = true;
		//发送断线消息
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		super.exceptionCaught(ctx, e);
		Log.error(this+" exceptionCaught() !");
		this.setChannel(null);
		e.getChannel().close();
		Log.log(e.getCause().toString());
	}
	/**
	 * 增加玩家当前装备角色经验值，一次加一点
	 * 如果升级检查是否解锁技能
	 */
	public void addRoleExp(int modExp)
	{
		MJ_User user = this.getBusiness().getPlayer();
		int roleId = user.getImage();
		if(roleId == -1)
		{
			Log.error("mgsPlayer.addRoleExp: "+this.getName()+"  image id == -1 ");
			return;
		}
		if(user.getEquipRole() == null || roleId != user.getEquipRole().getRoleId())
		{
			user.setEquipRole(null);
		}
		if(user.getEquipRole() == null)
		{
				Iterator<MJ_Role> roles = user.getRoles().iterator();
				while(roles.hasNext())
				{
					MJ_Role role = roles.next();
					if(role.getRoleId() == user.getImage())
					{
						user.setEquipRole(role);
						break;
					}
				}
		}
		if(user.getEquipRole() == null)
		{
			Log.error("mgsPlayer.addRoleExp: can't find image id " + user.getImage()+" in own roles Set");
			return;
		}
		MJ_Role equipRole = user.getEquipRole();
		if(equipRole.getLevel() >= GlobalData.MAXROLE_LEVEL) //满级
		{
			return;
		}
		equipRole.setExp(equipRole.getExp() + modExp);
		//发送增加经验消息
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.mgs_ModRoleExp(this, equipRole, modExp);
		//检查是否升级
		int[] info = GlobalData.getRoleData(equipRole.getRoleId());
		boolean upLevel = false;
		if(equipRole.getExp() >= equipRole.getLevel() * info[3] )
		{
			equipRole.setLevel(equipRole.getLevel() + 1);
			upLevel = true;
		}
		//检查技能解锁
		if(upLevel)
		{
			if(equipRole.getLevel() == info[1])
			{
				//解锁技能
				CCMD11102 cmd102 = new CCMD11102();
				cmd102.setPlayer(this);
				cmd102.unlockSkill(user, info[2]);
			}
		}
		
	}
	
	@Override
	public String toString() {
		return this._name;
	}
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public CmdRand getCmdRand() {
		return cmdRand;
	}
	public void setCmdRand(CmdRand cmdRand) {
		this.cmdRand = cmdRand;
	}
	public List<Integer> getShoupai() {
		return shoupai;
	}
	public void setShoupai(List<Integer> shoupai) {
		this.shoupai = shoupai;
	}
	public int getTouchedPai() {
		return touchedPai;
	}
	public void setTouchedPai(int touchedPai) {
		this.touchedPai = touchedPai;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Map<Integer, int[]> getFixedPai() {
		return fixedPai;
	}
	public void setFixedPai(Map<Integer, int[]> fixedPai) {
		this.fixedPai = fixedPai;
	}
	public int getOffLineTime() {
		return offLineTime;
	}
	public void setOffLineTime(int offLineTime) {
		this.offLineTime = offLineTime;
	}
	public List<Integer> getPeng() {
		return peng;
	}
	public void setPeng(List<Integer> peng) {
		this.peng = peng;
	}

	public List<List<Integer>> getGang() { 
		return gang;
	}
	public void setGang(List<List<Integer>> gang) {
		this.gang = gang;
	}

	public List<int[]> getChi() {
		return chi;
	}
	public void setChi(List<int[]> chi) {
		this.chi = chi;
	}
	public List<int[]> getDapai() {
		return dapai;
	}
	public void setDapai(List<int[]> dapai) {
		this.dapai = dapai;
	}

	public boolean isClosing() {
		return closing;
	}

	public void setClosing(boolean closing) {
		this.closing = closing;
	}

	public boolean isTing() {
		return ting;
	}

	public void setTing(boolean ting) {
		this.ting = ting;
	}

	public boolean isTianting() {
		return tianting;
	}

	public void setTianting(boolean tianting) {
		this.tianting = tianting;
	}

	public boolean isTianhe() {
		return tianhe;
	}

	public void setTianhe(boolean tianhe) {
		if(tianhe)
		{
			System.out.println("");
		}
		this.tianhe = tianhe;
	}

	public boolean isDihe() {
		return dihe;
	}

	public void setDihe(boolean dihe) {
		this.dihe = dihe;
	}

	public boolean isQianggang() {
		return qianggang;
	}

	public void setQianggang(boolean qianggang) {
		this.qianggang = qianggang;
	}

	public boolean isGangshanghua() {
		return gangshanghua;
	}

	public void setGangshanghua(boolean gangshanghua) {
		this.gangshanghua = gangshanghua;
	}

	public MaJiang_Fan getFan() {
		return fan;
	}
	public void setFan(MaJiang_Fan fan) {
		this.fan = fan;
	}
	public int getCountDapai() {
		return countDapai;
	}
	public void setCountDapai(int countDapai) {
		this.countDapai = countDapai;
	}
	public int getPreOperate() {
		return preOperate;
	}
	public void setPreOperate(int preOperate) {
		this.preOperate = preOperate;
	}
	public List<Integer> getJiao() {
		return jiao;
	}
	public void setJiao(List<Integer> jiao) {
		this.jiao = jiao;
	}
	public Map<Integer, MgsPlayer> getHuMap() {
		return huMap;
	}
	public void setHuMap(Map<Integer, MgsPlayer> huMap) {
		this.huMap = huMap;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public List<Integer> getDapaiList() {
		return dapaiList;
	}

	public void setDapaiList(List<Integer> dapaiList) {
		this.dapaiList = dapaiList;
	}

	public int getYun() {
		if(yun > 5)
			return 5;
		return yun;
	}

	public void setYun(int yun) {
		this.yun = yun;
		if(this.yun < 0)
		{
			this.skillutil.checkSkill_statusChange(this);
		}
	}

	public List<Integer> getWishNextPais() {
		return wishNextPais;
	}

	public void setWishNextPais(List<Integer> wishNextPais) {
		this.wishNextPais = wishNextPais;
	}

	public LinkedHashMap<Integer, Integer> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(LinkedHashMap<Integer, Integer> skillMap) {
		this.skillMap = skillMap;
	}
	public int getMaxQi() {
		return maxQi;
	}
	public void setMaxQi(int maxQi) {
		this.maxQi = maxQi;
	}
	public int getMaxYun() {
		return maxYun;
	}
	public void setMaxYun(int maxYun) {
		this.maxYun = maxYun;
	}
	public int getQi() {
		return qi;
	}
	public void setQi(int qi) {
		if(qi < 0) qi = 0;
		this.qi = qi;
	}
	/**
	 *皇权 技能倒计圈数  开启技能是初始化为3;
	 */
	public int getQuan_hq() {
		return quan_hq;
	}
	public void setQuan_hq(int quan_hq) {
		this.quan_hq = quan_hq;
	}
	public int getTmpYun() {
		return tmpYun;
	}
	public void setTmpYun(int tmpYun) {
		/*if(tmpYun >5)tmpYun = 5;
		else if(tmpYun< -5)tmpYun = -5;*/
		this.tmpYun = tmpYun;
	}
	public PlayerSkill getSkillutil() {
		return skillutil;
	}
	public void setSkillutil(PlayerSkill skillutil) {
		this.skillutil = skillutil;
	}
	
	public boolean isOnLine() {
		return onLine;
	}
	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}
	public void increaseQi()
	{
		Log.log("--->increaseQi ");
		if(this.qi < this.maxQi)
		{
			this.qi++;
		}
	}
	public void increaseQi(int num)
	{
		if(num < 0)
		{
			Log.log("error");
		}
		if(this.qi + num < this.maxQi)
		{
			this.qi += num;;
		}else if(this.qi + num >= this.maxQi)
		{
			this.qi = this.maxQi;
		}
		
	}
	public int compareTo(MgsPlayer o) {
		
		if(this.totalScore > o.getTotalScore())
			return -1;
		return 1;
	}
	public List<Integer> getSkillWishPais() {
		return skillWishPais;
	}
	public void setSkillWishPais(List<Integer> skillWishPais) {
		this.skillWishPais = skillWishPais;
	}
	public int getLiangang() {
		return liangang;
	}
	public void setLiangang(int liangang) {
		Log.log("连杠: " + liangang);
		this.liangang = liangang;
	}
	public int getLocId() {
		return locId;
	}
	public void setLocId(int locId) {
		this.locId = locId;
	}
	public int getFeng() {
		return feng;
	}
	public void setFeng(int feng) {
		this.feng = feng;
	}
	
	/**
	 * 判断手中的牌的数量，看是否可以打牌
	 */
	public boolean canDapai()
	{
		int totalNum = 0;
		
		totalNum = this.gang.size()* 3 + this.chi.size() * 3 + this.peng.size() * 3+ this.getShoupai().size();
		if(this.getTouchedPai() != -1) totalNum += 1;
		logger.info(this.getName() + "canDapai:totalNum  ---> " + totalNum);
		if(totalNum  <= 13)return false;
		else 
			return true;
	}
	
	/**
	 * 检查在线时间 奖励
	 */
	public void checkOnlineReward()
	{
		MJ_User user = this.business.getPlayer();
		if(user.getTaskJinDu().equals(""))return;
		
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Iterator<Integer> it = map.keySet().iterator();
		boolean flag =false;
		while(it.hasNext())
		{
			int key = it.next();
			MJ_DayTask task = Global.tasks.get(key);
			List<Object> value = map.get(key);
			int taskStatus = (Integer) value.get(1);
			if(taskStatus == MJ_DayTask.TASK_NOCOMPLETE)
			{
				if(task.getOnLineTime() > 0)
				{
					//设置进度
					String[] parr = ((String)value.get(2)).split(":");
					
					int newMiu =  user.getOnLineTime()/60;
					value.set(2, parr[0]+":" + newMiu);
					if(user.getOnLineTime()/60 >= task.getOnLineTime())
					{
						 value.set(1, MJ_DayTask.TASK_COMPLETE);
							//此任务完成， 通知前台
							CCMD11072 cmd072 = new CCMD11072();
							cmd072.auto_deal(this, value);
							flag = true;
							
							CheckTask check1 = new CheckTask();
							check1.mod_stsTask(task);
							
					}
				}
			}
		}
		user.setTaskJinduMap(map);
		if(flag || user.getOnLineTime() %60 == 0)
		{
			this.business.savePlayer(user);
			 map = 	user.getTaskJinduMap();
			Iterator<Integer> it1 = map.keySet().iterator();
			while(it1.hasNext())
			{
				int key = it1.next();
				List<Object> value = map.get(key);
				int taskStatus = (Integer) value.get(1);
				if(taskStatus == MJ_DayTask.TASK_NOCOMPLETE)
				{
					CCMD11072 cmd072 = new CCMD11072();
					cmd072.auto_deal(this, value);
				}
			}
		
		}
	}
	
	private int beatDown_1 = 0;
	
	
	public int getBeatDown() {
		return beatDown_1;
	}
	public void setBeatDown(int beatDown) {
		this.beatDown_1 = beatDown;
	}

	private Timeout timeout;
	@Override
	public void run(Timeout arg0) throws Exception {
		
		if(this.offline)return;
		//心跳计时， 统计在线时间
		MJ_User user = this.business.getPlayer();
		user.setOnLineTime(user.getOnLineTime() + 5);
		//检查任务完成情况
		checkOnlineReward();
		this.setBeatDown(this.getBeatDown() - 1);
		if(this.getBeatDown() <= 0)
		{
			logger.info("heart beat offline");
			
			if(this.getChannel() != null)this.getChannel().close();
			this.setChannel(null);
			//掉线
			if(this.getRoom() != null)
				this.getRoom().offLine(this);  //掉线处理， 而不是离开房间
			logger.info("beat offline: "+ this.getName());
			Calendar now = Calendar.getInstance();
			long timeNum = now.getTimeInMillis()+28800000;
			this.offLineTime = (int) (timeNum / 1000);
			return;
		}
		 this.timeout = Global.timer.newTimeout(this, 5 * 1000,
				 TimeUnit.MILLISECONDS);
		 
			Channel channel = this.getChannel();
			if(channel != null && channel.isConnected())
			{
			}else
			{
			}
		 this.send(11112,null);
	}
	public void startHeartBeat()
	{
		if(this.offline)return;
		this.setBeatDown(10);
		if(timeout!= null)timeout.cancel();
		 this.timeout = Global.timer.newTimeout(this, 5 * 1000,
				 TimeUnit.MILLISECONDS);
		 this.send(11112,null);
	}
	public void stopHeartBeat()
	{
		if(timeout!= null)timeout.cancel();
		this.setBeatDown(10);
	}
	
	/**
	 * 保存玩家金币钻石流动情况
	 */
	public void saveUserChargeRec(String cmd,int gold,int modGold,int afterGold,int dia,int modDia,int afterDia,String info)
	{
		MJ_User user = this.business.getPlayer();
		MJUserCharge charge = new MJUserCharge();
		charge.setUid(user.getUid());
		charge.setName(user.getName());
		charge.setNickName(user.getNick());
		charge.setTime(ServerTimer.distOfSecond(Calendar.getInstance()));
		charge.setTimeStr(ServerTimer.getNowString());
		
		charge.setGold(gold);
		charge.setModGold(modGold);
		charge.setAfterGold(afterGold);
		charge.setDia(dia);
		charge.setModDia(modDia);
		charge.setAfterDia(afterDia);
		charge.setCmd(cmd);
		charge.setInfo(info);
		this.business.saveDataObject(charge);
	}
	public void saveUserChargeRec(MJ_User user,String cmd,int gold,int modGold,int afterGold,int dia,int modDia,int afterDia,String info)
	{
		MJUserCharge charge = new MJUserCharge();
		charge.setUid(user.getUid());
		charge.setName(user.getName());
		charge.setNickName(user.getNick());
		charge.setTime(ServerTimer.distOfSecond(Calendar.getInstance()));
		charge.setTimeStr(ServerTimer.getNowString());
		
		charge.setGold(gold);
		charge.setModGold(modGold);
		charge.setAfterGold(afterGold);
		charge.setDia(dia);
		charge.setModDia(modDia);
		charge.setAfterDia(afterDia);
		charge.setCmd(cmd);
		charge.setInfo(info);
		
		CountDao cdao = new CountDao();
		cdao.saveSts_Object(charge);
	}
	
	
	public String getAllPaiStr()
	{
		String str =this.getName()+ "   shoupai:"+this.getShoupai().toString() +" peng:" + this.getPeng().toString() + " chi:"+ this.getChiStr()+""+ this.getGangStr() +" touchPai:"+this.getTouchedPai();
		return str;
	}
	public String getChiStr()
	{
		StringBuffer str = new StringBuffer();
		for(int i = 0 ;i < this.getChi().size();i++)
		{
			int[] arr= this.getChi().get(i);
			if(arr.length > 3)
				str.append(arr[0] +","+arr[1]+"," +arr[2]+";");
		}
		return str.toString();
	}
	public String getGangStr()
	{
		StringBuffer buf = new StringBuffer();
		for(int i = 0;i < this.getGang().size();i++)
		{
			buf.append(getGang().get(i).toString()+";");
		}
		
		return buf.toString();
	}
	public Map<String, String> getTokenMap() {
		return tokenMap;
	}
	public void setTokenMap(Map<String, String> tokenMap) {
		this.tokenMap = tokenMap;
	}
	public Map<String, String> getUserinfo_360() {
		return userinfo_360;
	}
	public void setUserinfo_360(Map<String, String> userinfo_360) {
		this.userinfo_360 = userinfo_360;
	}
	
	
	/**
	 * 	
	private String cmd = "";
	private int uid = 0;
	private String name = "";
	private String nickName = "";
	private int gold = 0;
	private int modGold = 0;
	private int afterGold = 0;
	private int dia = 0;
	private int modDia = 0;
	private int afterDia = 0;
	private String info = "";
	private int time = 0;
	private String timeStr = "";
	 */
	
}
