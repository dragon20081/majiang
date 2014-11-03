package server.mj;

public class MyProtocol {
	
	public static final byte MY_INFO				= 83;       //个人信息
	//<<指令(byte)
	//>>指令(byte) uid(int) firstLand(byte) titleId(byte) headId(byte) todayTask(byte) todayWinNum(short) winNum(int) loseNum(int) exp(int) nickName(String)
	public static final byte MOD_TITLE				= 84;       //修改称号
	//<<指令(byte) titleID(byte)
	//>>指令(byte) titleID(byte)
	public static final byte MOD_HEAD				= 85;    	//修改头像
	//>>指令(byte) headId(byte)
	//<<指令(byte) headId(byte)
	public static final byte MOD_TASK           	= 86;		//修改任务状�?
	//>>指令(byte) todayTask(byte)
	//<<指令(byte) todatTask(byte)
	public static final byte MOD_NICK				= 87;       //修改昵称
	//<<指令(byte) gold(short) nickName(String)
	public static final byte MOD_NICK_SUCC			= 88;       //修改昵称成功
	//>>指令(byte)
	public static final byte MOD_NICK_FAIL			= 89;		//修改昵称失败
	//>>指令(byte)
	public static final byte MOD_EXP			    = 90;		//修改经验�?	//<<指令(byte) exp(int)
	//>>指令(byte) exp(int)
	public static final byte MY_PROP					= 91;       //道具信息
	//<<指令(byte)
	//>>指令(byte) pro0(short) pro1(short) pro2(short) pro3(short) pro4(short) pro5(short) pro6(short) pro7(short) pro8(short) pro9(short)
	public static final byte MOD_PROP_ADD			= 92;       //修改道具 增加
	//<<指令(byte) 编号number(byte) addNum(short)
	//>>指令(byte) 编号number(byte) nowNum(short)
	public static final byte MOD_PROP_CUT			= 93;       //修改道具 减少
	//<<指令(byte) 编号number(byte)
	//>>指令(byte) 编号number(byte) nowNum(short)
	public static final byte MY_FUND				= 94;       //基金信息
	//<<指令(byte) 
	//>>指令(byte) chips(int) gold(int)
	public static final byte MOD_GOLD				= 95;       //修改金砖
	//<<指令(byte) gold(int)
	//>>指令(byte) gold(int)
	public static final byte MOD_CHIPS				= 96;       //修改筹码
	//<<指令(byte) chips(int)
	//>>指令(byte) chips(int)
	public static final byte MY_STATE				= 97;       //状�?信息
	//<<指令(byte)
	//>>指令(byte) VIP0(short) VIP1(short) VIP2(short) VIP3(short) receive0(int) receive1(int) receive2(int) receive3(int)
	public static final byte MOD_STATE				= 98;       //修改状�?时间
	//<<指令(byte)
	/*
	*number(byte) 		 addNum(byte)
	*0		VIP0
	*1		VIP1
	*2		VIP2
	*3		VIP3
	*4		receive0
	*5		receive1
	*6		receive2
	*7		receive3
	*/
	//>>指令(byte) number(byte) 	nowNum(int)
	public static final byte MY_TITLES				= 99;       //称号信息
	//<<指令(byte) 
	//>>指令(byte) title0(int) title1(int)
	public static final byte MOD_TITLES				= 100;       //修改获得称号
	//<<指令(byte) title0(int) title1(int)
	//>>指令(byte) title0(int) title1(int)
	
	
	public static final byte REQUEST_ROOM			= 101;       //请求房间
	//<<指令(byte) level(byte)
	//>>指令(byte) ing(byte) lun(byte) onepour(short) 
	public static final byte PLAYER_JOIN			= 102;       //加入人物
	//>>指令(byte)  id(byte) state(byte) titleId(byte) headId(byte) 
	//				VIP0(short) VIP1(short) VIP2(short) VIP3(short) receive0(int) receive1(int) receive2(int) receive3(int)
	//				uid(int) exp(int) winNum(int) loseNum(int) chips(int) pour(int) 
	//				nickName(String)
	/*
	state(byte)
	0			未准�?	1			准备
	2			有牌
	3			看牌
	4			弃牌
	5			比牌失败
	*/
	public static final byte PLAYER_READY			= 103;       //完成准备
	//<<指令(byte)
	//>>指令(byte)	id(byte)
	public static final byte PLAYER_CANCEL			= 104;       //取消准备
	//<<指令(byte)
	public static final byte PLAYER_TIMEROUT		= 105;      //超时
	//<<指令(byte)
	public static final byte PLAYER_LEAVE			= 106;      //离开房间
	//<<指令(byte)
	//>>指令(byte) id(byte)
	public static final byte GAME_RESULT_WIN		= 107;      //游戏结果 �?	//<<指令(byte) 
	public static final byte GAME_RESULT_LOSE		= 108;      //游戏结果 �?	//<<指令(byte)
	public static final byte ONLINE_INFO      	 	= 109;    	//在线玩家
	//<<指令(byte)
	//>>指令(byte) maxOnline(int) nowOnline(int)
	
	public static final byte FA_PAI					= 110;
	//>>指令(byte) pai0(byte) pai1(byte) pai2(byte)
	public static final byte GAME_START				= 111;
	//>>指令(byte) Banker(byte)
	public static final byte PLAYER_DO				= 112;
	//<<指令(byte)  
		//												
						/*
						0		跟注		+	筹码变量chips(int)	 
						1		看牌
						2		弃牌
						3		加注 + 改变后的单注onepour(short) 筹码变量chips(int) 
						4		比牌 + 目标IDtargetId(byte)  
						5	比牌结果 + 目标ID(byte) 我的�?byte) 目标的牌(byte) 败的ID(byte) 筹码变量chips(short)	
						*/
		//>>指令(byte) id(byte)  number(byte)
						/*
						id(byte)	0 跟注 + 筹码变量(int)  
						id(byte)	1 看牌
						id(byte)	2 弃牌
						id(byte)	3 加注 +	改变后的单注(short) 筹码变量(int) 
									4 比牌 +	目标ID的牌(byte)(byte)(byte)
						id(byte)	5 比牌结果		+	目标ID(byte) 我的�?byte)(byte)(byte) 目标的牌(byte)(byte)(byte) 败的ID(byte) 筹码变量chips(int)
						*/
	public static final byte ROBOT_JOIN			= 113;
	//<<指令(byte) titleId(byte) headId(byte) chips(int) nickName(String)
	//>>指令(byte) id(byte)
	public static final byte ROBOT_DO			= 114;
	//<<指令(byte) 玩家操作指令
	public static final byte ROBOT_PAI			= 115;
	//<<指令(byte)
	//>>指令(byte) pai0(byte) pai1(byte) pai2(byte)
	public static final byte GAME_DING			= 116;		//游戏封顶
	//>>指令(byte) number(byte) id(byte) pai0(byte) pai1(byte) pai2(byte)
	
	public static final byte GAME_OVER			= 117;		//通道游戏结束
	//>>指令(byte) winID(byte) //结束
	public static final byte NEXT				= 118;
	//>>指令(byte) curid(byte) lun(byte)
	
		
	public static final byte PUBLIC_NOTICE			= 122;
	public static final byte HEART					= 123;
	public static final byte VERSION				= 124;		//版本�?	public static final byte CHONG					= 125;
	public static final byte CLEAR_NOTICE			= 126;
	public static final byte CHONG_DATA				 = -1;
}
