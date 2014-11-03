package server.mj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import business.CountDao;
import business.conut.Sts_Changci;
import business.entity.MJ_User;

import server.command.Algorithm_Hu;
import server.command.CMD;
import server.command.GroupShoupai;
import server.command.MaJiang_Fan;
import server.command.MajiangPai;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PatternPai;
import server.command.SCMD3;
import server.command.SortByFan;
import server.command.SortMaJiang;
import server.command.SortPlayer;
import server.command.cmd.CCMD11004;
import server.command.cmd.CCMD11006;
import server.command.cmd.CCMD11008;
import server.command.cmd.CCMD11009;
import server.command.cmd.CCMD11011;
import server.command.cmd.CCMD11014;
import server.command.cmd.CCMD11024;
import server.command.cmd.CCMD11031;
import server.command.cmd.CCMD11054;
import server.command.cmd.CCMD11055;
import server.command.cmd.CCMD11101;
import server.command.cmd.CCMD11111;
import server.command.cmd.SCMD11002;
import server.command.cmd.SCMD11003;
import server.command.cmd.SCMD11004;
import server.command.cmd.SCMD11009;
import server.command.cmd.SCMD11022;
import server.command.cmd.SCMD11025;

import common.*;

/**
 * 1.房间创建时 对应模式房间数+1 2.房间销毁时，对应模式房间数-1
 * 
 * changfeng 多人场， 打完一圈改变， 暂时实现
 * 
 * @author xue
 */
public class Room implements MgsRoom, Comparable<Room>, TimerTask {

	
	private static final Logger logger = Logger.getLogger(Room.class.getName());
	// private static final Logger logger =
	// Logger.getLogger(Room.class.getName());
	private ChannelGroup channelGroup = new DefaultChannelGroup();
	public Hashtable<Integer, MgsPlayer> players = new Hashtable<Integer, MgsPlayer>();
	// public Map<Integer,MgsPlayer> locationPlayers = new Hashtable<Integer,
	// MgsPlayer>();

	public static final int MIN_CHIPS = 100;

	public static final int TIME_DAPAI = 1;
	public static final int TIME_DEALPAI = 2;
	public static final int TIME_QIANGGANGHU = 3;
	public static final int TIME_READY = 4;
	public static final int TIME_ROBOT = 5;
	public static final int TIME_HEART = 6;
	public static final int BASE_SCORE = 100;
	public static final int BASE_GOLD = 10;

	public int MAX_COUNT = 4;
	public int room_state = 1;
	public List<MgsPlayer> leavedPlayer = new ArrayList<MgsPlayer>();
	public List<MgsPlayer> tuoguanPlayer = new ArrayList<MgsPlayer>();
	private int roomID = 0;
	private String roomName = "";
	private MgsPlayer manager; // 房主
	private MgsPlayer zhuang = null; // 房间 ID 0 --- >MAX_COUNT-1
	private int zhuangId = -1;
	private MgsPlayer curPlayer;
	private int curPaiId;
	private Timeout timeout;
	private int timeFlag;

	private int changfeng = 0; // 1 2 ,3, 4 4个方位

	private int playerLimit = 4; // 人数限制 2人 4人 0 无限制
	private int skillLimit = 1; // 1可以使用 2不能使用 0 无限制
	private int chipsLimit = 200; // 0 无限制 100， 200， 500筹码
	private int quanLimit = 1; // 1, 4
	private int beishu = 10;

	private static final int DEFALUT_SKILL = 1;
//	private static final int DEFALUT_CHIPS = 500;
	private static final int DEFALUT_QUAN = 4;

	private Map<Integer, Integer> map; // 拍堆s
	public List<Integer> IDList;
	private int[] fixOrder;
	private int leftPai;

	/** * 当前圈的把数 */
	private int cur_bashu = 1;
	private int cur_quanshu = 1;
	private Map<Integer, GroupShoupai> groupHupai;
	private int countDapai = 0;

	private boolean tiantingFlag = true;
	private int fengkeStartId = 0;

	public int lianzhuang = 0;

	public int lastHuPai = -1;
	private int totalChips = 0;
	private CountDao cdao;
	
	public boolean enteredGame = false;

	public CheckTask checkTask = new CheckTask();
	
	public boolean firtPan = true;
	// ///////////////////////////////////////////////////////////////
	// ********************功能函数**********************************
	/**
	 * @param player
	 */
	public synchronized void addPlayer(MgsPlayer player) {

		if (this.players.size()  >= 4) {
			// logger.log(Level.WARNING, this +"roomid:"+this.getRoomID()
			// +"is full","  addPlayer fail");
			return;
		}
		if (player.getChannel() != null)
			this.channelGroup.add(player.getChannel());
		int i = 1;
		for (; i <= 4; i++) {
			MgsPlayer other = players.get(i);
			if (other != null)
				continue;
			// player.setRoomId(i);
			// this.locationPlayers.put(i, player);
			player.setLocId(i);
			player.setRoom(this);
			player.initSkill();
			Map<Integer, Integer> skills = player.getSkillMap();
			players.put(i, player);
			player.setQi(0);
			player.setYun(0);
			player.zhunbei = false;
//			player.startHeartBeat();
			// logger.log(Level.INFO, player +
			// " added to room at:"+player.getRoomId());
			break;
		}
		// if(this.players.size() == MAX_COUNT)
		// {
		// try_startRoom();
		// initRoomCondition();
		// cutChips();
		// }
	}
	
	public void resetFirtRoom()
	{
		int i = 0;
		for(i = 1;i <= this.players.size();i++)
		{
			MgsPlayer p = this.players.get(i);
			if(p == null) continue;
			Map<Integer, Integer> map = p.getSkillMap();
			if(map.containsKey(7))
			{
				map.put(7, 1);
			}
		}
	}

	public synchronized void tryStart() {
		if (this.players.size() == MAX_COUNT) {
			boolean b = try_startRoom();
			if (!b)
			{
				tryStartFail();				
				return;
			}
			
			if (this.room_state != MgsRoom.CREATED
					&& this.room_state != MgsRoom.ENDED)
			{
				Log.info("tryStart 2:" + room_state);
				tryStartFail();		
				return;
			}
			enteredGame = true;
			readyP_Count = null;
			this.started();
			initRoomCondition();
			cutChips();
			start_readyTimer();
			
			Log.info("--->tryStart()");
		}
	}
	
	public synchronized void tryStartFail()
	{
		MyArray arr = new MyArray();
		arr.push(false);
		MyByteArray tmpbuf = new MyByteArray();
		tmpbuf.write(arr);
		this.manager.send(11058, tmpbuf.getBuf());
	}

	/**
	 * 4人场，游戏开始，所有人上缴筹码,兑换成积分，游戏结束，根据积分分配
	 */
	public synchronized void cutChips() {
		this.totalChips = 0;
		if (this.playerLimit != 4)
			return;
		for (int i = 1; i <= this.MAX_COUNT; i++) {
			MgsPlayer p = this.players.get(i);
			if(p == null)continue;
			MJ_User user = p.getBusiness().getPlayer();
			
			int beforeGold 	= user.getGold();
			int beforeDia 	= user.getDianQuan();
			int modGold 	=-this.MIN_CHIPS;
			int modDia = 0;
			user.setGold(user.getGold() - this.MIN_CHIPS);
			p.getBusiness().savePlayer(user);
			this.totalChips += MIN_CHIPS;
			
			p.saveUserChargeRec("cutChips",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "对战扣除初始筹码，金币: -"+MIN_CHIPS+"");
			//保存金币流动数据
			
		}
	}

	public synchronized void sendRoomInfo() {
		if (this.players.size() == MAX_COUNT) {
			SCMD11022 scmd = new SCMD11022();
			scmd.room = this;
			this.SendRoomBroadcast(11022, scmd.getBytes());
		}
	}

	public synchronized void boradcastPlayerIn(MgsPlayer p) {
		CCMD11054 cmd = new CCMD11054();
		cmd.auto_deal(p);
	}

	public List<Integer> fengkeArray = null;

	public synchronized boolean try_startRoom() {
		if (this.players.size() < MAX_COUNT)
			return false;
		this.leavedPlayer = new ArrayList<MgsPlayer>();
		this.tuoguanPlayer = new ArrayList<MgsPlayer>();
		int i = 0;
		for (i = 1; i <= 4; i++) {
			MgsPlayer tmpp = this.players.get(i);
			if(tmpp == null)continue;
			if (!tmpp.zhunbei) {
				return false;
			}
		}
		if (fengkeArray == null) {
			// 随机设置东南西北位置
			List<Integer> list = null;
			if (this.playerLimit == 2)
				list = MyArrays.asList(1, 3);
			if (this.playerLimit == 4)
				list = MyArrays.asList(1, 2, 3, 4);
			fengkeArray = MyArrays.asList(-1, -1, -1, -1);

			for (i = 1; i <= this.players.size(); i++) {
				int rid = list.get((int) (list.size() * Math.random()));
				list.remove((Integer) rid);
				this.players.get(i).setRoomId(rid);

				fengkeArray.set(rid - 1, this.players.get(i).getLocId());
				this.players.get(i).setYun(0);
				this.players.get(i).setQi(0);
			}
			for (i = 0; i < fengkeArray.size(); i++) {
				if (fengkeArray.get(i) == -1)
					fengkeArray.remove(i);
			}
			initZhuangId();
			
			logger.info("zhuangid:"+ this.zhuangId + " fengkeArray:" + fengkeArray.toString());
			for(i = 1; i <= 4; i++)
			{
				MgsPlayer tmp = this.players.get(i);
				if(tmp == null) continue;
				logger.info(tmp.getName() +" LOC:"+tmp.getLocId() +" RID:" + tmp.getRoomId());
			}
		}
		// initfengke();

		// 广播位置
		MyArray arr = new MyArray();
		arr.push(true);
		for (i = 1; i < 4; i++) {
			MgsPlayer pp = this.players.get(i);
			if (pp == null)
				continue;
			arr.push(MyArrays.asList(pp.getLocId(), pp.getRoomId()));
		}
		MyByteArray tmpbuf = new MyByteArray();
		tmpbuf.write(arr);
		this.SendRoomBroadcast(11058, tmpbuf.getBuf());

		Global.rManager.removeRoomFromWaitList(this);
		return true;
	}

	public synchronized void initfengke() {
		// List<Integer> fengArr = null ;
		// if(this.playerLimit == 2)
		// fengArr = MyArrays.asList(1,3);
		// if(this.playerLimit == 4)
		// fengArr = MyArrays.asList(1,2,3,4);
		// for(int i = zhuangLoc;i < fengkeArray.size();i++)
		// {
		// int loc = fengkeArray.get(i);
		// MgsPlayer p = this.players.get(loc);
		// p.setFeng(fengArr.get(0));
		// fengArr.remove(0);
		// if(i == fengkeArray.size() -1)
		// i = 0;
		// if(fengArr.size() == 0)break;
		// }
	}

	public synchronized void initRoomCondition() {
		if (this.quanLimit == 0)
			this.chipsLimit = Room.DEFALUT_QUAN;
		if (this.skillLimit == 0)
			this.chipsLimit = Room.DEFALUT_SKILL;
	}

	public synchronized void start() {
		init();
		// readyP_Count = new int[this.MAX_COUNT];
//		if (this.leavedPlayer.size() > 0) {
//			ChannelBuffer uids = MgsCache.getInstance().removeCacheList(
//					this.leavedPlayer, true);
//			this.SendRoomBroadcast(11011, uids); // 广播这些玩家掉线了
//		}
	}

	public synchronized void init() {
		int i = 0;
		initPaidui();
		initZhuang();
		this.setCurPlayer(this.getZhuang());
		if (this.getZhuang() == null) {
			Log.log("this.getZhuang() == null");
		}
		// System.out.println("zhuang：-->"+this.getZhuang().getName() + ":" +
		// this.getZhuangId());
		hu_players = null;
		tiantingFlag = true;
		this.fufen = false;
		haveNoGoldP = null;
		countDapai = 0;
		// this.manager = null;
		List<MgsPlayer> yunPlayer = new ArrayList<MgsPlayer>();
		for (i = 1; i <= 4; i++) {
			MgsPlayer p = this.players.get(i);
			if (p == null)
				continue;
		//	p.stopHeartBeat();
			p.zhunbei = false;
			yunPlayer.add(p);
			p.setTouchedPai(-1);
			p.setFixedPai(new HashMap<Integer, int[]>());
			p.setShoupai(new ArrayList<Integer>());
			p.setGang(new ArrayList<List<Integer>>());
			p.setPeng(new ArrayList<Integer>());
			p.setChi(new ArrayList<int[]>());
			p.setDapai(new ArrayList<int[]>());
			p.setDapaiList(new ArrayList<Integer>());
			p.setCountDapai(0);
			p.setHuMap(new HashMap<Integer, MgsPlayer>());
			p.setJiao(new ArrayList<Integer>());
			p.setFan(null);
			p.setScore(0);
			p.setWishNextPais(new ArrayList<Integer>());
			p.setTianting(false);
			p.setTing(false);
			p.setQianggang(false);
			p.setTianhe(false);
			p.setDihe(false);
			p.setQuan_hq(3);
			// p.setYun(0);
			// p.setQi(0);
			p.setTmpYun(0);
			p.setSkillWishPais(new ArrayList<Integer>());
			p.setSkillutil(new PlayerSkill());
			p.initSkill();
			// if(this.manager == null)this.manager = p;
		}
		// 初始化手牌 //player用yun进行排序 //依次初始化
		SortPlayer.getInstance().sortByYun(yunPlayer);
		for (i = 0; i < yunPlayer.size(); i++) {
			MgsPlayer tmpp = yunPlayer.get(i);
			int num = 13;
			List<Integer> pai = this.getInitPaizu(num, tmpp.getYun());
			tmpp.setShoupai(pai);
			if (tmpp == this.getZhuang()) {
				int zNext = this.getNextRandPai();
				tmpp.setTouchedPai(zNext);
				logger.info("init pai:" + tmpp.getName() +"  "+ tmpp.getShoupai().toString() +" " + zNext);
			}else
			{
				logger.info("init pai:" + tmpp.getName() +"  "+ tmpp.getShoupai().toString() );
			}
		}
	}

	/**
	 * 准备的玩家数
	 */
	private int[] readyP_Count;

	/**
	 * 收到所有玩家手牌请求后开始第一次计时
	 */
	public synchronized void startFirstTimer(MgsPlayer p) {
		if (p != null) {
			MJ_User user = p.getBusiness().getPlayer();
			if (room_state == MgsRoom.DESTORYED || user.getGold() <= 0) {
				this.cancelTimeOut();
				Log.info("startFirstTimer:" + room_state  + ": " + user.getGold());
				return;
			}
		}
		int i = 0;
		if (readyP_Count == null) {
			readyP_Count = new int[] { -1, -1, -1, -1 };
			// readyP_Count = new int[this.MAX_COUNT];
			for (i = 1; i <= 4; i++) {
				MgsPlayer tmpp = this.players.get(i);
				if (tmpp != null)
					readyP_Count[i - 1] = 0;
			}
		}
		if (p != null)
			readyP_Count[p.getLocId() - 1] = 1;
		for (i = 0; i < readyP_Count.length; i++) {
			MgsPlayer tmpp = this.players.get(i);
			if (tmpp != null &&tmpp.offline)
				readyP_Count[i] = 1;
			if (readyP_Count[i] == 0)
			{
				Log.info("startFirstTimer 3:" + readyP_Count[i] + " " + i);
				return;
			}
		}
		Log.info("--->fapai");
		start();
		// 开始第一次计时(客户端自己取得手牌数据,到打出第一张牌)
		// 广播所有玩家手牌数据同时封装房间信息
		for (i = 1; i <= 4; i++) {
			MgsPlayer tmpp = this.players.get(i);
			if (tmpp == null)
				continue;
			sendShouPaiMsg(tmpp); // 发送手牌信息
		}
		this.start_chupaiTimer();
		
		if(this.firtPan)
		{
			this.resetFirtRoom();
		}
		
		for (i = 1; i <= 4; i++) {
			MgsPlayer tmpp = this.players.get(i);
			if (tmpp == null)
				continue;
			tmpp.getSkillutil().checkInitSkill(tmpp);
			
			if(tmpp.offline)
				sendOfflineMsg(tmpp);
		}
	}
	public synchronized void sendOfflineMsg(MgsPlayer p)
	{
		MyArray arr = new MyArray();
		arr.push(p.getLocId());
		arr.push(1);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		SendRoomBroadcast(11056, buf1.getBuf());
		Log.info(p.getName()+"  掉线消息发送 1");
	}
	

	/**
	 * //发送手牌信息
	 */
	public synchronized void sendShouPaiMsg(MgsPlayer p) {
		// SCMD3 smcd3 = new SCMD3();
		SCMD11003 scmd = new SCMD11003();
		this.SendRoomBroadcast(11003, scmd.getBytes(p));
		// ChannelBuffer buf1 =scmd.getBytes(p);
		// buf1 = p.coderCMD(11003, buf1);
		// smcd3.list.add(buf1);
		// SCMD11022 scmd2 = new SCMD11022();
		// ChannelBuffer buf2 =scmd2.getBytes(p);
		// buf2 = p.coderCMD(11022, buf2);
		// smcd3.list.add(buf2);
		// this.SendRoomBroadcast(3, smcd3.getBytes());
		String nick = p.getBusiness().getPlayer().getName();
		System.out.println("发送手牌----->" + nick + ":"
				+ p.getBusiness().getPlayer().getNick() + p.getRoomId());

	}

	public synchronized void start_chupaiTimer() {
		cancelTimeOut();
		 this.timeout = Global.timer.newTimeout(this, 20 * 1000,
		 TimeUnit.MILLISECONDS);
		this.setTimeFlag(Room.TIME_DAPAI);
	}

	public synchronized void start_dealTimer() {
		cancelTimeOut();
		 this.timeout = Global.timer.newTimeout(this, 15 * 1000,
		 TimeUnit.MILLISECONDS);
		this.setTimeFlag(Room.TIME_DEALPAI);
	}

	public synchronized void start_qiangganghuTimer() {
		cancelTimeOut();
		 this.timeout = Global.timer.newTimeout(this, 15 * 1000,
		 TimeUnit.MILLISECONDS);
		this.setTimeFlag(Room.TIME_QIANGGANGHU);
	}

	public synchronized void start_readyTimer() {
		cancelTimeOut();
		 this.timeout = Global.timer.newTimeout(this, 30 * 1000,
		 TimeUnit.MILLISECONDS);
		this.setTimeFlag(Room.TIME_READY);
	}

	public synchronized void start_robotTimer() {
		// cancelTimeOut();
		// this.timeout = Global.timer.newTimeout(this, 30 * 1000,
		// TimeUnit.MILLISECONDS);
		// this.setTimeFlag(Room.TIME_ROBOT);
	}

	/**
	 * 检查其中三家的叫牌
	 */
	public synchronized List<MgsPlayer> check_jiao(MgsPlayer p, int pai) {
		Log.info("check_jiao feng" + p.getRoomId() +"location "+p.getLocId()+" pai:"+ pai);
		Log.info(p.getJiao());
		List<MgsPlayer> plist = new ArrayList<MgsPlayer>();
		for (int i = 1; i <= 4; i++) {
			MgsPlayer tmpp = this.players.get(i);
			if (tmpp == null)
				continue;
			if (tmpp == p)
				continue;
			Log.info(tmpp.getJiao());
			boolean b = tmpp.getJiao().contains((Integer) pai);
			boolean daguo = tmpp.getDapaiList().contains((Integer) pai);
			// 如果此牌的前3张牌中有这张牌，则不可以胡 
			boolean gen = checkGen(this.curPlayer, tmpp, pai);
			if (gen)
				b = false;
			if (daguo)
				b = false;
			if (this.leavedPlayer.contains(tmpp))
				b = false;
			if (b)
				plist.add(tmpp);
		}
		return plist;
	}

	/**
	 * 胡牌，可以跟一手
	 */
	public synchronized boolean checkGen(MgsPlayer p1, MgsPlayer p2, int pai) {
		for (int i = 1; i <= 4; i++) {
			if (this.players.get(i) == null)
				continue;
			if (this.players.get(i) == p1 || this.players.get(i) == p2)
				continue;
			List<Integer> dapaiList = this.players.get(i).getDapaiList();
			if (dapaiList.size() == 0)
				continue;
			if (dapaiList.get(dapaiList.size() - 1) == pai)
				return true;
		}
		return false;
	}

	/**
	 * 设置庄家 \
	 */
	public synchronized void initZhuang() {
		if (lianzhuang > 0) {
			Log.log("连庄---->initZhuang:   zhuangLoc  " + zhuangLoc
					+ " zhuangId  " + zhuangId);
			Log.log("zhuangLoc:" + zhuangLoc);
			Log.log(this.zhuang);
			return;
		}

		int id = zhuangId;
		if (this.zhuang == null) {
			this.zhuang = this.players.get(id);
		} else {
			zhuangLoc++;
			if (zhuangLoc >= this.fengkeArray.size())
				zhuangLoc -= this.fengkeArray.size();
			this.zhuang = this.players.get(this.fengkeArray.get(zhuangLoc));
			this.zhuangId = this.fengkeArray.get(zhuangLoc);
		}
		Log.log("---->initZhuang: zhuangLoc  " + zhuangLoc + " zhuangId  "
				+ zhuangId);
		Log.log("zhuangLoc:" + zhuangLoc);
	}

	public int zhuangLoc = 0;

	public synchronized void initZhuangId() {
		int loc = 0;
		zhuangLoc = loc;
		this.zhuangId = this.fengkeArray.get(loc);
		fengkeStartId = loc;
		changfeng = loc;
		// System.out.println("--->zhuangId:" + this.zhuangId);
	}

	/**
	 * @param p
	 *            门风 == 自风
	 * @return
	 */
	public synchronized int getmenfeng(MgsPlayer p) {
		int menfeng = 0;
		int ownId = p.getRoomId();
		int id = this.changfeng;
		if (ownId < id)
			ownId += this.MAX_COUNT;
		menfeng = Math.abs(ownId - id) + 1;
		if (this.playerLimit == 2) {
			menfeng++;
		}
		return menfeng;
	}

	/**
	 * 设置当前出牌玩家
	 */
	public synchronized void changeCurPlayer() {
		Log.log("--->changeCurPlayer");
		if (this.curPlayer == null)
			return;
		
		Map<Integer,Integer> skills =  curPlayer.getSkillMap();
		skills.put(PlayerSkill.ID_FEIYANHUANCHAO, 0); //技能关闭
		
		this.curPlayer.setGangshanghua(false);
		int locid = this.curPlayer.getLocId();
		int nextid = this.fengkeArray.indexOf((Integer) locid) + 1;

		if (nextid >= this.fengkeArray.size())
			nextid -= this.fengkeArray.size();

		int curLoc = this.fengkeArray.get(nextid);
		this.curPlayer = this.players.get(curLoc);
		// 1 获得手牌
		CCMD11006 ccmd = new CCMD11006();
		ChannelBuffer cachebuf = ccmd.auto_deal(curPlayer);
		if (cachebuf == null)
		{
			Log.info("changeCurPlayer: cachebuf == null return");
			return;
		}
		// 检查玩家是否托管
		tuoguan(this.curPlayer, cachebuf);
	}

	/**
	 * 对打出的牌进行吃 碰 杠 操作后改变当前出牌玩家
	 */
	public synchronized void changeCurPlayer(MgsPlayer p) {
		if (this.curPlayer == null)
			return;
		Log.log("this.curPlayer.setGangshanghua(false)");
		this.curPlayer.setGangshanghua(false);
		this.curPlayer = p;
		this.start_chupaiTimer();
	}

	/**
	 * 玩家托管逻辑
	 */
	public synchronized void tuoguan(MgsPlayer p, ChannelBuffer buf) {
		SCMD3 scmd3 = new SCMD3();
		if (buf != null)
			scmd3.list.add(buf);

		if (this.tuoguanPlayer.contains(p) || this.leavedPlayer.contains(p)) {
			if (this.leavedPlayer.size() == this.MAX_COUNT) {
				Log.log("this.leavedPlayer.size() == this.MAX_COUNT");
				this.destoryRoom();
				return;
			}
			System.out.println("tuoguan  自动打牌");
			this.SendRoomBroadcast(3, scmd3.getBytes());
			// 2 打出手牌
			CCMD11004 ccmd = new CCMD11004();
			ccmd.setPlayer(p);
			ccmd.auto_deal(p.getTouchedPai());
			return;
		} else {
			this.SendRoomBroadcast(3, scmd3.getBytes());
			this.start_chupaiTimer();
		}
	}

	/**
	 * 初始化牌堆 // 1 - 9 条子 //11 - 19 筒子 //21 - 29 万字//31 -37 字牌 // 34种
	 */
	public synchronized void initPaidui() {
		if (this.playerLimit == 2) {
			init1v1PaiDui();
			return;
		}
		this.fixOrder = new int[] { -1, -1, -1, -1 };
		this.IDList = new ArrayList<Integer>();
		leftPai = 136;
		this.map = new HashMap<Integer, Integer>();
		for (int i = 1; i <= 9; i++) {
			map.put(i, 4);
			IDList.add(i);
			map.put(i + 10, 4);
			IDList.add(i + 10);
			map.put(i + 20, 4);
			IDList.add(i + 20);
			if (i <= 7) {
				IDList.add(i + 30);
				map.put(i + 30, 4);
			}
		}
	}

	public synchronized void init1v1PaiDui() {
		this.fixOrder = new int[] { -1, -1, -1, -1 };
		this.IDList = new ArrayList<Integer>();
		leftPai = 64;
		this.map = new HashMap<Integer, Integer>();
		for (int i = 1; i <= 9; i++) {
			IDList.add(i);
			map.put(i, 4);
			if (i <= 7) {
				IDList.add(i + 30);
				map.put(i + 30, 4);
			}
		}
		Log.log("init1v1PaiDui");
	}

	/**
	 * 初始牌组 //生成1个玩家的手牌
	 */
	public synchronized List<Integer> getInitPaizu(int initNum, int yun) {
		List<Integer> shoupai = new ArrayList<Integer>();
		for (int i = 0; i < initNum; i++) {
			int id = 0;
			if (shoupai.size() < 7) {
				int rand = (int) (Math.random() * IDList.size());
				id = this.IDList.get(rand);
			} else { // 根据运计算手牌
				int randYun = (int) (Math.random() * 10);
				if (randYun <= yun) {
					id = getYunPai(shoupai);
				}// 指定牌
				else {
					id = this.IDList.get((int) (Math.random() * IDList.size()));
				} // 随机牌
				if (id == 0)
					id = this.IDList.get((int) (Math.random() * IDList.size()));
			}
			int value = this.map.get(id);
			value--;
			this.map.put(id, value);
			shoupai.add(id);
			if (value <= 0)
				this.IDList.remove((Integer) id);
		}
		this.leftPai -= initNum;
		return shoupai;
	}

	private synchronized int getYunPai(List<Integer> shoupai) {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		List<Integer> maxList = null;
		for (int i = 0; i < shoupai.size(); i++) {
			int pai = shoupai.get(i);
			int key = (int) (pai / 10) + 1;
			if (map.get(key) != null)
				map.get(key).add(pai);
			if (map.get(key) == null)
				map.put(key, MyArrays.asList(pai));
			if (maxList == null)
				maxList = map.get(key);
			if (maxList.size() < map.get(key).size())
				maxList = map.get(key);
		}
		List<MajiangPai> tmp = SortMaJiang.getInstance().sortByNum(maxList); // 生成优先级排序数组
		for (MajiangPai pai : tmp) // 排序 1 . 找到多的牌 2. 给多的牌
		{
			if (IDList.contains(pai))
				return pai.getPaiId(); // 检查这张牌是否有
			continue;
		}
		int type = (int) shoupai.get(0) / 10;
		for (int i = 1; i <= 9; i++) // 随机一张同花色的牌
		{
			int tmpId = i + type * 10;
			if (IDList.contains(tmpId))
				return tmpId;
		}
		return 0;
	}

	/**
	 * 玩家托管
	 */
	public synchronized void addTuoGuanQueue(MgsPlayer p) {
		if (!this.leavedPlayer.contains(p)) {
			this.leavedPlayer.add(p);
			MyArray arr = new MyArray();
			arr.push(p.getLocId());
			arr.push(2);
			MyByteArray buf1 = new MyByteArray();
			buf1.write(arr);
			SendRoomBroadcast(11056, buf1.getBuf());
		}
	}
	public  synchronized boolean isInTuoguan(MgsPlayer p)
	{
		if (this.leavedPlayer.contains(p))
			return true;
		return false;
	}

	/**
	 * 随机获得下一张牌
	 */
	public synchronized int getNextRandPai() {
		// 根据运或者某些规则生成下一张牌
		// 从固定的排序中获得某一张牌
		int id = this.IDList.get((int) (Math.random() * IDList.size()));
		int value = this.map.get(id);
		if (value < 1) {
			Log.log("paidui error value < 1");
		}
		value--;
		this.map.put(id, value);
		if (value <= 0)
			this.IDList.remove((Integer) id);
		this.leftPai--;
		return id;
	}

	/**
	 * 根据运 获得下一张牌 听牌后， 运减半
	 */
	public synchronized int getNextPaiByYun(MgsPlayer p) {
		// 检查技能
		int paiId = p.getSkillutil().checkMopaiSkill(p);
		if (paiId != -1) {
			synPaidui(paiId);
			return paiId;
		}
		int id = -1;
		int yun = p.getYun() + p.getTmpYun();
		if(yun > 5)yun = 5;
		else if(yun < -5) yun = -5;
		logger.info("getNextPaiByYun "+ p.getName() + "  yun:" +p.getYun()  +  "tmpyun:" +  p.getTmpYun());
		p.getBusiness().saveUserOperate("getNextPaiByYun "+ p.getName() + "  yun:" +p.getYun()  +  "tmpyun:" +  p.getTmpYun());
		
		if (p.isTing() && yun > 0)
			if(yun > 3)yun = 3;
		
		int addRate = 0;
		Map<Integer, Integer> map = p.getSkillMap();
		if(!p.isTing() && map.containsKey(3))
		{
			int flag = map.get(3);
			if(flag == 1)addRate = 10;
		}
		
		List<Integer> wishs = p.getWishNextPais();
		List<Integer> skillWishs = p.getSkillWishPais();
		List<Integer> jiaopai = p.getJiao();
		
		if (yun < 0) {
			Log.info("摸牌  getPaiOutWishes");
			List<Integer> _all1 = new ArrayList<Integer>();
			_all1.addAll(wishs);
			_all1.addAll(skillWishs);
			_all1.addAll(jiaopai);
			int 	yunpai = 	getPaiOutWishes(_all1, yun);
			synPaidui(yunpai);
			return yunpai;
		}
		int rand = (int) (Math.random() * 100);
		Log.info("--->摸牌  运 :  rand  " + rand + " 运 :" + (yun * 100 / 5));
		if (p.isTing()) {
			int rate = yun * 100 / 5 + addRate;
			if (rand < rate && jiaopai.size() > 0) {
				id = this.getWishesId(jiaopai);
				Log.info("--->听 运好牌:" + id);
				Log.info("jiao:" +jiaopai.toString());
			}
		} else {
			int rate = yun * 100 / 5 + addRate;
			int r1 = (int) (Math.random() * 100);
			if (r1 < rate) {
				List<Integer> _all = new ArrayList<Integer>();
				_all.addAll(skillWishs);
				_all.addAll(wishs);
				_all.addAll(jiaopai);
				// 先技能， 再好牌
				id = getWishesId(_all);
				Log.info("---->没有听 运好牌:" + id);
				Log.info("skillWishs:" +skillWishs.toString());
				Log.info("wishs:" +wishs.toString());
				Log.info("jiao:" +jiaopai.toString());
			}
		}
		if (id == -1 || id == 0)
			id = this.IDList.get((int) (Math.random() * IDList.size()));
		synPaidui(id);
		//检查普通牌是否是技能好牌
		p.getSkillutil().checkIsHuangquan(p, id);
		return id;
	}

	public synchronized void synPaidui(int id) {
		int value = this.map.get(id);
		if (value < 1) {
			Log.log("paidui error value < 1");
		}
		value--;
		this.map.put(id, value);
		if (value <= 0)
			this.IDList.remove((Integer) id);
		this.leftPai--;

		if (id > 37 || id < 1 || id % 10 == 0) {
			System.out.println("error");
		}
		
		Log.error("left pai:"+IDList.toString());
		
		for(int i = 0;i < IDList.size();i++)
		{
			System.out.print(IDList.get(i) +":" + map.get(IDList.get(i)) +",");
		}
		
	}

	/**
	 * 运为负数，得不到希望的牌
	 */
	public synchronized int getPaiOutWishes(List<Integer> list, int yun) {
		
		logger.info("getPaiOutWishes rand time  wishpai:" + list.toString());
		int conut = 0;
		int pai = 0;
		yun = -yun + 1;
		while (conut < yun) {
			conut++;
			logger.info("getPaiOutWishes rand time:" + conut);
			int r = (int) (Math.random() * this.IDList.size());
			pai = this.IDList.get(r);
			logger.info("pai:" + pai);	
			//负数判断
			if(list.contains(-1))
				if(checkIsWishPai(1,pai)) continue;;
			if(list.contains(-2))
				if(checkIsWishPai(2,pai)) continue;;
			if(list.contains(-3))
				if(checkIsWishPai(3,pai)) continue;;
			if(list.contains(-4))
				if(checkIsWishPai(4,pai)) continue;;
			
			if (!list.contains(pai))
			{
				logger.info("getPaiOutWishes rand time pai:" + pai);				
				return pai;
			}
		}
		logger.info("getPaiOutWishes rand time pai other:" + pai);		
		return pai;
	}
	
	public boolean checkIsWishPai(int type, int paiId)
	{
		int tmp = (type -1) * 10;
		if(paiId >tmp && paiId < (tmp + 10))
			return true;
		return false;
	}

	/**
	 * 获得希望得到的牌组中的牌
	 */
	public synchronized int getWishesId(List<Integer> list) {
		while (list.size() > 0) {
			int randId = (int) (Math.random() * list.size());
			int tmpId = list.get(randId);
			list.remove((Integer) tmpId);
			int id2 = 0;
			switch (tmpId) // 负数，指代某一类型的牌
			{
			case -1:
				id2 = getWishPaiByType(0);
				if (id2 != 0)
					return id2; // 1 - 9
			case -2:
				id2 = getWishPaiByType(1);
				if (id2 != 0)
					return id2; // 11 - 19
			case -3:
				id2 = getWishPaiByType(2);
				if (id2 != 0)
					return id2; // 21 - 29
			case -4:
				id2 = getWishPaiByType(3);
				if (id2 != 0)
					return id2; // 31 ~ 37
			}
			if (this.IDList.contains(tmpId))
				return tmpId;
		}
		// int id = this.IDList.get((int)(Math.random()*IDList.size()));
		int id = -1;
		return id;
	}

	private synchronized int getWishPaiByType(int type) {
		List<Integer> list = MyArrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		while (list.size() > 0) {
			int r = (int) (Math.random() * list.size());
			int id = list.get(r) + type * 10;
			list.remove(r);
			if (this.IDList.contains(id))
				return id;
		}
		return 0;
	}

	/**
	 * 打牌后，检查是否有玩家需要碰吃胡杠
	 */
	public synchronized List<MgsPlayer> check_whoNeedDeal(int pai) {
		List<MgsPlayer> hu_p = this.check_jiao(this.curPlayer, pai);
		List<MgsPlayer> hu_deal = check_gang_player(pai);
		for (int i = 0; i < hu_deal.size(); i++) {
			MgsPlayer tmp = hu_deal.get(i);
			if (!hu_p.contains(tmp))
				hu_p.add(tmp);
		}
		return hu_p;
	}

	/**
	 * 检查碰 杠 吃的玩家
	 */
	public synchronized List<MgsPlayer> check_gang_player(int pai) {
		PatternPai pattern = new PatternPai();

		List<MgsPlayer> ps = new ArrayList<MgsPlayer>();
		for (int i = 1; i <= 4; i++) {
			MgsPlayer tmp_p = this.players.get(i);
			if (tmp_p == null)
				continue;
			if (this.leavedPlayer.contains(tmp_p)
					|| this.tuoguanPlayer.contains(tmp_p))
				continue;
			if (ps.contains(tmp_p))
				continue;
			if (tmp_p == this.curPlayer)
				continue;
			if (tmp_p.isTing())
				continue;
			boolean canChi = pattern.checkTOBEshun(pai, tmp_p.getShoupai());
			if (canChi && !ps.contains(tmp_p)) {
				ps.add(tmp_p);
				continue;
			}
			boolean canPeng = pattern.checkPeng(tmp_p, pai);
			if (canPeng && !ps.contains(tmp_p)) {
				ps.add(tmp_p);
				continue;
			}
			boolean canGang = pattern.checkGang(tmp_p, pai, PatternPai.DA_PAI);
			if (canGang && !ps.contains(tmp_p)) {
				ps.add(tmp_p);
				continue;
			}
		}
		return ps;
	}

	/**
	 * 流局
	 */
	public synchronized void liuju() {
		// 发送流局指令
		firtPan = false;
		for(int i = 1; i <= 4 ; i++)
		{
			MgsPlayer pp = this.players.get(i);
			if(pp == null)continue;
			pp.addRoleExp(1);
			MJ_User user = pp.getBusiness().getPlayer();
			CCMD11101 ccmd01 = new CCMD11101();
			if(user.getLevel() >= MJ_User.MAXLEVEL)continue;
			user.setScore(user.getScore() + 1);
			ccmd01.mgs_modPlayerScore(pp, 1);

			
					
			if(user.getScore() > user.getLevel() * MJ_User.SCORE_EVERYLEVEL * user.getLevel()  && user.getLevel() < MJ_User.MAXLEVEL)
			{
					int level = (int) Math.sqrt(user.getScore()/MJ_User.SCORE_EVERYLEVEL) + 1;
					user.setLevel(level);
					ccmd01.msg_modPlayerLevel(pp, 1);
					Log.log(user.getName() +" level up" +  user.getLevel());
			}
			pp.getBusiness().savePlayer(user);
		}
		lianzhuang++;
		System.out.println("---------->流局");
		this.ended();
		this.cancelTimeOut();
		checkNextGame();
		this.SendRoomBroadcast(11018, null);

	}

	/**
	 * 房主退出后 ， 按照ID顺序转移新的房主
	 * 
	 * @param id
	 * @return 新的房主player
	 */
	public synchronized MgsPlayer changeManager() {
		int tmpId = 0;
		MgsPlayer p = null;
		while (tmpId <= 4) {
			p = this.players.get(tmpId);
			if (p != null) {
				if(this.leavedPlayer.contains(p))
				{
					tmpId++;					
					continue;
				}
				this.manager = p;
				new CCMD11055().auto_deal(p);
				return p;
			}
			tmpId++;
		}
		return null;
	}

	// public synchronized boolean setManagerById(int id)
	// {
	// MgsPlayer p = this.players.get(id);
	// if(p != null)
	// {
	// this.manager = p;
	// return true;
	// }
	// return false;
	// }
	public synchronized void removePlayer(MgsPlayer player) {
		this.getChannelGroup().remove(player.getChannel());
		// this.players.remove(player.getRoomId());
	}

	public synchronized void leaveRoom(MgsPlayer p) {

		//p.stopHeartBeat();
		if (this.room_state == MgsRoom.STARTED
				|| this.room_state == MgsRoom.DESTORYED)
			return;
		int roomid = p.getLocId();
		if (this.players.containsKey(roomid))
			this.players.remove(roomid);
		if (this.leavedPlayer.contains(p))
			this.leavedPlayer.remove(p);
		if (this.tuoguanPlayer.contains(p))
			this.tuoguanPlayer.remove(p);
		p.setRoomId(-1);
		p.setRoom(null);
		removePlayer(p);
		if (players != null && this.players.size() == 0) {
			Global.rManager.destroyRoom(this);
			return;
		}
		if(players != null &&(this.leavedPlayer.size() == this.MAX_COUNT))
		{
			Global.rManager.destroyRoom(this);
			return;
		}
		if (this.manager == p)
			this.changeManager();
		if (this.playerLimit == 2 && this.room_state != MgsRoom.CREATED) {
			roomOver_msg();
		}
		if (this.playerLimit == 2) {
			Global.rManager.removeRoomFromGamingList(this);
		}

	}

	public synchronized void roomOver_msg()
	{
		this.SendRoomBroadcast(11024, null);
	}
	
	public synchronized void offLine(MgsPlayer player) {
		// 11056 离开房间指令
		if(players == null)return;
		if(player.offline)return;
		player.offline = true;
		player.zhunbei = false;
	//	player.stopHeartBeat();
		MyArray arr = new MyArray();
		arr.push(player.getLocId());
		arr.push(1);
		MyByteArray buf1 = new MyByteArray();
		buf1.write(arr);
		SendRoomBroadcast(11056, buf1.getBuf());
		Log.info(player.getName()+"  掉线消息发送");
		// ************************************
		removePlayer(player);
		if (this.room_state == MgsRoom.STARTED
				|| (this.playerLimit == 4 && this.room_state !=MgsRoom.CREATED)) // 已经开始
																			// 4人场
																			// 并且不是刚创建的房间
		{
			// 加入自动发牌队列
			this.leavedPlayer.add(player);
			System.out.println("加入leavedPlayer:" + player.getName());
			if (this.leavedPlayer.size() == this.MAX_COUNT) {
				Log.info("销毁房间    this.leavedPlayer.size():" +this.leavedPlayer.size());
				this.destoryRoom();
				return;
			}
		} else {
			int roomid = player.getLocId();
			this.players.remove(roomid);
			if (this.players.containsKey(roomid))
				this.players.remove(roomid);
			if (this.leavedPlayer.contains(player))
				this.leavedPlayer.remove(player);
			if (this.tuoguanPlayer.contains(player))
				this.tuoguanPlayer.remove(player);
			player.setRoomId(-1);
			player.setRoom(null);
			this.removePlayer(player);
		}
		if ((players != null && this.players.size() == 0)
				|| this.leavedPlayer.size() == this.MAX_COUNT) {
			Log.info("2 销毁房间: this.players.size: "+ this.players.size() +" :  this.leavedPlayer.size:"+  this.leavedPlayer.size());
			this.destoryRoom();
			return;
		}
		if(this.playerLimit == 2 && this.room_state != MgsRoom.CREATED)
			this.roomOver_msg();
		if (this.playerLimit == 2 && this.room_state != STARTED) {   //一盘结束时候再判断
			
			Global.rManager.removeRoomFromGamingList(this);
		}
		if(this.playerLimit == 4 && this.room_state != CREATED && this.leavedPlayer.size()>=2)
		{
			this.roomOver_msg();
			Global.rManager.removeRoomFromGamingList(this);
		}
		if (players != null && this.manager == player)
			this.changeManager();
	}

	/**
	 * 断线后重新回到房间
	 * 
	 * @param p
	 */
	public synchronized void reEnterRoom(MgsPlayer p) {
		if (this.leavedPlayer == null)
			return;
		for (int i = 0; i < this.leavedPlayer.size(); i++) {
			MgsPlayer tmp = this.leavedPlayer.get(i);
			if (tmp.getBusiness().getPlayer().getUid() == p.getBusiness()
					.getPlayer().getUid()) {
				this.leavedPlayer.remove(tmp);
				break;
			}
		}
		this.players.put(p.getRoomId(), p);
	}

	/**
	 * 发送战斗结果
	 */
	public synchronized void sendFightResultMsg() {
		System.out.println("发送战斗结果!");
		for (int i = 1; i <= this.MAX_COUNT; i++) {
			MgsPlayer p = this.players.get(i);
			if (p.getFan() == null)
				continue;
		}

	}

	/**
	 * 有一家的分数为0 ， 比赛结束
	 */
	private boolean fufen = false;
	private MgsPlayer haveNoGoldP;
	/**
	 * 胡牌的玩家数
	 */
	private int huPaiCount = 0;
	private List<MgsPlayer> hu_players;

	/**
	 * 战斗结束，保存胡牌结果，用于算番
	 */
	public synchronized void hupai(MgsPlayer p, Algorithm_Hu hu) {
		firtPan = false;
		Map<Integer, GroupShoupai> map = hu.shoupai;
		if (this.timeout != null)
			this.timeout.cancel();
		if (hu_players == null)
			hu_players = new ArrayList<MgsPlayer>();
		timeout = null;
		this.ended();
		this.cancelTimeOut();
		groupHupai = map;
		caculateFan(p, map, hu.isAnqidui);
		huPaiCount--;
		caculateScore(p); // 计算积分
		hu_players.add(p);
		Zhanji(p);
		if(hu.isZimo)this.checkTask.checkTaskAtterHuPai(p, CheckTask.TASK_ZIMO);
		this.checkTask.checkTaskAtterHuPai(p, CheckTask.TASK_WIN);
		
		logger.info("");
		
		if (huPaiCount > 0)
			return; // 还有胡牌玩家为做返回

		int i = 0;
		for(i = 1 ; i <= 4;i++)
		{
			MgsPlayer tmpp = this.players.get(i);
			if(tmpp == null)continue;
			MJ_User tmppUser = tmpp.getBusiness().getPlayer();
			tmppUser.setPanTotal(tmppUser.getPanTotal() + 1);
			if(!tmppUser.isJoinedNetBattle())
				tmppUser.setJoinedNetBattle(true);
			tmpp.addRoleExp(1);
			
			this.checkTask.checkTaskAtterHuPai(tmpp, CheckTask.TASK_PANPLAY);
			
			tmpp.getBusiness().savePlayer(tmppUser);
		}
		// 广播结果
		if (hu_players.contains(this.zhuang)) {
			lianzhuang++;
		} else {
			lianzhuang = 0;
		}
		this.ended();
		SCMD11009 scmd = new SCMD11009();
		scmd.setHu_players(hu_players);
		SendRoomBroadcast(11009, scmd.getBytes());
		checkEndSkill(p);
		checkNextGame();
	}
	
	public void Zhanji(MgsPlayer p)
	{
		MJ_User user = p.getBusiness().getPlayer();
		int fan = p.getFan().getTotalFan();
		if(fan >= 10)
		{
			this.checkTask.checkTaskAtterHuPai(p, CheckTask.TASK_MANGUAN);
		}
		
		user.setPanHu(user.getPanHu() + 1);
		if(fan  >= 10 && fan < 20)
			user.setPanHu10(user.getPanHu10() + 1);
		else if(fan  >= 20 && fan < 30)
			user.setPanHu10(user.getPanHu20() + 1);
		else if(fan >= 30)
			user.setPanHu10(user.getPanHu30() + 1);
	}
	
	public void checkEndSkill(MgsPlayer p) {
		Map<Integer, MgsPlayer> map = p.getHuMap();
		MgsPlayer tmp = null;
		if (map.get(2) == null)
			return;
		// 点炮
		tmp = map.get(2);
		int beforeqi = tmp.getQi();
		p.getSkillutil().checkSkill_end(tmp);
		CCMD11031 ccmd031 = new CCMD11031();
		ccmd031.auto_deal(tmp, tmp.getQi() -beforeqi);
	}

	public void saveChangRecord()
	{
		if(cdao == null)cdao = new CountDao();
		Sts_Changci changci = cdao.findTodayChangci();
		if(changci == null)
		{
			changci = new Sts_Changci();
			changci.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
			changci.setDay(ServerTimer.getDay());
		}
		if(this.playerLimit == 2)
		{
			changci.setChang2(changci.getChang2() + 1);
		}else if(this.playerLimit == 4)
		{
			changci.setChang4(changci.getChang4() + 1);
		}
		cdao.saveSts_Object(changci);
	}
	public synchronized void checkNextGame() {
		
		//打完一把， 记录一盘
		saveChangRecord();
		enteredGame = false;
		Log.log("checkNextGame");
		if (this.playerLimit == 2) {
				if(this.tuoguanPlayer.size() >0 || this.leavedPlayer.size() >0)
				{
					Global.rManager.removeRoomFromGamingList(this);
				}
			return;
		}
		if (this.fufen
				|| (this.cur_bashu == 4 && this.cur_quanshu == this.quanLimit)) {

			tongji();
			this.destoryRoom();
			return;
		} // 游戏结束，玩家结算并退出房间
		if (this.leavedPlayer.size() >= this.players.size() - 1) {
			tongji();
			this.destoryRoom();
			return;
		}// 玩家已经全部离开房间
		if (this.cur_bashu < 4) {
			// sendFightResultMsg();
//			this.start_readyTimer();
		}// 其他玩家进入准备
		if (this.cur_bashu == 4 && this.cur_quanshu < this.quanLimit) {
			cur_bashu = 0;
			cur_quanshu++;
			// sendFightResultMsg();
			// 开始计时，如果//其他玩家进入准备 2人模式不开启timer
//			if (this.playerLimit != 2)
//				this.start_readyTimer();
		}
	}

	/**
	 * 计算当前局积分
	 */
	public synchronized void caculateScore(MgsPlayer p) {
		
		
		int winFan = p.getFan().getTotalFan();
		MJ_User user = p.getBusiness().getPlayer();
		boolean mainpoff = false;
		if(p.offline)
		{
			//判断是否在线
			MgsPlayer tmp = MgsCache.getInstance().userInfos.get(user.getName());
			if(tmp != null)
			{
				user = tmp.getBusiness().getPlayer();
				mainpoff = true;
			}
			
		}
		CCMD11101 ccmd01 = new CCMD11101();
		int i = 0;
		if (this.playerLimit == 2) {
			// 直接扣除金币
			int winGold = this.beishu * winFan;
			CCMD11101 ccmd = new CCMD11101();
			for (i = 1; i <= 4; i++) {
				MgsPlayer tmp = this.players.get(i);
				if(tmp == null) continue;
				boolean offJieSuan = false;
				if(tmp.offline)
				{
					String name = tmp.getBusiness().getPlayer().getName();
					MgsPlayer tmp1 = MgsCache.getInstance().userInfos.get(name);
					if(tmp1 != null)
					{
						tmp = tmp1;
						offJieSuan = true;
					}
				}
				if ( tmp == p)
				{
					if(user.getLevel() >= MJ_User.MAXLEVEL)
					continue;
					user.setScore(user.getScore() + 5);
					ccmd01.mgs_modPlayerScore(p, 5);
					if(user.getScore() >= user.getLevel() * MJ_User.SCORE_EVERYLEVEL * user.getLevel() && user.getLevel() < MJ_User.MAXLEVEL)
					{
						int level = (int) Math.sqrt(user.getScore()/MJ_User.SCORE_EVERYLEVEL) + 1;
//						Log.info("分数: "+user.getScore() + " level: " + level + "   : name:" + user.getName());
						user.setLevel(level);
						ccmd01.msg_modPlayerLevel(tmp, 1);
						Log.log(user.getName() +" level up" +  user.getLevel());
					}
//					Log.info("  5  level :" + user.getLevel() + "       score: " + user.getScore()  + "   " + user.getName());
					
					continue;
				}
				MJ_User tmpuser = tmp.getBusiness().getPlayer();
				
				int beforeGold = tmpuser.getGold();
				int beforeDia = tmpuser.getDianQuan();
				int modGold = -winGold;
				
				if (tmpuser.getGold() < winGold) {
					winGold = tmpuser.getGold();
					modGold = -winGold;
					tmpuser.setGold(0);
					ccmd.auto_deal(tmp, 2);
					haveNoGoldP = tmp;
				} else {
					tmpuser.setGold(tmpuser.getGold() - winGold);
					ccmd.auto_deal(tmp, 2);
				}
				if(tmpuser.getLevel() < MJ_User.MAXLEVEL)
				{
					tmpuser.setScore(tmpuser.getScore() + 1);
					ccmd01.mgs_modPlayerScore(tmp, 1);
				}
				if(tmpuser.getScore() >= tmpuser.getLevel() * MJ_User.SCORE_EVERYLEVEL * tmpuser.getLevel() && tmpuser.getLevel() < MJ_User.MAXLEVEL)
				{
					int level = (int) Math.sqrt(tmpuser.getScore()/MJ_User.SCORE_EVERYLEVEL) + 1;
//					Log.info("分数: "+tmpuser.getScore() + " level: " + level + "   : name:" + tmpuser.getName());
					tmpuser.setLevel(level);
					ccmd01.msg_modPlayerLevel(tmp, 1);
					Log.log(tmpuser.getName() +" level up" + tmpuser.getLevel());
				}
//				Log.info(" 1 level :" + tmpuser.getLevel() + "       score: " + tmpuser.getScore() + "   " + tmpuser.getName());
				tmp.getBusiness().savePlayer(tmpuser);
				tmp.saveUserChargeRec("对战结算",beforeGold , modGold, tmpuser.getGold(), beforeDia, 0, tmpuser.getDianQuan(), "1V1对战结算:失败");
				
				
				if(offJieSuan)
				{
					CCMD11111 cmd111 = new CCMD11111();
					cmd111.auto_deal(tmp, "上一局游戏结束,积分: +1,金币: -" + winGold);
				}
			}
			
			int beforeGold = user.getGold();
			int beforeDia = user.getDianQuan();
			int modGold = (int) (winGold *0.99);
			
			user.setGold(user.getGold() + modGold);
			ccmd.auto_deal(p, 2);
			p.getBusiness().savePlayer(user);
			p.saveUserChargeRec("对战结算",beforeGold , modGold, user.getGold(), beforeDia, 0, user.getDianQuan(), "1V1对战结算:胜利");
			// 金币修改指令 ， 没钱了， 离开房间指令
			
			if(p.offline)
			{
				//判断是否在线
				MgsPlayer tmp = MgsCache.getInstance().userInfos.get(user.getName());
				if(tmp != null)
				{
					CCMD11111 cmd111 = new CCMD11111();
					cmd111.auto_deal(tmp, "上一局游戏结束,积分: +5,金币: +" + (winGold*0.99));
				}
				
			}
			
		} else if (this.playerLimit == 4) {// 4 人场，进行积分结算
											//
			Map<Integer, MgsPlayer> map = p.getHuMap();

			int score = 0;
			score = p.getFan().getTotalFan() * BASE_SCORE;
			p.setScore(score);
			p.setTotalScore(p.getTotalScore() + score);
			if (map.get(2) != null)// 点炮
			{
				map.get(2).setScore(-score);
				map.get(2).setTotalScore(map.get(2).getTotalScore() - score);
				if (map.get(2).getTotalScore() <= 0)
					fufen = true;
			} else // 自摸
			{
				for (i = 1; i <= 4; i++) {
					MgsPlayer tmpp = this.players.get(i);
					if (tmpp == null)
						continue;
					if (tmpp == p)
						continue;
					tmpp.setScore(-score / 3);
					tmpp.setTotalScore(tmpp.getTotalScore() - score / 3);
					if (tmpp.getTotalScore() <= 0)
						fufen = true;
				}
			}
		}
	}

	/**
	 * 统计总的结果
	 */
	public synchronized void tongji() {
		// 根据积分排序 ，分配chips;
		// 改变每个人的金币, diao
		SCMD11025 scmd = new SCMD11025();
		List<MgsPlayer> list = new ArrayList<MgsPlayer>();
		int i = 0;
		for (i = 1; i <= 4; i++) {
			if (this.players.get(i) == null)
				continue;
			list.add(this.players.get(i));
		}
		Collections.sort(list);
		for (i = 1; i <= 4; i++) {
			MgsPlayer tmp = this.players.get(i);
			if (tmp == null)
				continue;
			MJ_User user = tmp.getBusiness().getPlayer();

			int totalScore = 10000;
			user.setGold(user.getGold() + this.totalChips / totalScore
					* tmp.getTotalScore());
			CCMD11101 cmd101 = new CCMD11101();
			cmd101.auto_deal(tmp, 2);
			scmd.rank = i;
			scmd.score = tmp.getTotalScore();
			scmd.money = this.totalChips / totalScore * tmp.getTotalScore();
			scmd.auto_deal(tmp);
		}
	}

	public synchronized void caculateFan(MgsPlayer p,
			Map<Integer, GroupShoupai> map, boolean isAnqidui) {
		List<MaJiang_Fan> fans = new ArrayList<MaJiang_Fan>();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			GroupShoupai shoupai = map.get(key);
			shoupai.p = p;
			shoupai.patternShoupai();
			int menfeng = p.getRoomId();
			changfeng = this.zhuang.getRoomId();
			boolean lastOne = leftPai == 0 ? true : false;

			logger.info(p.getName()+" hupai:" +shoupai+ "peng:" + p.getPeng().toString() + "gang:"
					+ p.getGang().toString() + "  chi:" + p.getChi().toString() + "  shoupai:" + p.getShoupai().toString());
			if (this.playerLimit == 2)
				changfeng = -1;
			logger.info(p.getName()+"  menfeng:"+p.getRoomId() +" changfeng:" + changfeng) ;

			MaJiang_Fan fan = new MaJiang_Fan();
			int lianzhuangNum = 0;
			if(this.zhuang == p)lianzhuangNum = this.lianzhuang;
			fan.init(p, shoupai.getAnke(), shoupai.getShun(),
					shoupai.getJiang(), shoupai.getTing(), isAnqidui,
					p.isGangshanghua(), lastOne, p.isQianggang(), p.isTianhe(),
					p.isDihe(), p.isTing(), p.isTianting(), changfeng, menfeng,shoupai.hupaiId,lianzhuangNum);
			fan.caculateFan();
			fans.add(fan);
		}
		Collections.sort(fans, new SortByFan());
		MaJiang_Fan result = fans.get(0);
		p.setFan(result);
		logger.info(p.getName()+" fan:" +result);
	}

	/**
	 * 清除当前time计时
	 */
	public synchronized void cancelTimeOut() {
		if (this.timeout == null)
			return;
		this.timeout.cancel();
		this.timeout = null;
	}

	/**
	 * 广播房间消息
	 */
	public synchronized void SendRoomBroadcast(int cmd, ChannelBuffer buf) {
		if (this.players == null)
			return;
		for (int i = 1; i <= 4; i++) {
			MgsPlayer p = this.players.get(i);
			if (p == null)
				continue;
			ChannelBuffer tmpbuf = null;
			if (buf != null)
				tmpbuf = buf.copy();
			;
			p.send(cmd, tmpbuf);
		}
	}

	/**
	 * 给房间内其他玩家广播消息
	 */
	public synchronized void SendOtherBroadcast(int cmd, ChannelBuffer buf,
			MgsPlayer p1) {
		for (int i = 1; i <= 4; i++) {
			MgsPlayer p = this.players.get(i);
			if (p == null)
				continue;
			if (p1 == p)
				continue;
			ChannelBuffer tmpbuf = buf.copy();
			p.send(cmd, tmpbuf);
		}
	}

	// 吃 碰 杠 胡 过 超时 6中情况触发 deal 等待结束操作
	private List<MgsPlayer> waitDealPlayer;
	private String[] waitDeal_Operate;
	private CMD[] waitDeal_cmd;

	/**
	 * 过 pass
	 */
	public synchronized void deal_pass(MgsPlayer p) {
		if (this.waitDealPlayer == null || this.waitDealPlayer.size() == 0)
			return;
		if (!this.waitDealPlayer.contains(p))
			return;
		this.waitDealPlayer.remove(p);
		if (waitDealPlayer.size() == 0) {
			try {
				this.run(null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
			}
		}

	}
	/**
	 * waitDealPlayer 处理反馈
	 */
	public synchronized void waitDeal_back(MgsPlayer p, String operate, CMD ccmd) {
		if (this.timeFlag != Room.TIME_DAPAI
				&& this.timeFlag != Room.TIME_DEALPAI)
			return;// 出牌或打牌阶段才处理
		if (this.timeFlag == Room.TIME_DAPAI && this.curPlayer != p)
			return; // 打牌阶段， 非当前玩家的处理为非法

		if (waitDeal_Operate == null)
			waitDeal_Operate = new String[5];
		if (waitDeal_cmd == null)
			waitDeal_cmd = new CMD[5];
		if (p != null) {
			if (this.timeFlag != Room.TIME_DAPAI) {
				if (waitDealPlayer == null || !waitDealPlayer.contains(p))
					return;
				waitDealPlayer.remove(p);
			}
			int id = p.getRoomId();
			waitDeal_Operate[id] = operate;
			waitDeal_cmd[id] = ccmd;
		}
		if (waitDealPlayer == null || waitDealPlayer.size() == 0) {
			// 判断优先级
			// 1 .优先级玩家进行操作
			List<Integer> huList = checkPriority(waitDeal_Operate, "胡");
			if (huList.size() > 0) {
				deal_waitBackOver(huList, "胡");
				return;
			}
			List<Integer> gangList = checkPriority(waitDeal_Operate, "杠");
			if (gangList.size() > 0) {
				deal_waitBackOver(gangList, "杠");
				return;
			}
			List<Integer> pengList = checkPriority(waitDeal_Operate, "碰");
			if (pengList.size() > 0) {
				deal_waitBackOver(pengList, "碰");
				return;
			}
			List<Integer> chiList = checkPriority(waitDeal_Operate, "吃");
			if (chiList.size() > 0) {
				deal_waitBackOver(chiList, "吃");
				return;
			}
			// 没有人进行操作，超时， 改变当前玩家，摸牌
			this.curPlayer.getSkillutil().checkSkillAfterDapai(curPlayer,null,null);
			this.changeCurPlayer();
		}
	}

	private synchronized void deal_waitBackOver(List<Integer> list,
			String operate) {
		MgsPlayer tmpcur = this.curPlayer;
		Log.log("--->deal_waitBackOver");
		this.cancelTimeOut();
		if (operate.equals("胡"))
			huPaiCount = list.size();
		for (Integer tmp : list) {
			waitDeal_cmd[tmp].deal_back();
			MgsPlayer p = waitDeal_cmd[tmp].getPlayer();
			if (operate.equals("胡")) {
				p.increaseQi(10);
				CCMD11031 ccmd031 = new CCMD11031();
				ccmd031.auto_deal(p, 10);
			} else if (operate.equals("吃")) {
				p.increaseQi(2);
				CCMD11031 ccmd031 = new CCMD11031();
				ccmd031.auto_deal(p, 2);
			} else if (operate.equals("碰")) {
				p.increaseQi(2);
				CCMD11031 ccmd031 = new CCMD11031();
				ccmd031.auto_deal(p, 2);
			} else if (operate.equals("杠")) {
				p.increaseQi(5);
				CCMD11031 ccmd031 = new CCMD11031();
				ccmd031.auto_deal(p, 5);
			}

			if (!operate.equals("胡")) // 检查技能
			{
				tmpcur.getSkillutil().checkSkillAfterDapai(tmpcur,p,operate);// tmpcur打牌的玩家  p 吃碰杠操作的玩家
			}
		}
		// 发送成功的buf
		// 2 .改变当前玩家为操作 成功的玩家(不摸牌，开始打牌计时)
		waitDeal_Operate = null;
		waitDeal_cmd = null;
	}

	private synchronized List<Integer> checkPriority(String[] arrStr,
			String target) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < arrStr.length; i++) {
			if (arrStr[i] == null)
				continue;
			if (arrStr[i].equals(target))
				list.add(i);
		}
		return list;
	}

	/**
	 * timer
	 */
	public synchronized void run(Timeout out) throws Exception {

		this.cancelTimeOut();
		int flag = this.getTimeFlag();
		Log.info("TimeFlag:" + flag);
		
		switch (flag) {
		case TIME_DAPAI:
			// 自动打出一张牌
			if (this.curPlayer == null)
				return;
			if(!this.curPlayer.canDapai())
				return;
			if (this.curPlayer.getTouchedPai() == -1) { // 随机一张
				List<Integer> shoupai = this.curPlayer.getShoupai();
				MyArrays.sort(shoupai);
				int num = (int) (Math.random() * shoupai.size());
				int id = shoupai.get(num);
				CCMD11004 ccmd = new CCMD11004();
				ccmd.setPlayer(curPlayer);
				ccmd.auto_deal(id);
			} else {
				int id = this.curPlayer.getTouchedPai();
				CCMD11004 ccmd = new CCMD11004();
				ccmd.setPlayer(curPlayer);
				ccmd.auto_deal(id);
			}
			break;
		case TIME_DEALPAI:
			// new CCMD11010().auto_deal(this.curPlayer);
			this.waitDealPlayer.clear();
			waitDeal_back(null, null, null);

			break;
		case TIME_QIANGGANGHU:
			// 抢杠胡超时// 当前杠的玩家继续摸牌打牌摸牌打牌
			this.waitDealPlayer.clear();
			CCMD11008 ccmd = new CCMD11008();
			ccmd.timeout_qiangganghu(this.curPlayer);
			break;
		case TIME_READY:
			// 全部准备 ， 开始发牌
//			for (int i = 1; i <= 4; i++) {
////				if (readyP_Count[i - 1] == 0)
////					this.addTuoGuanQueue(this.players.get(i));
//				readyP_Count[i - 1] = 1;
//			}  
			Log.info("--->TIME_READY startFirstTimer");
			readyP_Count = new int[]{1,1,1,1};
			this.startFirstTimer(null);

			break;
		case TIME_ROBOT:
			MgsPlayer r = Global.getRobot();
			if (r != null) {
				this.addPlayer(r);
			} else {
				start_robotTimer();
			}
			break;
		case TIME_HEART:
			break;
		}
	}

	public synchronized void checkRobot() {
	}

	// ///////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////
	// 房间生命周期

	public synchronized void createdRoom() {
		room_state = MgsRoom.CREATED;
	}

	public synchronized void started() {
		room_state = MgsRoom.STARTED;
//		for (int i = 1; i <= 4; i++) {
//			MgsPlayer p = this.players.get(i);
//			if (p != null)
//				p.startHeartBeat();
//		}
	}

	public synchronized void ended() {
		room_state = MgsRoom.ENDED;
	}

	public synchronized void destoryRoom() {
		System.out.println("销毁房间");
		room_state = MgsRoom.DESTORYED;
		this.cancelTimeOut();
		if (this.players == null)	
			return;
		for (int i = 1; i <= 4; i++) {
			MgsPlayer p = this.players.get(i);
			if (p != null)
				p.setRoom(null);
		}
		this.players = null;
		this.leavedPlayer = null;
		this.tuoguanPlayer = null;

		Global.rManager.destroyRoom(this);

	}

	// /////////////////////////////////////////////////////////

	public int compareTo(Room o) {
		return this.roomID - o.getRoomID();
	}

	// //////////////////////////////////////////////////////////
	public void setManager(MgsPlayer manager) {
		this.manager = manager;
	}

	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Hashtable<Integer, MgsPlayer> getPlayers() {
		return players;
	}

	public int getMAX_COUNT() {
		return MAX_COUNT;
	}

	public void setMAX_COUNT(int mAX_COUNT) {
		MAX_COUNT = mAX_COUNT;
	}

	public int getPlayerLimit() {
		return playerLimit;
	}

	public synchronized void setPlayerLimit(int playerLimit) {
		this.playerLimit = playerLimit;
		if (playerLimit == 0)
			this.MAX_COUNT = 4;
		if (playerLimit != 0)
			this.MAX_COUNT = playerLimit;
		if (playerLimit == 2) {
			this.quanLimit = 99999;
		}
	}

	public int getSkillLimit() {
		return skillLimit;
	}

	public void setSkillLimit(int skillLimit) {
		this.skillLimit = skillLimit;
	}

	public int getChipsLimit() {
		return chipsLimit;
	}

	public void setChipsLimit(int chipsLimit) {
		this.chipsLimit = chipsLimit;
	}

	public Timeout getTimeout() {
		return timeout;
	}

	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}

	public int getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(int timeFlag) {
		this.timeFlag = timeFlag;
	}

	public MgsPlayer getZhuang() {
		return zhuang;
	}

	public void setZhuang(MgsPlayer zhuang) {
		this.zhuang = zhuang;
	}

	public MgsPlayer getCurPlayer() {
		return curPlayer;
	}

	public void setCurPlayer(MgsPlayer curPlayer) {
		this.curPlayer = curPlayer;
	}

	public int getCurPaiId() {
		return curPaiId;
	}

	public void setCurPaiId(int curPaiId) {
		this.curPaiId = curPaiId;
	}

	public boolean isTiantingFlag() {
		return tiantingFlag;
	}

	public void setTiantingFlag(boolean tiantingFlag) {
		this.tiantingFlag = tiantingFlag;
	}

	public int getCountDapai() {
		return countDapai;
	}

	public void setCountDapai(int countDapai) {
		this.countDapai = countDapai;
	}

	public List<MgsPlayer> getWaitDealPlayer() {
		return waitDealPlayer;
	}

	public void setWaitDealPlayer(List<MgsPlayer> waitDealPlayer) {
		this.waitDealPlayer = waitDealPlayer;
	}

	public List<Integer> getIDList() {
		return IDList;
	}

	public void setIDList(List<Integer> iDList) {
		IDList = iDList;
	}

	public int getQuanLimit() {
		return quanLimit;
	}

	public void setQuanLimit(int quanLimit) {
		this.quanLimit = quanLimit;
	}

	public int getZhuangId() {
		return zhuangId;
	}

	public void setZhuangId(int zhuangId) {
		this.zhuangId = zhuangId;
	}

	public MgsPlayer getManager() {
		return manager;
	}

	public int getBeishu() {
		return beishu;
	}

	public void setBeishu(int beishu) {
		this.beishu = beishu;
	}

}
