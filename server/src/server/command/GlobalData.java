package server.command;

public class GlobalData {

	public static final int  MAX_JUQING_1  =10;
	public static final int  MAX_JUQING_2  =10;
	public static final int  MAX_JUQING_3  =10;
	
	public static final int MAXROLE_LEVEL = 10;
	
	public static final String PINGTAI_SERVER = "IOS"; //IOS  AND
	
	public static final int PROGRAM_VERSION = 10002;
	
	
	public	static final String AppKey	= "9b1fa1a96ed0d726d2bceb6df8a02d8c";
	public	static final String AppSecret	= "1106640b6df74133173be7328bae64c2";
	public	static final String AppID		= "201680431";
	
	/**
	 * 360 签名
	 */
	
	/**
	 * 没章剧情解锁奖励金币
	 */
	public static final int JUQING1_GOLD = 100;
	public static final int JUQING2_GOLD = 200;
	public static final int JUQING3_GOLD = 300;
	public static final int JUQING4_GOLD = 400;
	
	//形象等级解锁机能机能
	public static final int[][] rolemap = 
		{
			{100,3,3,100},           //角色ID， 机能解锁等级， 解锁机能ID 每级需要经验
			{101,3,4,100},
			{102,-1,0,100},
			{103,-1,0,100},
			{104,3,7,100},
			{105,5,1,100},
			{1001,-1,0,100},
			{1002,-1,0,100},
			{1003,-1,0,100},
			{1004,-1,0,100},
			{1005,-1,0,100},
			{1006,-1,0,100},
			{2001,-1,0,100},
			{2002,-1,0,100},
			{2003,-1,0,100}
		};
	public synchronized static int[] getRoleData(int roleId)
	{
		for(int i = 0 ; i < rolemap.length; i++)
		{
			int[] tmp = rolemap[i];
			if(roleId == tmp[0])
				return tmp;
		}
		return null;
	}
	
}
